package tw.kayjean.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

import tw.kayjean.ui.client.rpc.CoordinateService;
import tw.kayjean.ui.client.rpc.CoordinateServiceAsync;

import tw.kayjean.ui.sdk.FBCore;
import tw.kayjean.ui.sdk.FBEvent;
import tw.kayjean.ui.sdk.FBXfbml;

import tw.kayjean.ui.client.model.FBDetail;
import tw.kayjean.ui.client.model.FBFriends;
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

//另外一種寫法
//facebook http://code.google.com/p/restfb/
//http://restfb.com/


//所謂的ValueChangeHandler 其實是指網站URL改變  http://gwttutorials.com/tag/valuechangehandler/
//其實不太需要使用
//public class Waggle_ui implements EntryPoint, ValueChangeHandler<String> {
public class Waggle_ui implements EntryPoint, ValueChangeHandler<String>  {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
//	private static final String SERVER_ERROR = "An error occurred while "
//			+ "attempting to contact the server. Please check your network "
//			+ "connection and try again.";


	//進入系統後,選擇應用程式設定
	//選擇authorized
	//選擇developer (前面動作好像是多餘的)
	//進入applications
	//選擇register(里面文字有點多,要找一下)
	//這時候應該會看到原本AP了 albumupload
	
	//kayjean
	public String APPID = "b2bbee7655d167948c9d84160210240b";
	
	//demo
	//public String APPID = "1d81c942b38e2e6b3fc35a147d371ab3"; 
	
	public static FBCore fbCore = GWT.create(FBCore.class);
	private FBEvent fbEvent = GWT.create(FBEvent.class);
	private FBXfbml fbXfbml = GWT.create(FBXfbml.class);
	
	private boolean status = true;
	private boolean xfbml = true;
	private boolean cookie = true;

	private DockPanel mainPanel = new DockPanel ();
	private VerticalPanel mainView = new VerticalPanel ();

	public static CoordinateServiceAsync coordService;
	public static FBDetail username;
	public static FBFriends friends;
	
	public void onModuleLoad() {
		
//		History.addValueChangeHandler ( this );
		fbCore.init(APPID, status, cookie, xfbml);

		setupRPC();						// Set up the RPC services


		RootPanel root = RootPanel.get();
		root.getElement().setId ( "TheApp" );
		mainView.getElement().setId("MainView");
		mainPanel.add( new TopMenuPanel () , DockPanel.NORTH );
		mainPanel.add( mainView, DockPanel.CENTER );
		root.add ( mainPanel );
		
		//第一組
		//只要按下登入或是登出,都是來這邊檢查
		//
		// Callback used when session status is changed
		//
		class SessionChangeCallback extends Callback<JavaScriptObject> {
			public void onSuccess ( JavaScriptObject response ) {
			    // Make sure cookie is set so we can use the non async method
			    renderHomeView ();
			}
		}
		//
		// Get notified when user session is changed
		//
		SessionChangeCallback sessionChangeCallback = new SessionChangeCallback ();
		fbEvent.subscribe("auth.sessionChange",sessionChangeCallback);

		//第二組
		//一開始程式進入,就是來這裡檢查有沒有登入
		//或是說URL改變時,也會來這邊檢查,原本系統有三個,第一個是基本範例,"example/stream.publish","example/friends"
		//example部份要繼續STUDY
		// Callback used when checking login status
		class LoginStatusCallback extends Callback<JavaScriptObject> {
			public void onSuccess ( JavaScriptObject response ) {
				renderApp( Window.Location.getHash() );
			}
		}
		LoginStatusCallback loginStatusCallback = new LoginStatusCallback ();
		// Get login status
		fbCore.getLoginStatus( loginStatusCallback );
	}

	private void setupRPC() {
		// Create an instance of the asynchronous interface.
		coordService = (CoordinateServiceAsync) GWT.create(CoordinateService.class);
	}

	//第一組
	/**
	 * Render GUI when logged in
	 */
	private void renderWhenLoggedIn () {
		mainView.clear();
		mainView.add( new WUF() );
		
		mainView.add( new UserInfoViewController ( fbCore ) );
		//目前被修改成
		//要達到目標是,顯示地圖項目,可以開始運作了
//		(new WUF()).initialize();		// Build the UI
//		RootPanel.get("start").setVisible(false); // Get rid of the Please Wait
//		WUF.test2( new UserInfoViewController ( fbCore ) );
		fbXfbml.parse();
//		mainView.setWidget ( new WUF() );
	}
	
	/**
	 * Render GUI when not logged in
	 */
	private void renderWhenNotLoggedIn () {
		mainView.clear();
		mainView.add ( new FrontpageViewController () );
		//目前被修改成
		//要達到目標是,完全不出現地圖畫面,只出現一些評論還有等待登入
//		WUF.test2( new FrontpageViewController () );
		fbXfbml.parse();
	}

	/**
	 * Render home view. If user is logged in display welcome message, otherwise
	 * display login dialog.
	 */
	private void renderHomeView () {
//	    sideBarView.clear();
        if ( fbCore.getSession() == null ) {
            renderWhenNotLoggedIn ();
        } else {
        	//加入左邊點擊區域,加入更多EXAMPLE機會
//            sideBarView.setWidget( new HomeSideBarPanel () );
            renderWhenLoggedIn();
        }
	}
	
	//第二組
	/**
	 * Render GUI
	 */
	private void renderApp ( String token ) {
	    token = token.replace("#", "");
	    if ( token == null || "".equals ( token ) || "#".equals ( token ) ) 
	    {
	        token = "home";
	    }
        if ( token.endsWith("home") ) {
            renderHomeView ();
        } else {
            Window.alert ( "Unknown  url "  + token );
        }
	}
	
	//系統基礎
    public void onValueChange(ValueChangeEvent<String> event) {
        renderApp ( event.getValue() );
    }
}
