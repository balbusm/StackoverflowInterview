package com.polidea.stackoverflowinterview.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.polidea.stackoverflowinterview.R;
import com.polidea.stackoverflowinterview.domain.Summary;
import com.polidea.stackoverflowinterview.search.QueryResult;

import java.util.Map;

import static com.polidea.stackoverflowinterview.search.QueryResult.ErrorCode;

public class ResultActivity extends Activity implements QueryTaskFragment.QueryTaskCallbacks {

    private static final Map<ErrorCode, Integer> ERROR_CODE_TO_STRING_RES;

    static {
        final ImmutableMap.Builder<ErrorCode, Integer> mapBuilder = new ImmutableMap.Builder<ErrorCode, Integer>();
        mapBuilder.put(ErrorCode.CONNECTION_ERROR, R.string.connection_error);
        mapBuilder.put(ErrorCode.DATA_ERROR, R.string.data_error);
        mapBuilder.put(ErrorCode.SERVER_ERROR, R.string.server_error);
        mapBuilder.put(ErrorCode.NO_RESULT, R.string.no_results);
        mapBuilder.put(ErrorCode.UNSPECIFIED_ERROR, R.string.unspecified_error);
        ERROR_CODE_TO_STRING_RES = mapBuilder.build();
    }

    private static final String QUERY_TASK_TAG = "QUERY_TASK_TAG";

    private QueryTaskFragment mQueryTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        mQueryTaskFragment = (QueryTaskFragment) fragmentManager.findFragmentByTag(QUERY_TASK_TAG);

        if (mQueryTaskFragment == null) {
            mQueryTaskFragment = new QueryTaskFragment();
            fragmentManager.beginTransaction().add(mQueryTaskFragment, QUERY_TASK_TAG).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mQueryTaskFragment.executeQueryTask();
        return true;
    }

    @Override
    public void onPreExecute() {
        setContentView(R.layout.loading);
    }

    @Override
    public void onPostExecute(QueryResult queryResult) {
        if (!checkErroCode(queryResult)) {
            prepareListView(queryResult);
        }
    }

    private void prepareListView(QueryResult queryResult) {
        final ResultAdapter resultAdapter = new ResultAdapter(this, queryResult.getSummaryList());
        setContentView(R.layout.list);
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(resultAdapter);
        resultAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Summary summary = (Summary) adapterView.getAdapter().getItem(i);
                final String link = summary.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(intent);
            }
        });
    }

    private boolean checkErroCode(QueryResult queryResult) {
        boolean hasError = queryResult.hasError();
        if (hasError) {
            final Integer stringResId = ERROR_CODE_TO_STRING_RES.get(queryResult.getErrorCode());
            showError(stringResId);
        }
        return hasError;
    }

    private void showError(int stringResId) {
        final TextView textView = (TextView) findViewById(R.id.textId);
        textView.setText(stringResId);
    }

}