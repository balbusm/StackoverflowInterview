package com.polidea.stackoverflowinterview.search;

import android.graphics.Bitmap;

import com.google.common.collect.Lists;
import com.polidea.stackoverflowinterview.domain.Owner;
import com.polidea.stackoverflowinterview.domain.Summary;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class JsonParserImpl implements JsonParser {

    @Inject
    HttpResolver resolver;

    public List<Summary> parseToListSummary(String json) throws ClientException {
        List<Summary> summaries = Lists.newArrayList();
        try {
            final JSONObject rootObject = new JSONObject(json);
            JSONArray items = rootObject.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                final JSONObject summary = items.getJSONObject(i);
                summaries.add(parseSummary(summary));
            }
        } catch (JSONException e) {
            throw new ClientException(QueryResult.ErrorCode.DATA_ERROR, e);
        } catch (IOException e) {
            throw new ClientException(QueryResult.ErrorCode.DATA_ERROR, e);
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
            profileImage = resolver.getBitmap(profileImageLink);
        }
        String displayName = jsonOwner.getString("display_name");
        Owner owner = new Owner(displayName, profileImage);
        return owner;
    }

}
