package tw.kayjean.ui.client;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import tw.kayjean.ui.sdk.FBCore;
import tw.kayjean.ui.sdk.FBXfbml;
import tw.kayjean.ui.sdk.objects.Paging;
import tw.kayjean.ui.sdk.objects.Post;

import tw.kayjean.ui.client.model.FBDetail;
import tw.kayjean.ui.client.model.FBFriend;
import tw.kayjean.ui.client.model.FBFriends;
import tw.kayjean.ui.client.model.Poi;

//facebook專用
public class UserInfoViewController extends Composite {
	
    private HTML welcomeHtml = new HTML ();
	private VerticalPanel outer = new VerticalPanel ();
	
	//目前網站位置是 http://www.facebook.com/apps/application.php?id=102921393079418
	//原本網站資料是 http://www.facebook.com/apps/application.php?id=37309251911#!/apps/application.php?id=37309251911&v=wall
	//POST版 內容會 時有時無??

	private FBCore fbCore;

	
	//post
	private Anchor streamPublishLink = new Anchor ( "Test Stream Publish" );
	
	
	//friend
	private HashMap<String,String> suggestionWorkaround = new HashMap<String,String> ();
    /*
     * Decide what to render in response
     */
    enum Ui { INPUT, JSON, FEED };
    private VerticalPanel content = new VerticalPanel ();
    
    
    
	/**
	 * New View
	 */
	public UserInfoViewController ( final FBCore fbCore ) {
		
	    this.fbCore = fbCore;

	    outer.add ( welcomeHtml );
		outer.add ( new HTML ( "<p/>" ) );
		outer.add ( new HTML ( "<hr/><fb:comments xid='albumupload' />" ) );
		
		//這個項目是實驗用,還沒有使用
		/*
		 * Stream Publish 
		 */
		class PublishHandler implements ClickHandler {
            public void onClick(ClickEvent event) {
                testPublish ();
            }
		}
		streamPublishLink.addClickHandler( new PublishHandler () );
		outer.add ( streamPublishLink );
		
		//這個項目是實驗用,還沒有使用
		/*
		 * Stream Share
		 */
		//class ShareHandler implements ClickHandler {
		//    public void onClick(ClickEvent event) {
		//        testShare ();
		//    }
		//}
		//streamShareLink.addClickHandler( new ShareHandler () );
		//outer.add ( streamShareLink );
		
		/*
		 * Display User info
		 */
		class MeCallback extends Callback<JavaScriptObject> {
			public void onSuccess ( JavaScriptObject response ) {
				renderMe ( response );
			}
		}
		fbCore.api ( "/me" , new MeCallback () );

		/*
		 * Display number of posts
		 */
		class PostsCallback extends Callback<JavaScriptObject> {
			public void onSuccess ( JavaScriptObject response ) {
				JSOModel model = response.cast ();
				JsArray array = model.getArray("data");
				outer.add ( new HTML ( "Posts " + array.length() ) );
			}
		}
		fbCore.api ( "/f8/posts",  new PostsCallback () );
		//這個只是顯示,目前使用者已經POST出去數目
		
		fbCore.api ( "/me/friends", new FacebookCallback ( "/me/friends", Ui.INPUT, null ) );
		
		outer.add ( content );
		initWidget ( outer );
	}
	
	/**
	 * Render information about logged in user
	 */
	private void renderMe ( JavaScriptObject response ) {
		JSOModel jso = response.cast();
		welcomeHtml.setHTML ( "<h3> Hi,  " + jso.get ( "name" ) + "</h3> albumupload is a simple GWT Facebook Graph Client. "  );
		//setusername( jso.get ( "name" ) );
		
		FBDetail fd = new FBDetail();
		fd.id = jso.get ( "id" );
		fd.name = jso.get ( "name" );
		fd.first_name = jso.get ( "first_name" );
		fd.last_name = jso.get ( "last_name" );
		fd.link = jso.get ( "link" );
		fd.birthday = jso.get ( "birthday" );
		fd.bio = jso.get ( "bio" );
		fd.gender = jso.get ( "gender" );
		fd.timezone = jso.get ( "timezone" );
		fd.locale = jso.get ( "locale" );
		fd.updated_time = jso.get ( "updated_time" );
		Waggle_ui.username = fd;
		
		HTML json = new HTML ( new JSONObject ( response ).toString() );
		json.addStyleName("jsonOutput");
		outer.add ( json );
		
		WUF.mPanel.rerefesh();
	}
	
