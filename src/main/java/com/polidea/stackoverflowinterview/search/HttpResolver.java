package com.polidea.stackoverflowinterview.search;

import android.graphics.Bitmap;

/**
 * Helps to get content from the web
 */
public interface HttpResolver {
    /**
     * Returns string content on given http request
     * @param httpRequest
     * @return
     * @throws ClientException
     */
    String getContent(String httpRequest) throws ClientException;

    /**
     * Downloads bitmap on given http request
     * @param httpRequest
     * @return
     * @throws ClientException
     */
    Bitmap getBitmap(String httpRequest) throws ClientException;
}
