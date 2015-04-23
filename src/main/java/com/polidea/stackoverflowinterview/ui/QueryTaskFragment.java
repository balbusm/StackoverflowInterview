package com.polidea.stackoverflowinterview.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import com.polidea.stackoverflowinterview.Injector;
import com.polidea.stackoverflowinterview.search.QueryArgument;
import com.polidea.stackoverflowinterview.search.QueryResult;
import com.polidea.stackoverflowinterview.search.StackOverflowClient;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class QueryTaskFragment extends Fragment {

    @Inject
    StackOverflowClient client;
    private QueryTaskCallbacks mCallbacks;
    private QueryAsyncTask mQueryAsyncTask;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (QueryTaskCallbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((Injector) getActivity().getApplication()).inject(this);
        if (savedInstanceState == null) {
            executeQueryTask();
        }
    }

    public void executeQueryTask() {
        final QueryArgument queryArgument = checkNotNull((QueryArgument) getActivity().getIntent().getParcelableExtra("QUERY"));
        // start new async task when previous was finished
        if (mQueryAsyncTask == null || mQueryAsyncTask.getStatus() == AsyncTask.Status.FINISHED) {
            mQueryAsyncTask = new QueryAsyncTask();
            mQueryAsyncTask.execute(queryArgument);

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    interface QueryTaskCallbacks {
        void onPreExecute();

        void onPostExecute(QueryResult result);
    }

    private class QueryAsyncTask extends AsyncTask<QueryArgument, Void, QueryResult> {

        @Override
        protected void onPreExecute() {
            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
        }

        @Override
        protected QueryResult doInBackground(QueryArgument... queryArguments) {
            final QueryResult result = client.getResults(queryArguments[0]);

            return result;
        }

        @Override
        protected void onPostExecute(final QueryResult queryResult) {
            if (mCallbacks != null) {
                mCallbacks.onPostExecute(queryResult);
            }
        }

    }

}
