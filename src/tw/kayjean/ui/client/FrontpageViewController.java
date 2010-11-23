package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FrontpageViewController extends Composite {
	
	private VerticalPanel outer = new VerticalPanel ();
	
	public FrontpageViewController () {

		outer.getElement().setId ( "FrontpageViewController" );
		outer.setSpacing(10);
		outer.add ( new HTML ( "This demo uses Facebook Connect. Please click to login " ) );
		//這個項目會隨著登入後,自動變成logout文字
		outer.add ( new HTML ( "<fb:login-button autologoutlink='true' perms='publish_stream,read_stream' /> " ) );
		outer.add ( new HTML ( "<hr/>" ) );
		//這一句是加入下面be the first to xxx ,沒特別用處
		//找到名稱方式是,個人登入後,進入開發者,再點選個人,個人裡面就會有名稱
		outer.add ( new HTML ( "<hr/><fb:comments xid='albumupload' />" ) );
		initWidget ( outer );
	}
}
