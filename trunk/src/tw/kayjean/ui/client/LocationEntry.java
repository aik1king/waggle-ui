package tw.kayjean.ui.client;

import com.allen_sauer.gwt.dnd.client.HasDragHandle;
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

/**
 * Inner class representing one element of the routeFlow.
 * Contains both a label (name) of the Location, and a delete button for it, plus extras.
 * @author Simon
 */
public class LocationEntry extends Composite implements ClickListener,HasDragHandle {

    private PushButton bDelete = new PushButton(new Image("images/button_remove.png"),this);
    private PushButton bDetails = new PushButton(new Image("images/button_details.png"),this);
    private ToggleButton bShowOnly = new ToggleButton(new Image("images/button_eye.gif"),this);
    private Label loctext;
    protected String name;
    protected double x;
    protected double y;
    protected String geocell;

    // Stopover options for *after* this Location
//delete    private StopoversSet stopovers = new StopoversSet();
    private Panel stopoversPanel;
    private HTML soClick;
    private VerticalPanel exts;

    // In buildStopOverOptions()
    private CheckBox passCafe = new CheckBox("place to eat");
    private CheckBox passLibrary = new CheckBox("library");
    private CheckBox passLab = new CheckBox("Computer Lab (students only)"); 

    private int myIndex;
    private int myType;

    public LocationEntry(final String inName , double inx , double iny , int type , String ingeocell ) {
        name = inName;
        x = inx;
        y = iny;
        myType = type;
        geocell = ingeocell;
        VerticalPanel contents = new VerticalPanel();

        contents.add(buildLabel());
        contents.add(buildExtras());
        stopoversPanel.setVisible(false);
        //hideExtras();

        initWidget(contents);
        this.setStyleName("locationEntry");
    }
    
    public LocationEntry(final String inName) {
        name = inName;
        VerticalPanel contents = new VerticalPanel();

        contents.add(buildLabel());
        contents.add(buildExtras());
        stopoversPanel.setVisible(false);
        //hideExtras();

        initWidget(contents);
        this.setStyleName("locationEntry");
    }

    /**
     * Builds the basic part of a LocationEntry.
     * Namely, the label of its name and a delete button.
     * In the old days, this was all there was to it.
     * @return ColumnPanel of old
     */
    private HorizontalPanel buildLabel() {
        HorizontalPanel p = new HorizontalPanel();
        loctext = new Label(name);
        
        HorizontalPanel buttons = new HorizontalPanel();
        buttons.add(bDetails);
        buttons.add(bDelete);

        p.add(loctext);
        p.add(buttons);

        bDelete.setTitle("Click to delete \"" + name + "\"");
        bDetails.setTitle("Click to show details about \"" + name + "\"");

        //p.setCellVerticalAlignment(loctext, HasVerticalAlignment.ALIGN_MIDDLE);
        //p.setCellHorizontalAlignment(bDelete, HasHorizontalAlignment.ALIGN_RIGHT);
        p.setStyleName("label");
        loctext.setStyleName("text");
        buttons.addStyleName("buttons");
        return p;
    }

    private VerticalPanel buildExtras() {
        exts = new VerticalPanel();
        HorizontalPanel extsBaseRow = new HorizontalPanel();

        soClick = new HTML("<div class=\"togglelink\">set stopovers</div>");
        soClick.setTitle("Click to add some stopovers in between this location and the next.");
        soClick.addClickListener(this);

        bShowOnly.setTitle("Click to hide all route segments except this one! Click again to restore.");

        stopoversPanel = buildStopOvers();
        
        extsBaseRow.add(bShowOnly);
        extsBaseRow.add(soClick);

        //extsBaseRow.setCellHorizontalAlignment(soClick, HasHorizontalAlignment.ALIGN_RIGHT);

        exts.add(extsBaseRow);
        exts.add(stopoversPanel);

        exts.setStyleName("extras");
        extsBaseRow.setStyleName("base_row");
        bShowOnly.setStyleName("eye");
        stopoversPanel.setStyleName("stopovers");
        return exts;
    }

