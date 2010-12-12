package tw.kayjean.ui.client.rpc;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import tw.kayjean.ui.client.WUF;
import tw.kayjean.ui.client.model.Node;

public class CoordinateRTreeCallback implements AsyncCallback{

	public void onFailure(final Throwable caught) {
		GWT.log("Error in CoordinateService!", caught);
		caught.printStackTrace();
		//        doThisBeforeReturn(true);
	}

	public void onSuccess(final Object result ) {
		List avgNodes = (List) result;
		if (result == null) {
			GWT.log("CoordinateRTreeCallback got a null coordinate!", null);
			//MainFrame.message( "找不到符合地點" );
			//doThisBeforeReturn(false);
			return;
		}
		for (Iterator locIter = avgNodes.iterator(); locIter.hasNext();) {
			Node avgNode = (Node) locIter.next();
			if( WUF.mPanel.pointinbox(avgNode.y, avgNode.x) == true ){
				//如果節點屬於目前視窗範圍,才要加入,不然不需要加入
				
				//加入時候,應該依照數值進行排序,分數高的放上面
				if( avgNode.type == 0 )
					WUF.locPanel.addLocation(avgNode);
				else if( avgNode.type == 3 )
					WUF.favPanel.addFavorite(avgNode);
			}
		}
		//各個類別分別存入自己歸屬內容
	}
}
