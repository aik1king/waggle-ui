package tw.kayjean.ui.client.rpc;

import tw.kayjean.ui.client.WUF;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CoordinateIPLocation implements AsyncCallback {

	public void onFailure(final Throwable caught) {
		GWT.log("Error in CoordinateService!", caught);
		caught.printStackTrace();
		// doThisBeforeReturn(true);
	}

	public void onSuccess(final Object result) {
		String address = (String) result;
		if (result == null) {
			return;
		}
		WUF.mPanel.Clear();
		//Hsinchu-24.8047-120.9714
		String items[] = address.split("-");
		double y = Double.parseDouble(items[1]);
		double x = Double.parseDouble(items[2]);
		WUF.mPanel.Move( y, x , 10 );
		return;
	}

}
