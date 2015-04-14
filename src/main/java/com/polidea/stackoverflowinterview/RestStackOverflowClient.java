package com.polidea.stackoverflowinterview;

import android.util.Log;

import java.util.List;

public class RestStackOverflowClient implements StackOverflowClient {
    public List<StackOverflowSearchResult> getResults() {
                try {
                    final String url = "http://rest-service.guides.spring.io/greeting";
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    Greeting greeting = restTemplate.getForObject(url, Greeting.class);
                    return greeting;
                } catch (Exception e) {
                    Log.e("MainActivity", e.getMessage(), e);
                }

                return null;
            }

    }
}
