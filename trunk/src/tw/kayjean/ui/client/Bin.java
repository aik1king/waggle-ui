package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;


class Bin extends HTML {

	  private static final String CSS_DEMO_BIN = "demo-bin";

	  private static final String CSS_DEMO_BIN_ENGAGE = "demo-bin-engage";

	  public Bin(int width, int height) {
	    setPixelSize(width, height);
	    updateText();
	    addStyleName(CSS_DEMO_BIN);
	  }

	  public void eatWidget(Widget widget) {
	  }

	  public boolean isWidgetEater() {
	    return false;
	  }

	  public void setEngaged(boolean engaged) {
	    if (engaged) {
	      addStyleName(CSS_DEMO_BIN_ENGAGE);
	    } else {
	      removeStyleName(CSS_DEMO_BIN_ENGAGE);
	    }
	  }

	  protected void updateText() {
	    setHTML("<b>Closed Bin</b><br>\n" + "(does not currently accept trash)<br>\n<br>\n"
	        + "<i>try dropping something on me</i>");
	  }
	}

