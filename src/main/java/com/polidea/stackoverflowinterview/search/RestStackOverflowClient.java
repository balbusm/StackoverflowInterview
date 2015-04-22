package com.polidea.stackoverflowinterview.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.polidea.stackoverflowinterview.domain.Owner;
import com.polidea.stackoverflowinterview.domain.Summary;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static com.polidea.stackoverflowinterview.search.QueryResult.ErrorCode;

public class RestStackOverflowClient implements StackOverflowClient {
    public static final String TAG = RestStackOverflowClient.class.getSimpleName();
    public static final String BASE_STACK_EXCHANGE = "http://api.stackexchange.com/2.2/search?";
    public static final String ORDER = "order";
    public static final String SORT = "sort";
    public static final String INTITLE = "intitle";
    public static final String ARG_TEMPLATE = "&%s=%s";

    private final HttpClient client = new DefaultHttpClient();

    private String applyArgument(String name, Object value) {
        return String.format(ARG_TEMPLATE, name, value.toString());
    }

    private String prepareQuery(QueryArgument queryArgument) throws ClientException {
        try {
            StringBuilder builder = new StringBuilder(BASE_STACK_EXCHANGE);
            final String encodedIntitle = URLEncoder.encode(queryArgument.getIntitle(), "UTF-8");
            builder.append(applyArgument(INTITLE, encodedIntitle));
            builder.append(applyArgument(ORDER, queryArgument.getOrder()));
            builder.append(applyArgument(SORT, queryArgument.getSort()));
            builder.append(applyArgument("site", "stackoverflow"));
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            throw new ClientException(ErrorCode.UNSPECIFIED_ERROR, e);
        }
    }

    private List<Summary> jsonToListSummary(String json) throws ClientException {
        List<Summary> summaries = Lists.newArrayList();
        try {
            final JSONObject rootObject = new JSONObject(json);
            JSONArray items = rootObject.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                final JSONObject summary = items.getJSONObject(i);
                summaries.add(parseSummary(summary));
            }
        } catch (JSONException e) {
            throw new ClientException(ErrorCode.DATA_ERROR, e);
        } catch (IOException e) {
            throw new ClientException(ErrorCode.DATA_ERROR, e);
        }

        return summaries;
    }

    private Summary parseSummary(JSONObject summary) throws JSONException, IOException, ClientException {
        final Owner owner = parseOwner(summary);

        final int answerCount = summary.getInt("answer_count");
        final String title = StringEscapeUtils.unescapeHtml4(summary.getString("title"));
        final String link = summary.getString("link");

        return new Summary(owner, title, answerCount, link);
    }

    private Owner parseOwner(JSONObject summary) throws JSONException, IOException, ClientException {
        final JSONObject jsonOwner = summary.getJSONObject("owner");
        Bitmap profileImage = null;
        if (jsonOwner.has("profile_image")) {
            final String profileImageLink = jsonOwner.getString("profile_image");
            profileImage = downloadBitmap(profileImageLink);
        }
        String displayName = jsonOwner.getString("display_name");
        Owner owner = new Owner(displayName, profileImage);
        return owner;
    }

    private Bitmap downloadBitmap(String link) throws ClientException {
        final HttpResponse httpResponse = request(link);
        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        verifyStatusCode(statusCode);
        HttpEntity entity = httpResponse.getEntity();

        Bitmap bitmap = readBitmapEntiry(entity);
        return bitmap;
    }

    @Override
    public QueryResult getResults(QueryArgument queryArgument) {
        final String jsonResult;
        try {
            jsonResult = queryServer(queryArgument);
            final List<Summary> summaries = jsonToListSummary(jsonResult);
            return createQueryResult(summaries);
        } catch (ClientException e) {
            Log.e(TAG, "Exception while getting result from server", e);
            return new QueryResult(e.getErrorCode());
        }
    }

    private QueryResult createQueryResult(List<Summary> summaries) {
        if (summaries.isEmpty()) {
            return new QueryResult(ErrorCode.NO_RESULT);
        }
        return new QueryResult(summaries);
    }

    public String queryServer(QueryArgument queryArgument) throws ClientException {
        HttpEntity httpEntity = null;
        try {
            String httpRequest = prepareQuery(queryArgument);
            HttpResponse response = request(httpRequest);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            verifyStatusCode(statusCode);
            httpEntity = response.getEntity();
            return readEntiry(httpEntity);
        } finally {
            if (httpEntity != null) {
                try {
                    httpEntity.consumeContent();
                } catch (IOException e) {
                    Log.e(TAG, "Error while consuming content", e);
                }
            }
        }
    }

    private String readEntiry(HttpEntity httpEntity) throws ClientException {
        try {
            final String encoding = httpEntity.getContentEncoding().getValue();
            InputStream in;
            if ("gzip".equals(encoding)) {
                in = AndroidHttpClient.getUngzippedContent(httpEntity);
            } else {
                in = httpEntity.getContent();
            }
            in = new BufferedInputStream(in);
            final String entityString = CharStreams.toString(new InputStreamReader(in));
            if (Strings.isNullOrEmpty(entityString)) {
                throw new ClientException(ErrorCode.SERVER_ERROR);
            }
            return entityString;
        } catch (IOException e) {
            throw new ClientException(ErrorCode.SERVER_ERROR, e);
        }
    }

    private Bitmap readBitmapEntiry(HttpEntity httpEntity) throws ClientException {
        try {
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(httpEntity);

            InputStream inputStream = bufHttpEntity.getContent();

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            throw new ClientException(ErrorCode.SERVER_ERROR, e);
        }
    }

    private void verifyStatusCode(int statusCode) throws ClientException {
        if (statusCode != HttpStatus.SC_OK) {
            Log.e(TAG, "Couldn't query server. Response code: " + statusCode);
            throw new ClientException(ErrorCode.SERVER_ERROR);
        }
    }

    private HttpResponse request(String requestUrl) throws ClientException {
        try {
            HttpGet httpGet = new HttpGet(requestUrl);
            HttpResponse response = client.execute(httpGet);
            return response;
        } catch (IOException e) {
            throw new ClientException(ErrorCode.CONNECTION_ERROR, e);
        }

    }
}