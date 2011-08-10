package tw.kayjean.ui.client;

import tw.kayjean.ui.client.rpc.CoordinateSendCallback;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Image;
import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.model.FBFriend;

class Bin extends HTML {

	  private static final String CSS_DEMO_BIN = "demo-bin";
	  private static final String CSS_DEMO_BIN_ENGAGE = "demo-bin-engage";
	  private static final String CSS_DEMO_TRASHBIN = "demo-trashbin";
	  private static final String CSS_DEMO_TRASHBIN_ENGAGE = "demo-trashbin-engage";

	  private String id;
	  private String name;
	  private int count;
	  
	  public Bin(int width, int height , FBFriend t ) {
	    setPixelSize(width, height);
	    id = t.id;
	    name = t.name;
	    count = t.item_me2friends;
	    updateText();
	    addStyleName(CSS_DEMO_BIN);
	  }

	  public void eatWidget(Widget widget) {
		    Node placename = ((LocationEntry)widget).n;
		    widget.removeFromParent();
		    count++;
		    updateText();
		    //送入系統
		    placename.fid = Waggle_ui.username.id;
		    placename.tid = id;
	    	DataSwitch.get().sendNode( placename , new CoordinateSendCallback() );
	}

		  public boolean isWidgetEater() {
		    return true;
		  }

		  public void setEngaged(boolean engaged) {
		    if (engaged) {
		      addStyleName(CSS_DEMO_TRASHBIN_ENGAGE);
		    } else {
		      removeStyleName(CSS_DEMO_TRASHBIN_ENGAGE);
		    }
		  }

		  protected void updateText() {
		    String text;
		    //Image image = new Image("http://graph.facebook.com/" + id + "/picture" );
		    if (count == 0) {
		      text = "currently empty";
		    } else {
		      text = "contains " + count + " item" + (count == 1 ? "" : "s");
		    }
		    //setHTML("<b>" + name + "</b><br>\n" + "(" + text + ")<br>\n<br>\n" + "<i>try dropping someitems on me</i>");
		    if( id.equalsIgnoreCase( Waggle_ui.username.id ) )
		    	setHTML( "<img src=\"http://graph.facebook.com/" + id + "/picture\" ><br>" + name + "<br>items " + count );
		    else if( id.equalsIgnoreCase( Waggle_ui.username.id + "_trash" ) )
		    	setHTML( name + " items " + count );
		    else if( id.equalsIgnoreCase( Waggle_ui.username.id + "_wish" ) )
		    	setHTML( name + " items " + count );
		    else
		    	setHTML( "<img src=\"http://graph.facebook.com/" + id + "/picture\" ><br>" + name + "<br>items " + count );
		  }
	}

