package tw.kayjean.ui.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Composite;


import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.rpc.CoordinateSendCallback;
import tw.kayjean.ui.client.examples.Example;
import tw.kayjean.ui.client.examples.FriendsExample;
import tw.kayjean.ui.client.examples.StreamPublishExample;


/**
 * The WaggleUIFrame which holds all our Panels (and thus, virtually
 * all our data). Access panels statically here, so that you don't have to
 * access the actual classes statically. You can also use the utility methods
 * here to notify users of stuff.
 * @author Simon
 */
public class WUF extends DockPanel implements WindowResizeListener {

	
	// The various Panels you see in CalMap, and the only things accessible statically
	public static final InputPanel inPanel = new InputPanel("Type a location here");
	// Private elements used for notification purposes
	private static final Label messagePanel = new Label();

	public static final TabPanel tPanel = new TabPanel();
	public static final LocationPanel locPanel = new LocationPanel();
	public static final FavoritePanel favPanel = new FavoritePanel();
	public static final ErrorPanel errPanel = new ErrorPanel();
	public static final WhiichPanel whiichPanel = new WhiichPanel();
	
	//delete public static final OptionsPanel oPanel = new OptionsPanel();
	private static final SimplePanel flashPanel = new SimplePanel();
	//delete public static final POIPanel poiPanel = new POIPanel();
	//delete protected static final MapModPanel mModPanel = new MapModPanel();

	public static final MapPanel mPanel = new MapPanel();
	
	
	private static final Label userName = new Label( "guest" );
	
	/**
	 * Set to true whenever we are about to make a Coordinate, Routing, or POI Service call.
	 * Set to false right before the service Callback returns.
	 * This flag helps ensure that we don't have concurrent servlet calls 
	 * (specifically when clicking rapidly on the map).
	 * This boolean was previously located in MapPanel.
	 */
    public static boolean isMakingServerCall = false;
    
    public static int currentselect = 0;

	/**
	 * Constructor for this DockPanel. Only used from CalMap.java
	 */
	public WUF() { Window.addWindowResizeListener(this); }
    
    /**
     * Builds the UI, one piece at a time.
     */
    public void initialize() {
        // Build the Header
        RootPanel header = RootPanel.get("header");
        
        HTML print = new HTML("<a>Print</a>");
        print.addClickListener(new PrintClickListener());
        print.setTitle("Switch to campus view and print the current map");
        
        HTML email = new HTML("<a>E-mail</a>");
        email.addClickListener(new EmailClickListener());
        email.setTitle("E-mail the current route to a friend");
        
        header.add(email);
        header.add(print);
        
        // Build the InputPanel (user input textbox)
        RootPanel.get("inputPanel").add(inPanel);
        // Build the messagePanel
        RootPanel.get("messagePanel").add(messagePanel);
        RootPanel.get("usernamePanel").add(userName);

        // Building the TabPanel
        tPanel.add(locPanel, "<div id=\"tab_route\" class=\"one_tab\"><span>See POIs in Map</span></div>", true); // List of locations to plot, and a plot button
        tPanel.add(favPanel, "<div id=\"tab_route\" class=\"one_tab\"><span>See Your Favorite</span></div>", true);
        tPanel.add(errPanel, "<div id=\"tab_route\" class=\"one_tab\"><span>Error Report</span></div>", true);
        tPanel.add(whiichPanel, "<div id=\"tab_route\" class=\"one_tab\"><span>Whiich Report</span></div>", true);
//delete        tPanel.add(oPanel, "<div id=\"tab_options\" class=\"one_tab\"><span>Set Advanced Options</span></div>", true);
//        tPanel.add(flashPanel, "<div id=\"tab_info\" class=\"one_tab\"><span>Show Pic and Information</span></div>", true);
//delete        tPanel.add(poiPanel, "<div id=\"tab_poi\" class=\"one_tab\"><span>Points of Interest</span></div>", true);
//delete        tPanel.add(mModPanel, "<div id=\"tab_mapmod\" class=\"one_tab\"><span>Map Modification</span></div>", true);
        tPanel.add(new HTML(),"<img id=\"spinner\" src=\"images/spinner.gif\" alt=\"Please wait...\"/>", true);
        tPanel.addTabListener(new TabClickListener());
        RootPanel.get("tabPanel").add(tPanel);
        
        // Build the MapPanel
        RootPanel.get("mapPanel").add(mPanel);
        
        // Initialize and create a HashMap mapping loc name strings to flashobject url strings
//delete        FlashObject.createMap();

        // Official welcome message.
        //�n������w�@�Ӧ�m��ܱ���
//delete        setSWF("Intro");
        message("To get started, begin typing the name of a location in the box to the left.");


//因為沒有辦法在一開始就知道有沒有登入,所以這邊要先關閉
//登入狀況等到main後段,執行差不多時再檢查登入情形
//        RootPanel.get("fb-root").add( new FrontpageViewController () );
    }

	/**
	 * Selects the photo & details tab
	 */
    //��2�אּ1
	public static void selectInfoTab() { tPanel.selectTab(1); }

	/**
	 * Selects the routelist tab
	 */
	public static void selectRouteTab() { tPanel.selectTab(0); }
    
    public static void chooseTab(int index) { tPanel.selectTab(index); }

	private static void setSWF(String name, boolean isOurOwn) {
//delete		flashPanel.clear();
//delete		FlashObject swf = new FlashObject(name,300,420,isOurOwn);
//delete		flashPanel.add(swf);
		WUF.selectInfoTab();
	}