	/**
	 * Render publish
	 */
	public void testPublish () {
	    JSONObject data = new JSONObject ();
	    data.put( "method", new JSONString ( "stream.publish" ) );
	    data.put( "message", new JSONString ( "Getting education about Facebook Connect and albumupload" ) );
	    JSONObject attachment = new JSONObject ();
	    attachment.put( "name", new JSONString ( "albumupload" ) );
	    attachment.put("caption", new JSONString ( "The Facebook Connect Javascript SDK and GWT" ) );
	    attachment.put( "description", new JSONString ( "A small GWT library that allows you to interact with Facebook Javascript SDK in GWT ") ); 
	    attachment.put("href",  new JSONString ( "http://www.albumupload.com" ) );
	    data.put( "attachment", attachment );
	    JSONObject actionLink = new JSONObject ();
	    actionLink.put ( "text", new JSONString ( "nctu" ) );
	    actionLink.put ( "href", new JSONString ( "http://www.nctu.edu.tw" ) );
	    JSONArray actionLinks = new JSONArray ();
	    actionLinks.set(0, actionLink);
	    data.put( "action_links", actionLinks);
	    data.put( "user_message_prompt", new JSONString ( "Share your thoughts about Connect and GWT" ) );
	    fbCore.ui(data.getJavaScriptObject(), new Callback () );
	}
	
	/**
	 * Render share
	 */
	public void testShare () {
	    JSONObject data = new JSONObject ();
	    data.put( "method", new JSONString ( "stream.share" ) );
	    data.put( "u", new JSONString ( "http://www.albumupload.com" ) );
	    fbCore.ui ( data.getJavaScriptObject(), new Callback () );
	}
	
	//friend
    /*
     * Generic callback class
     */
    class FacebookCallback extends Callback<JavaScriptObject> {
        
   
        private String path;
        private Ui ui;
        private VerticalPanel result;
        
        public FacebookCallback ( String path, Ui ui, VerticalPanel result ) {
            this.path = path;
            this.ui = ui;
            this.result = result;
        }

        public void onSuccess ( JavaScriptObject response ) {
            
            switch ( ui )
            {
                case INPUT :
                    renderSuggestBox ( response );
                    break;
                    
                case JSON :
                    result.add( new HTML ( new JSONObject ( response ).toString() ) );
                    break;
                    
                case FEED : 
                    renderFeed ( response, result );
                    break;
                
            }
//            if ( ui == Ui.INPUT ) {
//                renderSuggestBox ( response );
//            }
        }
    }

    /*
     * Render suggesbox to let user choose a friend
     */
    private void renderSuggestBox ( JavaScriptObject response ) {
        
//        suggestPanel.clear();
        
        JSOModel jso = response.cast ();
        if ( jso.hasKey ( "error" ) ) {
            handleError ( response );
            return;
        }
        
        JsArray array = jso.getArray("data");
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle ();
        
		FBFriends ffs = new FBFriends();
		ffs.items = new ArrayList();
        for ( int i = 0 ; i < array.length(); i++ ) {
            JSOModel j = array.get(i).cast();

            String name = j.get("name");
            String id = j.get("id");
            oracle.add(name);
            suggestionWorkaround.put ( name, id);
            
            FBFriend ff = new FBFriend();
            ff.id = j.get("id");
            ff.name = j.get("name");
            ffs.items.add(ff);
        }
		Waggle_ui.friends = ffs;
        
        HorizontalPanel panel = new HorizontalPanel ();
        panel.getElement().setAttribute("style", "padding: 10px; border: 1px solid #cccccc" );
        panel.add ( new HTML ( "Type friends name and hit return to see available methods: " ) );
       
        SuggestBox box = new SuggestBox (oracle);
        box.addSelectionHandler( new SelectionHandler<Suggestion> () {
            public void onSelection(SelectionEvent<Suggestion> event) {
                clear ();
                displaySelectedName ( event.getSelectedItem().getDisplayString() );
                doGetFriendData ( new Long ( suggestionWorkaround.get ( event.getSelectedItem().getReplacementString() ) ) );
               FBXfbml.parse();
            }
        });
        
        panel.add ( box);

        
		HTML json = new HTML ( new JSONObject ( response ).toString() );
		json.addStyleName("jsonOutput");
        
        outer.add (panel);
        outer.add (json);
        
        //將資料送到server進行更新
    	Waggle_ui.coordService.sendDetail( Waggle_ui.username , Waggle_ui.friends , new AsyncCallback() {

    		public void onFailure(final Throwable caught) {
    			GWT.log("Error in CoordinateService!", caught);
    			caught.printStackTrace();
    			//        doThisBeforeReturn(true);
    		}

    		public void onSuccess(final Object result ) {
/*先不處理
    			Poi poidata = (Poi) result;
    			if (poidata == null) {
    				GWT.log("CoordinateRTreeCallback got a null coordinate!", null);
    				//MainFrame.message( "找不到符合地點" );
    				//doThisBeforeReturn(false);
    				return;
    			}
*/    			
    		}
      }
		);
        
        
    }

