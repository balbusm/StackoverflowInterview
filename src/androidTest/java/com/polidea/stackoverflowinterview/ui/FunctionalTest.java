package com.polidea.stackoverflowinterview.ui;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import com.polidea.stackoverflowinterview.ModifiableInjector;
import com.polidea.stackoverflowinterview.R;
import com.polidea.stackoverflowinterview.domain.Owner;
import com.polidea.stackoverflowinterview.domain.Summary;
import com.polidea.stackoverflowinterview.search.QueryArgument;
import com.polidea.stackoverflowinterview.search.QueryResult;
import com.polidea.stackoverflowinterview.search.StackOverflowClient;

import org.hamcrest.Matchers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FunctionalTest extends
        ActivityInstrumentationTestCase2<WelcomeActivity> {

    private MockClientModule mockClientModule;

    public FunctionalTest() {
        super(WelcomeActivity.class);
    }

    private QueryResult prepQueryResult1() {
        List<Summary> summaryList = new ArrayList<Summary>();

        Owner owner = new Owner("John", null);
        Summary summary = new Summary(owner, "This is simple Hello World", 2, null);
        summaryList.add(summary);

        owner = new Owner("Jack", null);
        summary = new Summary(owner, "This is just another Hello World question", 4, null);
        summaryList.add(summary);

        owner = new Owner("James", null);
        summary = new Summary(owner, "Third Hello World question", 0, null);
        summaryList.add(summary);

        final QueryResult queryResult = new QueryResult(summaryList);
        return queryResult;
    }

    private QueryResult prepQueryResult2() {
        List<Summary> summaryList = new ArrayList<Summary>();

        Owner owner = new Owner("Tommy", null);
        Summary summary = new Summary(owner, "This is new one Hello World", 1, null);
        summaryList.add(summary);

        owner = new Owner("Teddy", null);
        summary = new Summary(owner, "This is new two Hello World question", 3, null);
        summaryList.add(summary);

        owner = new Owner("Tikki", null);
        summary = new Summary(owner, "This is new three Hello World question", 5, null);
        summaryList.add(summary);

        return new QueryResult(summaryList);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockClientModule = new MockClientModule();
        ((ModifiableInjector) getActivity().getApplication()).modifyInjector(mockClientModule);
    }

    public void testElementsDisplayed() {
        final QueryResult queryResult = prepQueryResult1();
        mockClientModule.setResultQuery(queryResult);

        onView(withId(R.id.searchText)).perform(typeText("Hello World"));
        onView(withId(R.id.searchButton)).perform(click());

        Summary summary = queryResult.getSummaryList().get(0);
        onData(allOf(is(instanceOf(Summary.class)), is(summary))).atPosition(0).onChildView(withId(R.id.author_name_id)).check(matches(withText(summary.getOwner().getDisplayName())));

        summary = queryResult.getSummaryList().get(1);
        onData(allOf(is(instanceOf(Summary.class)), is(summary))).atPosition(0).onChildView(withId(R.id.title_id)).check(matches(withText(summary.getTitle())));

        summary = queryResult.getSummaryList().get(2);
        onData(allOf(is(instanceOf(Summary.class)), is(summary))).atPosition(0).onChildView(withId(R.id.answers_id)).check(matches(withText(Integer.toString(summary.getAnswersCount()))));

    }

    public void testRefresh() {
        final QueryResult queryResult1 = prepQueryResult1();
        final QueryResult queryResult2 = prepQueryResult2();
        mockClientModule.setResultQuery(queryResult1, queryResult2);

        onView(withId(R.id.searchText)).perform(typeText("Hello World"));
        onView(withId(R.id.searchButton)).perform(click());

        onView(withId(R.id.refresh_id)).perform(click());

        Summary summary = queryResult1.getSummaryList().get(0);
        onView(withText(summary.getOwner().getDisplayName())).check(doesNotExist());

        summary = queryResult2.getSummaryList().get(0);
        onData(allOf(is(instanceOf(Summary.class)), is(summary))).atPosition(0).onChildView(withId(R.id.author_name_id)).check(matches(withText(summary.getOwner().getDisplayName())));

        summary = queryResult2.getSummaryList().get(1);
        onData(allOf(is(instanceOf(Summary.class)), is(summary))).atPosition(0).onChildView(withId(R.id.title_id)).check(matches(withText(summary.getTitle())));

        summary = queryResult2.getSummaryList().get(2);
        onData(allOf(is(instanceOf(Summary.class)), is(summary))).atPosition(0).onChildView(withId(R.id.answers_id)).check(matches(withText(Integer.toString(summary.getAnswersCount()))));

    }

    @Module(injects = QueryTaskFragment.class)
    public class MockClientModule {

        private QueryResult queryResult;
        private QueryResult[] queryResults;

        @Provides
        @Singleton
        public StackOverflowClient getStackOverflowClient() {
            final StackOverflowClient mock = mock(StackOverflowClient.class);

            when(mock.getResults(any(QueryArgument.class))).thenReturn(queryResult, queryResults);
            return mock;
        }

        public void setResultQuery(QueryResult queryResult, QueryResult... queryResults) {
            this.queryResult = queryResult;
            this.queryResults = queryResults;
        }
    }


}