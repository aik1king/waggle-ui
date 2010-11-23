package tw.kayjean.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;

import tw.kayjean.ui.client.rpc.CoordinateService;
import tw.kayjean.ui.client.rpc.CoordinateServiceAsync;

import tw.kayjean.ui.sdk.FBCore;
import tw.kayjean.ui.sdk.FBEvent;
import tw.kayjean.ui.sdk.FBXfbml;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
//所謂的ValueChangeHandler 其實是指網站URL改變  http://gwttutorials.com/tag/valuechangehandler/
//其實不太需要使用
//public class Waggle_ui implements EntryPoint, ValueChangeHandler<String> {
public class Waggle_ui implements EntryPoint {
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
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
//	private final GreetingServiceAsync greetingService = GWT
//			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
/*	
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {

			 //Fired when the user clicks on the sendButton.
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			 //Fired when the user types in the nameField.
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			 //Send the name from the nameField to the server and wait for a response.
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
*/	
	
	public static CoordinateServiceAsync coordService;
	
	public void onModuleLoad() {
		
//		History.addValueChangeHandler ( this );
		fbCore.init(APPID, status, cookie, xfbml);

		setupRPC();						// Set up the RPC services
		(new WUF()).initialize();		// Build the UI
		RootPanel.get("start").setVisible(false); // Get rid of the Please Wait
//delete		WUF.mPanel.jsniResize();		// Ensure map is the right size
//delete		WUF.mPanel.recenter();			// Dramatic entrance
		
		
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
		WUF.test2( new UserInfoViewController ( fbCore ) );
		fbXfbml.parse();
	}
	
	/**
	 * Render GUI when not logged in
	 */
	private void renderWhenNotLoggedIn () {
		WUF.test2( new FrontpageViewController () );
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
//            sideBarView.setWidget( new HomeSideBarPanel () );
            renderWhenLoggedIn();
        }
	}
	
	
	//第二組
	/**
	 * Render GUI
	 */
	private void renderApp ( String token ) {
        renderHomeView ();
	}
	//facebook http://code.google.com/p/restfb/
	//http://restfb.com/
	//
	
/*	
	//系統基礎
    public void onValueChange(ValueChangeEvent<String> event) {
        renderApp ( event.getValue() );
    }
*/
	
}

