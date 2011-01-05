package tw.kayjean.ui.client;

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
	public static final ReferencePanel refPanel = new ReferencePanel();
	public static final MapPanel mPanel = new MapPanel();
	
	//private static final Label userName = new Label( "guest" );
    public static int currentselect = 0;
    
    private HorizontalPanel outer = new HorizontalPanel ();
    
	/**
	 * Constructor for this DockPanel. Only used from CalMap.java
	 */
	public WUF() {
        // Build the Header
		outer.add(messagePanel);
//		outer.add(userName);

        // Building the TabPanel
		// List of locations to plot, and a plot button
        tPanel.add(locPanel, "<div id=\"tab_dispatch\" class=\"one_dispatch\"><span>分送景點</span></div>", true); 
        tPanel.add(favPanel, "<div id=\"tab_mine\" class=\"one_mine\"><span>專長</span></div>", true);
        tPanel.add(refPanel, "<div id=\"tab_mine\" class=\"one_mine\"><span>地圖</span></div>", true);
        tPanel.addTabListener(new TabClickListener());
        outer.add(tPanel);
        
        //Build the MapPanel
        outer.add(mPanel);
        initWidget ( outer );
	}

    public static void chooseTab(int index) { tPanel.selectTab(index); }

	/**
	 * Shows a given string in the "message area"
	 * @param s Given String
	 */
	public static void message(String s) {
        messagePanel.setText(s);
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
}