    private void handleError ( JavaScriptObject response )
    {
        Window.alert ( "Handle error ");
    }

    /**
     * Clear previous data
     */
    private void clear () {
        content.clear();
    }
    
    private void displaySelectedName ( String name ) {
        
        content.add ( new HTML ( "<h1>" + name + "</h1>" ) );
    }

    /**
     * Loop all methods that is accessible to users.
     */
    private void doGetFriendData ( Long id ) {

        renderMethod ( id, "feed", Ui.FEED );
        
        
        String [] methods = { "albums", "friends", "home", "likes", "movies", "books", 
                              "notes", "photos", "videos", "events", "groups" }; 
        
        for ( String method : methods ) {
            renderMethod(id, method, Ui.JSON );
        }
    }

    private void renderMethod(final Long userId, String method, final Ui render) {
        String fields = null;
        
        if ( method.split(":").length == 2 ) {
            fields = method.split(":")[1];
            method = method.split(":")[0];
        }
        final String fieldsInner = fields;
        final String m = "/" + userId + "/" + method;
        
        HTML header = new HTML ( "<div class='smallheader'>/" + method + "</div>");
        // Add link
        final Anchor anchor = new Anchor ( "Click to see result" + (render == Ui.JSON ? " (json)" : "" ) );
        content.add( header );
        content.add ( anchor );
        content.add ( new HTML ( "<p/>" ) );
        
        // Where to put the result from the method
        final VerticalPanel result = new VerticalPanel ();
        content.add ( result );
        
        anchor.addClickHandler(new ClickHandler () {
            public void onClick(ClickEvent event) {
                if ( fieldsInner != null ) {
                    // Add fields parameter
                    JSONObject filter = new JSONObject ();
                    filter.put("fields",new JSONString ( fieldsInner) );
                    fbCore.api (m , filter.getJavaScriptObject(),   new FacebookCallback ( m , render, result ) );
                } else {
                    fbCore.api (m ,  new FacebookCallback ( m , render, result ) );
                }
            }
        });
    }
    
    /**
     * Render user posts
     */
    public void renderFeed ( JavaScriptObject response, VerticalPanel resultPanel ) {
        try {
            DataObject dataObject = response.cast();
            JsArray<Post> posts = dataObject.getData().cast();
            
            for ( int i = 0; i < posts.length(); i++ ) {
                DecoratorPanel dp = new DecoratorPanel ();
                dp.addStyleName("post");
                dp.add(posts.get(i).toHTML());
                resultPanel.add (dp);
            }
            
            HorizontalPanel pagingPanel = new HorizontalPanel();
            pagingPanel.setSpacing(10);
            
            Paging paging = dataObject.getObject("paging").cast();
            Anchor previousLink = new Anchor ( "<< Previous" );
            previousLink.setHref(paging.getPrevious());
            
            Anchor nextLink = new Anchor ( "Next >>" );
            nextLink.setHref(paging.getNext());
            
            previousLink.setTarget("blank");
            nextLink.setTarget("blank");
            
            pagingPanel.add(previousLink);
            pagingPanel.add(nextLink);
            
            resultPanel.add(pagingPanel);
            
        } catch ( Exception e ) {
            Window.alert ( "Could not render response: " + e.getMessage() );
        }
    }
    
}
