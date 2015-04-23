package com.polidea.stackoverflowinterview.search;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

public class HttpResolverImpl implements HttpResolver {

    private static final String TAG = HttpResolverImpl.class.getSimpleName();

    @Inject
    HttpClient client;

    @Override
    public String getContent(String httpRequest) throws ClientException {
        HttpEntity httpEntity = null;
        try {
            Log.d(TAG, "Getting content for " + httpRequest);
            HttpResponse response = request(httpRequest);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            verifyStatusCode(statusCode);
            httpEntity = response.getEntity();
            final String content = readEntiry(httpEntity);
            Log.d(TAG, "Finished getting content for " + httpRequest);
            return content;
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

    @Override
    public Bitmap getBitmap(String httpRequest) throws ClientException {
        Log.d(TAG, "Getting bitmap from " + httpRequest);

        final HttpResponse httpResponse = request(httpRequest);
        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        verifyStatusCode(statusCode);
        HttpEntity entity = httpResponse.getEntity();

        Bitmap bitmap = readBitmapEntiry(entity);
        Log.d(TAG, "Finished getting bitmap from " + httpRequest);
        return bitmap;
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
                throw new ClientException(QueryResult.ErrorCode.SERVER_ERROR);
            }
            return entityString;
        } catch (IOException e) {
            throw new ClientException(QueryResult.ErrorCode.SERVER_ERROR, e);
        }
    }

    private Bitmap readBitmapEntiry(HttpEntity httpEntity) throws ClientException {
        try {
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(httpEntity);

            InputStream inputStream = bufHttpEntity.getContent();

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            throw new ClientException(QueryResult.ErrorCode.SERVER_ERROR, e);
        }
    }

    private void verifyStatusCode(int statusCode) throws ClientException {
        if (statusCode != HttpStatus.SC_OK) {
            Log.e(TAG, "Couldn't query server. Response code: " + statusCode);
            throw new ClientException(QueryResult.ErrorCode.SERVER_ERROR);
        }
    }

    private HttpResponse request(String requestUrl) throws ClientException {
        try {
            HttpGet httpGet = new HttpGet(requestUrl);
            HttpResponse response = client.execute(httpGet);
            return response;
        } catch (IOException e) {
            throw new ClientException(QueryResult.ErrorCode.CONNECTION_ERROR, e);
        }

    }
}
