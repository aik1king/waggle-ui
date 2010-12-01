package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

import com.allen_sauer.gwt.dnd.client.PickupDragController;

public class FrontpageViewController extends Composite {
//	private VerticalPanel outer = new VerticalPanel ();
	
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

	}
	
}
