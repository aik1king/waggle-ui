package tw.kayjean.ui.client;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.rpc.AsyncCallback;

//繼續要思考如何讓產生出來的項目可以分成左右兩邊,未來才能拿到需要的內容
//另外一個部份是,查詢內容和顯示內容必須不同,可以參考原本的描述
//http://google-web-toolkit.googlecode.com/svn/javadoc/1.4/com/google/gwt/user/client/ui/SuggestOracle.Suggestion.html

/**
 * A SuggestOracle that uses a server side source of suggestions. Instances of this class can not
 * be shared between SuggestBox instances.
 * 從http://development.lombardi.com/?p=45拷貝出來
 * 網路上http://www.google.com/codesearch/p?hl=zh-TW#qX-qYcAvBaw/trunk/gss/src/gr/ebs/gss/client/ServerSuggestOracle.java&q=%22ServerSuggestOracle%20extends%20SuggestOracle%22&sa=N&cd=2&ct=rc有類似內容
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

//        	return suggestions;
/*        	
        	//因為每一個結果都符合規範,這裡不太需要
        	//相對的,未來這裡可以把資料變成另外形式,例如名稱符合,特性符合
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
*/
            List<Suggestion> newSuggestions = new ArrayList<Suggestion>(limit);
            int i = 0, s = suggestions.size();
            while (i < s && newSuggestions.size() < limit ){
            	if( suggestions.get(i).getReplacementString().indexOf(query) >= 0 )
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
//數值還是太高,繼續縮小    private static final int numberOfServerSuggestions = 75;
    private static final int numberOfServerSuggestions = 25;

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

/*        為了讓輸入一個字的時候也能夠顯示內容,打開這個限制
        if (mostRecentQuery.length() == 1) {
            callback.onSuggestionsReady(mostRecentClientRequest,
                    new Response(Collections.<Suggestion>emptyList()));
            return;
        }
*/        
        // If we have a response from the server, and it includes all the possible suggestions for its request, and
        // that request is a superset of the request we're trying to satisfy now then use the server results, otherwise
        // ask the server for some suggestions.
        if (mostRecentServerResponse != null) {
            if (mostRecentQuery.equals(mostRecentServerResponse.getQuery())) {
            	//如果完全相同
                Response resp =
                        new Response(mostRecentServerResponse.filter(mostRecentClientRequest.getQuery(),mostRecentClientRequest.getLimit()));
                callback.onSuggestionsReady(mostRecentClientRequest, resp);
            } else if (mostRecentServerResponse.isComplete() &&
                    mostRecentQuery.startsWith(mostRecentServerResponse.getQuery())) {
                Response resp =
                        new Response(mostRecentServerResponse.filter(mostRecentClientRequest.getQuery(),mostRecentClientRequest.getLimit()));
                
                //中間有做過一些處理,修改成原始程式內容
                //if( resp.getSuggestions().size() < 3 )
                //	makeRequest(mostRecentClientRequest, callback);
                //else
                //	callback.onSuggestionsReady(mostRecentClientRequest, resp);
                
                //callback.onSuggestionsReady(mostRecentClientRequest, resp);

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

        Waggle_ui.coordService.getLocations(request.getQuery(), numberOfServerSuggestions, new AsyncCallback<List>() {
                  public void onFailure(Throwable caught) {
                	  requestInProgress = false;
                  }

                  public void onSuccess(List result) {

                      requestInProgress = false;

                      
/*另外一種寫法                	  
                      List<Suggestion> res = new ArrayList();
                      List<UserDTO> users = (List<UserDTO>)result;
                      for(UserDTO u : users)
                              res.add(new UserSuggestion(u));
                      mostRecentServerResponse = new ServerResponse(request, numberOfServerSuggestions, res);
                      ServerSuggestOracle.this.returnSuggestions(callback);
*/
                	  
                	  
                      List<String> r = (List<String>)result;
                      List<Suggestion> matches = new ArrayList<Suggestion>(r.size());
                      
                      int count = 0;
                      while ( count < r.size() ) {
                          //matches.add(new Suggestion(r.get(count)));
                    	  
                    	  //resultdata.add( e.gettitle() + "\t" + e.getu() + "\t" + e.getnongeo() + "\t" + e.getranking() );
                      	final String str = r.get(count);
                      	matches.add(new SuggestOracle.Suggestion() {
                      		//這是呈現在畫面上
                              public String getDisplayString() {
                                      return str.split("\t")[0];
                              }
                              //這是點下去會取出資料
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