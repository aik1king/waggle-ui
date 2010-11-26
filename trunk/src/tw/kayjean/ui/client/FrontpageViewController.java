package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Image;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.DragController; 

public class FrontpageViewController extends Composite {
//	private VerticalPanel outer = new VerticalPanel ();
	
	
	private AbsolutePositionDropController dropController;
	
    private static final int COLUMNS = 2;
    private static final int ROWS = 2;
    private static final int IMAGE_HEIGHT = 58;
    private static final int IMAGE_WIDTH = 65;
	
	
	public FrontpageViewController () {
/*		
		outer.getElement().setId ( "FrontpageViewController" );
		outer.setSpacing(10);
		outer.add ( new HTML ( "This demo uses Facebook Connect. Please click to login " ) );
		//這個項目會隨著登入後,自動變成logout文字
		outer.add ( new HTML ( "<fb:login-button autologoutlink='true' perms='publish_stream,read_stream' /> " ) );
		//這一句是加入下面be the first to xxx ,沒特別用處
		outer.add ( new HTML ( "<hr/><fb:comments xid='albumupload' />" ) );
		initWidget ( outer );
*/		
		
		
	    AbsolutePanel containingPanel = new AbsolutePanel();
//		AbsolutePanel boundaryPanel = new AbsolutePanel();
//		boundaryPanel.add( outer );
		PickupDragController dragController = new PickupDragController(containingPanel, true);			
	    //addStyleName(CSS_DEMO_BIN_EXAMPLE);
	    // use the containing panel as this composite's widget
		
	    containingPanel.setPixelSize(500, 200);      
	    setWidget(containingPanel);        
	    // create a trash bin      
//	    Bin trashBin = new TrashBin(120, 120);      
//	    containingPanel.add(trashBin, 30, 30);        
	    // create a bin that won't accept trash      
//	    Bin closedBin = new Bin(120, 120);      
//	    containingPanel.add(closedBin, 350, 30);        
	    // add drop controller for trash bin      
//	    DropController openTrashBinDropController = new BinDropController(trashBin);      
//	    dragController.registerDropController(openTrashBinDropController);        
	    // add drop controller for closed bin      
//	    DropController closedTrashBinDropController = new BinDropController(closedBin);      
//	    dragController.registerDropController(closedTrashBinDropController);        

        //friends
        FlexTable flexTable = new FlexTable();
        // create our grid
        for (int i = 0; i < COLUMNS; i++) {
          for (int j = 0; j < ROWS; j++) {
            // create a simple panel drop target for the current cell
            SimplePanel simplePanel = new SimplePanel();
            simplePanel.setPixelSize(IMAGE_WIDTH, IMAGE_HEIGHT);
            flexTable.setWidget(i, j, simplePanel);
            //flexTable.getCellFormatter().setStyleName(i, j, CSS_DEMO_PUZZLE_CELL);

            // place a pumpkin in each panel in the cells in the first column
            //if (j == 0) {
            //  simplePanel.setWidget(createDraggable());
            //}
            Bin trashBin = new TrashBin(IMAGE_WIDTH, IMAGE_HEIGHT);
            simplePanel.setWidget( trashBin );
            BinDropController openTrashBinDropController = new BinDropController(trashBin);
            dragController.registerDropController(openTrashBinDropController);
            
            // instantiate a drop controller of the panel in the current cell
            //SetWidgetDropController dropController = new SetWidgetDropController(simplePanel);
            //dragController.registerDropController(dropController);
          }
        }
        containingPanel.add(flexTable, 0, 0);
	    
	    
	    // create a drop controller for the containing panel      
	    dropController = new AbsolutePositionDropController(containingPanel);      
	    dragController.registerDropController(dropController);        
	    containingPanel.add(createDraggable(dragController), 200, 20);      
	    containingPanel.add(createDraggable(dragController), 240, 50);      
	    containingPanel.add(createDraggable(dragController), 190, 100);
//	    outer.add( containingPanel );

	    
	    
	}
	
	protected Widget createDraggable(DragController dragController) {
		Image image = new Image("images/info.png");
		dragController.makeDraggable(image);
		return image;
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