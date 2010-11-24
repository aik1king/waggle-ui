package tw.kayjean.ui.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CoordinateSendCallback implements AsyncCallback {

	public void onFailure(final Throwable caught) {
		GWT.log("Error in CoordinateService!", caught);
		caught.printStackTrace();
		// doThisBeforeReturn(true);
	}

	public void onSuccess(final Object result) {
		if (result == null ) {
			return;
		}
		Integer r = (Integer) result;
		return;
	}
}