	/**
	 * Use this version of setSWF
	 * 
	 * @param name
	 */
	public static void setSWF(String name) {
		if (name.equals("Intro"))
			setSWF(name, true);
		else
			setSWF(name, false);
	}

	/**
	 * Shows a given string in the "message area"
	 * @param s Given String
	 */
	public static void message(String s) {
        messagePanel.setText(s);
		//Effects.Effect("Highlight", messagePanel);
	}

	public static String username() {
		return "Kay Jean";
//		return userName.getText();
	}

	public static void setusername( String s ) {
		userName.setText(s);
	}

	/** Start and stop the waiting bar... **/
    public static native void startSpin() /*-{
    $doc.getElementById('spinner').style.visibility = 'visible';
  }-*/;
    public static native void stopSpin() /*-{
    $doc.getElementById('spinner').style.visibility = 'hidden';
  }-*/;
    
	/**
	 * Control when the map is resized.
	 */
	public void onWindowResized(int width, int height) {
//delete		WUF.mPanel.jsniResize();
	}

	/**
	 * Use this method in place of clearOverlays()
	 */
	public static void clearMap() {
//delete		WUF.mPanel.theMap.clearOverlays();
//delete		WUF.poiPanel.uncheckPOIBoxes();
//delete		WUF.mModPanel.exitMapModMode();
	}
	
	/**
	 * TabListener that invalidates clicks on the farthest right tab (spinner).
	 */
/*	
	private class TabClickListener implements TabListener {

		public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
			if (tabIndex == tPanel.getTabBar().getTabCount() - 1)
				return false;
			return true;
		}

		public void onTabSelected(SourcesTabEvents sender, int tabIndex) {}

	}
*/
	private class TabClickListener implements TabListener {

		public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
//			if (tabIndex == tPanel.getTabBar().getTabCount() - 1)
//				return false;
			return true;
		}

//        public void onTabSelected(SourcesTabEvents sender, int tabIndex) {}

		public void onTabSelected(SourcesTabEvents sender, int tabIndex){
			currentselect = tabIndex;
			//清除所有畫面上的點
			if( currentselect == 0 )
				locPanel.closeRouteBar();
			else if( currentselect == 1 )
				favPanel.closeRouteBar();
			else if( currentselect == 2 )
				errPanel.closeRouteBar();
			else if( currentselect == 3 )
				whiichPanel.closeRouteBar();
				
			mPanel.Clear();
			//啟動map更新機制
			mPanel.rerefesh();
		}

	}
	
	
	/**
	 * Click Listener for printing functionality
	 */
	private class PrintClickListener implements ClickListener {
		public void onClick(Widget sender) {
            // Switch to the best printable MapType
//delete            mPanel.changeToCustomMode();
			print();
		}
	}
    
    /**
     *  Printing method
     */
    public static native void print() /*-{
    $wnd.print();
}-*/;

    public static void favoriteLocation( int type , final LocationEntry loc ){
    	// type == 0 , move to Favorite
    	if( type == 0 ){
    		//畫在畫面上
    		favPanel.addFavorite( loc.n );
    		//資料送入cache中server
    		DataSwitch.get().sendNode(username() , 1 , loc.n , new CoordinateSendCallback() );
    		locPanel.removeLocation( loc );
    	}
    }
    
    public static void errorLocation( int type , final LocationEntry loc ){
    	// type == 0 , move to Favorite
    	if( type == 0 ){
    		//移動TAB
    		errPanel.addError( loc.n );
    		//資料送入cache中server
    		DataSwitch.get().sendNode( username() , 2 , loc.n, new CoordinateSendCallback() );
    		locPanel.removeLocation( loc );
    	}
    }

    public static void whiichLocation( int type , final LocationEntry loc ){
    	// type == 0 , move to Favorite
    	if( type == 0 ){
    		//移動TAB
    		whiichPanel.addWhiich( loc.n );
    		//資料送入cache中server
    		DataSwitch.get().sendNode( username() , 3 , loc.n, new CoordinateSendCallback() );
    		locPanel.removeLocation( loc );
    	}
    }
    
	/**
	 * Click Listener for email functionality
	 */
	private class EmailClickListener implements ClickListener {

		public void onClick(Widget sender) {
			String url = genURL();
			redirectEmailClient(url);
		}
		
		private String genURL() {
			String growingURL = "http://www.kayjean.idv.tw/XXX.html";
			
			List loclist = WUF.locPanel.routeFlow.generateLocStringList();
			
			if (loclist.size() < 1) {
				return (growingURL);
			} else {
				growingURL += "%3F"; //"?";
			}

			for (Iterator i = loclist.iterator(); i.hasNext(); ) {				
				growingURL += "loc=" + i.next();
				if (i.hasNext()) {
					growingURL += "%26"; //"\046"; //"&";
				}
			}
			
			growingURL = growingURL.replaceAll(" ", "_");
			return (growingURL);
		}
    }

    private native void redirectEmailClient(String url) /*-{
        var messageBody = "mailto:?subject=CalMap Route&body=Someone has sent you a link from CalMap - the UC Berkeley Mapping Solution. Check it out at: ";
        if (confirm("Would you like to launch your e-mail client? If not, you'll be provided with a permanent link.")) {
            $wnd.location = messageBody + escape(url);
        } else {
            $wnd.alert("If your browser supports it, this URL will be copied to your clipboard: " + url);
            $wnd.clipboardData.setData("Text", url);
        }
  }-*/;

    
    
    //使用者不一定要使用fb-root這個區塊啦
    //只是在html部分名子不能改變
    
    
    //測試內容

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
   
}