    protected void hideExtras() {
        exts.setVisible(false);
    }

    protected void showExtras() {
        exts.setVisible(true);
    }

    /**
     * 
     * @return stopovers to requirements so that there is no spacing for these elements
     */
    private VerticalPanel buildStopOvers() {
        VerticalPanel contents = new VerticalPanel();
        Label stopoverLabel = new Label("After " + name + ", stop by a:");

        VerticalPanel stopBoxes = new VerticalPanel();
        stopBoxes.add(passCafe);        
        stopBoxes.add(passLibrary);
        stopBoxes.add(passLab); 
        //stopBoxes.setStyleName("indented-block");

        passCafe.addClickListener(this);
        passLibrary.addClickListener(this);
        passLab.addClickListener(this);

        contents.add(stopoverLabel);
        contents.add(stopBoxes);

        return contents;
    }

    /**
     * Expands the extras panel
     */
    private void toggleExtras() {
    	//顯示完整HTML
    	//重覆點選,可以造成打開,關閉效果
        stopoversPanel.setVisible(!stopoversPanel.isVisible());
    }

    /**
     * Resets the stopovers required after this Location
     */
    protected void resetOptions() {
        passCafe.setChecked(false);
        passLab.setChecked(false);
        passLibrary.setChecked(false);
    }

    /**
     * Use this method to assign a number to be visible on the location entry bar
     * @param index The number you want assigned.
     */
    protected void setIndex(int index) {
        myIndex = index;
        loctext.setText(myIndex+1 + ") " + this.name);
    }

    /**
     * Generates and returns a StopoversSet object representing
     * this LocationEntry's stopover selection
     * @return StopoverSet
     */
/*delete    
    protected StopoversSet getStopoversSet() {
        stopovers.passCafe = passCafe.isChecked();
        stopovers.passLab = passLab.isChecked();
        stopovers.passLib = passLibrary.isChecked();
        return stopovers;
    }
*/
    
    /**
     * Catches clicks on the delete button and more
     */
    public void onClick(final Widget sender) {
        if (sender == bDelete) {
        	
        	//1.目前就是先送去SERVER
        	//2.還有送入那個TABLE中
        	
            // When the button is hit, remove ourselves from the list
        	//將自己移動到別的區域,清除目前區域的連結
        	//因為所有項目都是一個存在項目,不會有單純消失情形,只是移動到不同區域
        	//可是自己去過地方那麼多,會不會變成一大團很難尋找呢
        	//現階段先直接塞進去,未來則是,在SERVER讀取出一堆內容後,利用框框大小過濾內容,這會比較花時間,不過應該還屬於合理範圍,只怕內容太多
        	//
        	//最簡單方式就是比照景點顯示,每次查詢,都把當時景點和參與過景點
        	//在local端相互檢查
        	//即使切到使用者紀錄時,也可以快速顯示,不需要到server中再次詢問
        	//反正先尋找已經參加過
        	//扣除之後,就是剩下的
        	
        	//目前每個移動,都會在local先記錄起來
        	//並且通知server
        	//一定要簡單,新加入之後,就要傳送給
        	
        	//登入之後一次把東西全部送過來
        	//增加項目,只是把東西加入POOL
        	
        	//動作都會送入SERVER,由SERVER丟入QUEUE進行處理
        	//先不管LOCAL的INDEX之類機制
        	//不過檔案最好可以
        	
        	WUF.removeLocation( myType , this );
        }
        else if (sender == bDetails) {
            WUF.setSWF(this.name);
        }
        else if (sender == soClick) {
            toggleExtras();
        }
        else if (sender == bShowOnly) {
/*        	
            if (currHighlight == myIndex) {
                showAllAgain();
            } else {
                currHighlight = myIndex; // Set the currently highlighted
//delete                    onlyShowRoute(myIndex+1);
            }
*/            
        }
        else if (sender == passLab ||
                sender == passCafe ||
                sender == passLibrary){
//            signalMaptoDrawRoute();
        }
    }

    /**
     * What region can we drag on?
     */
    public Widget getDragHandle() {
        return loctext;
    }

}
