package tw.kayjean.ui.client;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A SuggestOracle that uses a server side source of suggestions. Instances of this class can not
 * be shared between SuggestBox instances.
 *
 * @author amoffat Alex Moffat
 */
public class ServerSuggestOracle extends SuggestOracle {

    /**
     * Class to hold a response from the server.
     */
    private static class ServerResponse {

        /**
         * Request made by the SuggestBox.
         */
        private final Request request;

        /**
         * The number of suggestions the server was asked for
         */
        private final int serverSuggestionsLimit;

        /**
         * Suggestions returned by the server in response to the request.
         */
        private final List<Suggestion> suggestions;

        /**
         * Create a new instance.
         *
         * @param request Request from the SuggestBox.
         * @param serverSuggestionsLimit The number of suggestions we asked the server for.
         * @param suggestions The suggestions returned by the server.
         */
        private ServerResponse(Request request, int serverSuggestionsLimit, List<Suggestion> suggestions) {
            this.request = request;
            this.serverSuggestionsLimit = serverSuggestionsLimit;
            this.suggestions = suggestions;
        }

        /**
         * Get the query string that was sent to the server.
         *
         * @return The query.
         */
        private String getQuery() {
            return request.getQuery();
        }

        /**
         * Does the response include all possible suggestions for the query.
         *
         * @return True or false.
         */
        private boolean isComplete() {
            return suggestions.size() <= serverSuggestionsLimit;
        }

        /**
         * Filter the suggestions we got back from the server.
         *
         * @param query The query string.
         * @param limit The number of suggestions to return.
         * @return The suggestions.
         */
        public List<Suggestion> filter(String query, int limit) {

            List<Suggestion> newSuggestions = new ArrayList<Suggestion>(limit);
            int i = 0, s = suggestions.size();
            while (i < s && !suggestions.get(i).getDisplayString().startsWith(query)) {
                ++i;
            }
            while (i < s && newSuggestions.size() < limit && suggestions.get(i).getDisplayString().startsWith(query)) {
                newSuggestions.add(suggestions.get(i));
                ++i;
            }
            return newSuggestions;
        }
    }

    /**
     * Number of suggestions to request from the server.
     * Using 75 lets you test the logic that uses the isComplete method. Try using "al" as the initial query
     */
//    private static final int numberOfServerSuggestions = 100;
    private static final int numberOfServerSuggestions = 75;

    /**
     * Is there a request in progress
     */
    private boolean requestInProgress = false;

    /**
     * The most recent request made by the client.
     */
    private Request mostRecentClientRequest = null;

    /**
     * The most recent response from the server.
     */
    private ServerResponse mostRecentServerResponse = null;

    /**
     * Create a new instance.
     */
    public ServerSuggestOracle() {
    }

    /**
     * Called by the SuggestBox to get some suggestions.
     *
     * @param request The request.
     * @param callback The callback to call with the suggestions.
     */
    public void requestSuggestions(final Request request, final Callback callback) {
        // Record this request as the most recent one.
        mostRecentClientRequest = request;
        // If there is not currently a request in progress return some suggestions. If there is a request in progress
        // suggestions will be returned when it completes.
        if (!requestInProgress) {
            returnSuggestions(callback);
        }
    }

    /**
     * Return some suggestions to the SuggestBox. At this point we know that there is no call to the server currently in
     * progress and we try to satisfy the request from the most recent results from the server before we call the server.
     *
     * @param callback The callback.
     */
    private void returnSuggestions(Callback callback) {
        // For single character queries return an empty list.
        final String mostRecentQuery = mostRecentClientRequest.getQuery();
        
        if (mostRecentQuery.length() == 1) {
            callback.onSuggestionsReady(mostRecentClientRequest,
                    new Response(Collections.<Suggestion>emptyList()));
            return;
        }
        // If we have a response from the server, and it includes all the possible suggestions for its request, and
        // that request is a superset of the request we're trying to satisfy now then use the server results, otherwise
        // ask the server for some suggestions.
        if (mostRecentServerResponse != null) {
            if (mostRecentQuery.equals(mostRecentServerResponse.getQuery())) {
                Response resp =
                        new Response(mostRecentServerResponse.filter(mostRecentClientRequest.getQuery(),
                                mostRecentClientRequest.getLimit()));
                callback.onSuggestionsReady(mostRecentClientRequest, resp);
            } else if (mostRecentServerResponse.isComplete() &&
                    mostRecentQuery.startsWith(mostRecentServerResponse.getQuery())) {
                Response resp =
                        new Response(mostRecentServerResponse.filter(mostRecentClientRequest.getQuery(),
                                mostRecentClientRequest.getLimit()));
                if( resp.getSuggestions().size() < 3 )
                	makeRequest(mostRecentClientRequest, callback);
                else
                	callback.onSuggestionsReady(mostRecentClientRequest, resp);
            } else {
                makeRequest(mostRecentClientRequest, callback);
            }
        } else {
            makeRequest(mostRecentClientRequest, callback);
        }
    }

    /**
     * Send a request to the server.
     *
     * @param request The request.
     * @param callback The callback to call when the request returns.
     */
    private void makeRequest(final Request request, final Callback callback) {
        requestInProgress = true;
        Waggle_ui.locsService.getLocations(request.getQuery(), numberOfServerSuggestions, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                requestInProgress = false;
            }
            public void onSuccess(Object result) {
                requestInProgress = false;
                List<String> r = (List<String>)result;
                List<Suggestion> matches = new ArrayList<Suggestion>(r.size());
                
                int count = 0;
                while ( count < r.size() ) {
                    //matches.add(new Suggestion(r.get(count)));
                	final String str = r.get(count);
                	matches.add(new SuggestOracle.Suggestion() {
                        public String getDisplayString() {
                                return str;
                        }
                        public String getReplacementString() {
                                return str;
                        }
                	});

                    count++;
                }
                mostRecentServerResponse = new ServerResponse(request, numberOfServerSuggestions, matches);
                ServerSuggestOracle.this.returnSuggestions(callback);
            }
        });
    }
}