package tw.kayjean.ui.client;

import java.util.HashMap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.user.client.ui.AbsolutePanel;  
import com.google.gwt.user.client.ui.FlexTable;  
import com.google.gwt.user.client.ui.SimplePanel;  
import com.google.gwt.user.client.ui.Widget;    

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;  
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.allen_sauer.gwt.dnd.client.DragContext;

//import tw.kayjean.ui.client.examples.FriendsExample.FacebookCallback;
//import tw.kayjean.ui.client.examples.FriendsExample.Ui;
import tw.kayjean.ui.client.rpc.CoordinateSendCallback;
import tw.kayjean.ui.sdk.FBCore;
import tw.kayjean.ui.sdk.FBXfbml;

//facebook專用
public class UserInfoViewController extends Composite {
	
    private HTML welcomeHtml = new HTML ();
	private VerticalPanel outer = new VerticalPanel ();
	//private Anchor streamPublishLink = new Anchor ( "Test Stream Publish" );
	//private Anchor streamShareLink = new Anchor ( "Test Stream Share" );
	
	//目前網站位置是 http://www.facebook.com/apps/application.php?id=102921393079418
	//原本網站資料是 http://www.facebook.com/apps/application.php?id=37309251911#!/apps/application.php?id=37309251911&v=wall
	//兩個都看不到POST內容

	public static Label messagePanel = new Label();
	public static TabPanel tPanel = new TabPanel();
	public static LocationPanel locPanel = new LocationPanel();
	public static FavoritePanel favPanel = new FavoritePanel();
	public static MapPanel mPanel;

	private static final Label userName = new Label( "guest" );
	public static int currentselect = 0;
	
	//friend有關
	AbsolutePanel boundaryPanel = new AbsolutePanel();
    /*
     * Decide what to render in response
     */
    enum Ui { INPUT, JSON, FEED };
    private static final int COLUMNS = 4;
    private static final int ROWS = 4;
    private PickupDragController dragController; 
    private static final int IMAGE_HEIGHT = 58;
    private static final int IMAGE_WIDTH = 65;

    
    
    
    
    
	private FBCore fbCore;
	
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
		//streamPublishLink.addClickHandler( new PublishHandler () );
		//outer.add ( streamPublishLink );
		
		//這個項目是實驗用,還沒有使用
		/*
		 * Stream Share
		 */
		class ShareHandler implements ClickHandler {
		    public void onClick(ClickEvent event) {
		        testShare ();
		    }
		}
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
/*		
		fbCore.api ( "/me/friends", new FacebookCallback ( "/me/friends", Ui.INPUT, null ) );
	    boundaryPanel.setPixelSize(500, 300);
	    setWidget(boundaryPanel);
	    // initialize our flex table
	    FlexTable flexTable = new FlexTable();
//	    flexTable.setStyleName(CSS_DEMO_PUZZLE_TABLE);
	    boundaryPanel.add(flexTable, 50, 20);  
	    // initialize our drag controller
	    dragController = new PickupDragController(boundaryPanel, false);
	    dragController.addDragHandler( new LocationDragHandler() );
	    dragController.setBehaviorMultipleSelection(false);
	    // create our grid      
	    for (int i = 0; i < COLUMNS; i++) {
	    	for (int j = 0; j < ROWS; j++) {
	    		// create a simple panel drop target for the current cell
	    		SimplePanel simplePanel = new SimplePanel();
	            simplePanel.setPixelSize(IMAGE_WIDTH, IMAGE_HEIGHT);
	            flexTable.setWidget(i, j, simplePanel);
//	    flexTable.getCellFormatter().setStyleName(i, j, CSS_DEMO_PUZZLE_CELL);            
	    // place a pumpkin in each panel in the cells in the first column          
//	    if (j == 0)
//	    {
//	                simplePanel.setWidget(createDraggable());          
//	    }

	            // instantiate a drop controller of the panel in the current cell
	            SetWidgetDropController dropController = new SetWidgetDropController(simplePanel);
	            dragController.registerDropController(dropController);
	    	}
	    }  
		
	    outer.add(boundaryPanel);
	    
		outer.add(messagePanel);
		outer.add(userName);
        tPanel.add(locPanel, "<div id=\"tab_route\" class=\"one_tab\"><span>See POIs in Map</span></div>", true); // List of locations to plot, and a plot button
        tPanel.add(favPanel, "<div id=\"tab_route\" class=\"one_tab\"><span>See Your Favorite</span></div>", true);
        tPanel.addTabListener(new TabClickListener());
        outer.add(tPanel);
		mPanel = new MapPanel();
        outer.add(mPanel);
        message("將景點拉動到合適位置.");
*/	
		initWidget ( outer );
		
	}
	
    private class LocationDragHandler implements DragHandler {
        public void onDragEnd(DragEndEvent event) {
//            signalMaptoDrawRoute();
        }
        public void onDragStart(DragStartEvent event) {}
        public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {}
        public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {}
    }
	
    private class SetWidgetDropController extends SimpleDropController {
    	private final SimplePanel dropTarget;
    	public SetWidgetDropController(SimplePanel dropTarget) {
    		super(dropTarget);
    		this.dropTarget = dropTarget;
    	}
    	@Override
    	public void onDrop(DragContext context) {      
    		dropTarget.setWidget(context.draggable);      
    		super.onDrop(context);    
    	}      
    	@Override    
    	public void onPreviewDrop(DragContext context) throws VetoDragException {      
    		if (dropTarget.getWidget() != null) 
    		{        
    			throw new VetoDragException();      
    		}      
    		super.onPreviewDrop(context);    
    	}  
    }

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
/*            
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
            if ( ui == Ui.INPUT ) {
                renderSuggestBox ( response );
            }
*/            
        }
    }
	
	
	/**
	 * Render information about logged in user
	 */
	private void renderMe ( JavaScriptObject response ) {
		JSOModel jso = response.cast();
		welcomeHtml.setHTML ( "<h3> Hi,  " + jso.get ( "name" ) + "</h3> albumupload is a simple GWT Facebook Graph Client. "  );
		setusername( jso.get ( "name" ) );
		
		HTML json = new HTML ( new JSONObject ( response ).toString() );
		json.addStyleName("jsonOutput");
		outer.add ( json );
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
	    actionLink.put ( "text", new JSONString ( "Code" ) );
	    actionLink.put ( "href", new JSONString ( "http://www.albumupload.com" ) );

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

    public void chooseTab(int index) { tPanel.selectTab(index); }

	/**
	 * Shows a given string in the "message area"
	 * @param s Given String
	 */
	public void message(String s) {
        messagePanel.setText(s);
	}

	public static String username() {
//		return "Kay Jean";
		return userName.getText();
	}

	public static void setusername( String s ) {
		userName.setText(s);
	}

	private class TabClickListener implements TabListener {
		public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
			return true;
		}
		public void onTabSelected(SourcesTabEvents sender, int tabIndex){
			currentselect = tabIndex;
			//清除所有畫面上的點
			if( currentselect == 0 )
				locPanel.close();
			else if( currentselect == 1 )
				favPanel.close();
			mPanel.Clear();
			//啟動map更新機制
			mPanel.rerefesh();
		}
	}

    public static void favoriteLocation( final LocationEntry loc ){
    	// type == 0 , move to Favorite
    	//if( type == 0 ){
    		//畫在畫面上
    		favPanel.addFavorite( loc.n );
    		//資料送入cache中server
    		DataSwitch.get().sendNode(username() , 1 , loc.n , new CoordinateSendCallback() );
    		locPanel.removeLocation( loc );
    	//}
    }


}
