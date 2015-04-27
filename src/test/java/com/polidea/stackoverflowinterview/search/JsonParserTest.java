package com.polidea.stackoverflowinterview.search;

import com.polidea.stackoverflowinterview.domain.Summary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml")
public class JsonParserTest {

    private ObjectGraph graph;
    private JsonParserImpl jsonParser;

    @Before
    public void setUp() {
        graph = ObjectGraph.create(new ParserModule());
        jsonParser = new JsonParserImpl();
        graph.inject(jsonParser);
    }

    private String formatItem(String imageLink, String displayName, String count, String link, String title) {
        String template =
                " {     \"owner\": {\n" +
                        "        \"profile_image\": \"%s\",\n" +
                        "        \"display_name\": \"%s\"\n" +
                        "      },\n" +
                        "      \"answer_count\": %s,\n" +
                        "      \"link\": \"%s\",\n" +
                        "      \"title\": \"%s\"\n}";
        return String.format(template, imageLink, displayName, count, link, title);
    }

    private String formatItems(String... items) {
        String jsonItems =
                "{\n" +
                        "  \"items\": [\n" +
                        addItems(items) +
                        "  ]\n" +
                        "}";
        return jsonItems;
    }

    private String addItems(String... items) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String item : items) {
            if (!first) {
                builder.append(",");
            }
            first = false;
            builder.append(item);
        }
        return builder.toString();
    }

    @Test
    public void testRestObject() throws ClientException {
        final String item1 = formatItem("imageLinkA", "user1A", "2", "answerLinkA", "title1A");
        final String item2 = formatItem("imageLink", "user2", "1", "answerLink", "title2");
        final String item3 = formatItem("imageLink3", "user3", "1", "answerLink", "title3");
        final String json = formatItems(item1, item2, item3);
        final List<Summary> summaries = jsonParser.parseToListSummary(json);
        assertThat(summaries.get(0).getOwner().getDisplayName(), is("user1A"));
        assertThat(summaries.get(0).getTitle(), is("title1A"));
        assertThat(summaries.get(0).getLink(), is("answerLinkA"));

        assertThat(summaries.get(1).getTitle(), is("title2"));
        assertThat(summaries.get(2).getOwner().getDisplayName(), is("user3"));

    }

    @Module(injects = JsonParserImpl.class)
    public class ParserModule {

        @Singleton
        @Provides
        public HttpResolver provideHttpResolver() {
            try {
                final HttpResolver client = mock(HttpResolver.class);
                when(client.getBitmap(any(String.class))).thenReturn(null);
                return client;
            } catch (ClientException e) {
                throw new RuntimeException(e);
            }
        }

    }
}