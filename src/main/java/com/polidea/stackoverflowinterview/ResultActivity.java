package com.polidea.stackoverflowinterview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class ResultActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String query = getIntent().getStringExtra("query");
        new AsyncTask<String, Integer, List<>>()
    }
}
