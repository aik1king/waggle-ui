package tw.kayjean.ui.client;

import java.util.Vector;
import com.google.gwt.maps.client.overlay.Overlay;

public class GOverlayCollection {
	Vector dex = new Vector();
	Overlay[] cache;
	
	public void add(Overlay overlay){
		cache = null;
		dex.add(overlay);
	}
	
	public Overlay get(int i){
		return (Overlay)dex.get(i);
	}
	
	
}
