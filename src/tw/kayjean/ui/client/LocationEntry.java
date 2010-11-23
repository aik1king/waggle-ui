package tw.kayjean.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;

import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.model.Poi;

/**
 * Inner class representing one element of the routeFlow.
 * Contains both a label (name) of the Location, and a delete button for it, plus extras.
 * @author Simon
 */
public class LocationEntry extends Composite implements ClickListener,HasDragHandle {

    private Label loctext;
    protected Node n = new Node();
    private Panel detailPanel;
    private HTML soClick;
    private HTML poidetail = new HTML("");
    private int myType;

    public LocationEntry(final String inName , String infullname , double inx , double iny , int type , String ingeocell ) {
    	//設定基本資料
    	n.name = inName;
    	n.fullname = infullname;
    	n.x = inx;
    	n.y = iny;
        n.geocell = ingeocell;
        myType = type;
        
        VerticalPanel contents = new VerticalPanel();
        loctext = new Label(n.name);
        contents.add(loctext);
        contents.add(buildExtras());
        detailPanel.setVisible(false);
        initWidget(contents);
        
        this.setStyleName("locationEntry");
    }
    

    private VerticalPanel buildExtras() {

        HorizontalPanel extsBaseRow = new HorizontalPanel();

        ToggleButton bShowOnly = new ToggleButton(new Image("images/button_eye.gif"),this);
        bShowOnly.setTitle("Click to hide all route segments except this one! Click again to restore.");
        extsBaseRow.add(bShowOnly);
        
        soClick = new HTML("<div class=\"togglelink\">詳細內容</div>");
        soClick.setTitle("顯示詳細內容.");
        soClick.addClickListener(this);
        extsBaseRow.add(soClick);

        VerticalPanel exts = new VerticalPanel();
        exts.add(extsBaseRow);
        
        detailPanel = buildStopOvers();
        exts.add(detailPanel);
        
        exts.setStyleName("extras");
        extsBaseRow.setStyleName("base_row");
        bShowOnly.setStyleName("eye");
        detailPanel.setStyleName("stopovers");
        return exts;
    }
    
    /**
     * 
     * @return stopovers to requirements so that there is no spacing for these elements
     */
    private VerticalPanel buildStopOvers() {
        VerticalPanel contents = new VerticalPanel();
        
        Label stopoverLabel = new Label("After " + n.name + ", stop by a:");
        contents.add(stopoverLabel);

        VerticalPanel stopBoxes = new VerticalPanel();
        stopBoxes.add(poidetail);
        contents.add(stopBoxes);
        return contents;
    }

    /**
     * Expands the extras panel
     */
    private void toggleExtras() {
    	
    	//重覆點選,可以造成打開,關閉效果
        detailPanel.setVisible(!detailPanel.isVisible());
        if( detailPanel.isVisible() ){
        	//可以看到內容
        	//顯示完整HTML,到server取得POI詳細內容
        	Waggle_ui.coordService.getNode(n.fullname, new AsyncCallback() {

        		public void onFailure(final Throwable caught) {
        			GWT.log("Error in CoordinateService!", caught);
        			caught.printStackTrace();
        			//        doThisBeforeReturn(true);
        		}

        		public void onSuccess(final Object result ) {

        			Poi poidata = (Poi) result;
        			if (poidata == null) {
        				GWT.log("CoordinateRTreeCallback got a null coordinate!", null);
        				//MainFrame.message( "找不到符合地點" );
        				//doThisBeforeReturn(false);
        				return;
        			}
        			poidetail.setHTML( poidata.toString() );
        		}
          }
    		);
        }
        else{
        	//清除HTML內容
        }
    }

    /**
     * Catches clicks on the delete button and more
     */
    public void onClick(final Widget sender) {
        if (sender == soClick) {
            toggleExtras();
        }
    }

    /**
     * What region can we drag on?
     */
    public Widget getDragHandle() {
    	//這裡可能範圍太小了
        return loctext;
    }
}
