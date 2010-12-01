package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Composite;

import tw.kayjean.ui.client.rpc.CoordinateSendCallback;

/**
 * The WaggleUIFrame which holds all our Panels (and thus, virtually
 * all our data). Access panels statically here, so that you don't have to
 * access the actual classes statically. You can also use the utility methods
 * here to notify users of stuff.
 */
public class WUF extends Composite{

	private static final Label messagePanel = new Label();

	public static final TabPanel tPanel = new TabPanel();
	public static final LocationPanel locPanel = new LocationPanel();
	public static final FavoritePanel favPanel = new FavoritePanel();
	public static final MapPanel mPanel = new MapPanel();
	
	//private static final Label userName = new Label( "guest" );
    public static int currentselect = 0;
    
    private VerticalPanel outer = new VerticalPanel ();
    
	/**
	 * Constructor for this DockPanel. Only used from CalMap.java
	 */
	public WUF() {
        // Build the Header
		outer.add(messagePanel);
//		outer.add(userName);

        // Building the TabPanel
        tPanel.add(locPanel, "<div id=\"tab_route\" class=\"one_tab\"><span>See POIs in Map</span></div>", true); // List of locations to plot, and a plot button
        tPanel.add(favPanel, "<div id=\"tab_route\" class=\"one_tab\"><span>See Your Favorite</span></div>", true);
        tPanel.addTabListener(new TabClickListener());
        outer.add(tPanel);
        
        //Build the MapPanel
        outer.add(mPanel);
        initWidget ( outer );
//        message("將景點拉動到合適位置.");

//因為沒有辦法在一開始就知道有沒有登入,所以這邊要先關閉
//登入狀況等到main後段,執行差不多時再檢查登入情形
//        RootPanel.get("fb-root").add( new FrontpageViewController () );
	}
    

    public static void chooseTab(int index) { tPanel.selectTab(index); }

	/**
	 * Shows a given string in the "message area"
	 * @param s Given String
	 */
	public static void message(String s) {
        messagePanel.setText(s);
	}

//	public static String username() {
//		return "Kay Jean";
//		return userName.getText();
//	}

//	public static void setusername( String s ) {
//		userName.setText(s);
//	}

	
//	/** Start and stop the waiting bar... **/
//    public static native void startSpin() /*-{
//    $doc.getElementById('spinner').style.visibility = 'visible';
//  }-*/;
//    public static native void stopSpin() /*-{
//    $doc.getElementById('spinner').style.visibility = 'hidden';
//  }-*/;
    
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

/*	
    public static void favoriteLocation( final LocationEntry loc ){
    	// type == 0 , move to Favorite
    	//if( type == 0 ){
    		//畫在畫面上
    		favPanel.addFavorite( loc.n );
    		//資料送入cache中server
    		DataSwitch.get().sendNode( Waggle_ui.username.id , 1 , loc.n , new CoordinateSendCallback() );
    		locPanel.removeLocation( loc );
    	//}
    }
*/    
    //使用者不一定要使用fb-root這個區塊啦
    //只是在html部分名子不能改變
    
    //測試內容
/*
    public static void test2( Composite example ){
    	RootPanel mainView = RootPanel.get("login");
    	mainView.clear();
    	mainView.add( example );

        Example e = null;
//        if ( "stream.publish".equals ( example ) ) {
//            e = new StreamPublishExample ( Waggle_ui.fbCore );
//        } else if ( "friends".equals ( example ) ) {
            e = new FriendsExample ( Waggle_ui.fbCore );
//        }
    	
        VerticalPanel examplePanel = new VerticalPanel ();
        examplePanel.setWidth ( "700px" );
        examplePanel.getElement().setId("ExampleView");
        HorizontalPanel headerPanel = new HorizontalPanel ();
        headerPanel.addStyleName( "header" );
        headerPanel.add ( new HTML ( "Method: " + e.getMethod() ) );
        Anchor sourceLink = new Anchor ( "Source" );
        sourceLink.addStyleName("sourceLink");
        sourceLink.setTarget( "blank");
        sourceLink.setHref("http://code.google.com/p/gwtfb/source/browse/trunk/GwtFB/src/com/gwtfb/client/examples/" + e.getSimpleName() + ".java" );
         headerPanel.add ( sourceLink ); 
        examplePanel.add( headerPanel );
        examplePanel.addStyleName ( "example" );
        e.addStyleName( "example" );
        examplePanel.add ( e );
        // Add example
        mainView.add( examplePanel );
    }
*/    
}