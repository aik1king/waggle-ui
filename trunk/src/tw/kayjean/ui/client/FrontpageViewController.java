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
		//這一句是加入下面be the first to xxx ,沒特別用處
		outer.add ( new HTML ( "<hr/><fb:comments xid='albumupload' />" ) );
		initWidget ( outer );
	}
}

//原本網站資料是 http://www.facebook.com/apps/application.php?id=37309251911#!/apps/application.php?id=37309251911&v=wall
//可以看到POST內容
//首先要調整設定成為,讓albumupload在沒有LOGIN時,也可以看到內容,匿名發文
//控制位置是 http://www.facebook.com/developers/createapp.php#!/developers/apps.php
//在 第二項 網站部份 記得加入siteurl,這樣才能通過驗證,否則登入時會怪怪的
//其他部分看不出來 如何設定,決定到控制頁面
//目前網站位置是 http://www.facebook.com/apps/application.php?id=102921393079418
//有個編輯應用程式,選擇編輯應用程式設定....這裡會跳回原本位置
//塗鴉牆設定,改成所有留言

//當從應用程式,選擇管理頁面後,打開匿名留言,就會看到全部內容了耶,和一般相同