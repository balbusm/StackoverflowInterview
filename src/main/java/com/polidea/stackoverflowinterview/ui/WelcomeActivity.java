package com.polidea.stackoverflowinterview.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.polidea.stackoverflowinterview.R;
import com.polidea.stackoverflowinterview.search.QueryArgument;

public class WelcomeActivity extends Activity {

    private TextView searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBox = (TextView) findViewById(R.id.searchText);
    }

    public void searchOnStackOverflow(View v) {
        final String titleToLook = searchBox.getText().toString();
        final Intent intent = new Intent(this, ResultActivity.class);
        final QueryArgument queryArgument = new QueryArgument(titleToLook, QueryArgument.Order.ASC, QueryArgument.Sort.ACTIVITY);
        intent.putExtra("QUERY", queryArgument);
        startActivity(intent);
    }
}
