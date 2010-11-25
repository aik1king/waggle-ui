package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.Widget;

final class TrashBin extends Bin {

	  private static final String CSS_DEMO_TRASHBIN = "demo-trashbin";

	  private static final String CSS_DEMO_TRASHBIN_ENGAGE = "demo-trashbin-engage";

	  private int count;

	  public TrashBin(int width, int height) {
	    super(width, height);
	    addStyleName(CSS_DEMO_TRASHBIN);
	  }

	  @Override
	  public void eatWidget(Widget widget) {
	    widget.removeFromParent();
	    count++;
	    updateText();
	  }

	  @Override
	  public boolean isWidgetEater() {
	    return true;
	  }

	  @Override
	  public void setEngaged(boolean engaged) {
	    if (engaged) {
	      addStyleName(CSS_DEMO_TRASHBIN_ENGAGE);
	    } else {
	      removeStyleName(CSS_DEMO_TRASHBIN_ENGAGE);
	    }
	  }

	  @Override
	  protected void updateText() {
	    String text;
	    if (count == 0) {
	      text = "currently empty";
	    } else {
	      text = "contains " + count + " item" + (count == 1 ? "" : "s");
	    }
	    setHTML("<b>Trash Bin</b><br>\n" + "(" + text + ")<br>\n<br>\n"
	        + "<i>try dropping something on me</i>");
	  }
	}
