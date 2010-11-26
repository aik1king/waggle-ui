package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.Widget;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;

/**
 * Sample SimpleDropController which discards draggable widgets which are dropped on it.
 */
final class BinDropController extends SimpleDropController {

  private static final String CSS_DEMO_BIN_DRAGGABLE_ENGAGE = "demo-bin-draggable-engage";

  private Bin bin;

  public BinDropController(Bin bin) {
    super(bin);
    this.bin = bin;
  }

  @Override
  public void onDrop(DragContext context) {
    for (Widget widget : context.selectedWidgets) {
      bin.eatWidget(widget);
    }
    super.onDrop(context);
  }

  @Override
  public void onEnter(DragContext context) {
    super.onEnter(context);
    for (Widget widget : context.selectedWidgets) {
      widget.addStyleName(CSS_DEMO_BIN_DRAGGABLE_ENGAGE);
    }
    bin.setEngaged(true);
  }

  @Override
  public void onLeave(DragContext context) {
    for (Widget widget : context.selectedWidgets) {
      widget.removeStyleName(CSS_DEMO_BIN_DRAGGABLE_ENGAGE);
    }
    bin.setEngaged(false);
    super.onLeave(context);
  }

  @Override
  public void onPreviewDrop(DragContext context) throws VetoDragException {
    super.onPreviewDrop(context);
    if (!bin.isWidgetEater()) {
      throw new VetoDragException();
    }
  }
}
