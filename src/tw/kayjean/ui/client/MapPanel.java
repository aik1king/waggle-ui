package tw.kayjean.ui.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelVelocity;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MenuMapTypeControl;
import com.google.gwt.maps.client.control.ScaleControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapTypeChangedHandler;
import com.google.gwt.maps.client.event.MapZoomEndHandler;
import com.google.gwt.maps.client.event.MapMoveEndHandler;
import com.google.gwt.maps.client.event.MapDragEndHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.geom.LatLng;


import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MapMoveEndHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.geom.LatLngBounds;

public class MapPanel extends Composite {

	private static final int DEFAULT_ZOOM = 7;
	private static final LatLng DEFAULT_CENTER = LatLng.newInstance(23.63,
			120.9756);
	private MapWidget map;
	private Icon baseIcon;
	private boolean moveflag = true;

	public MapPanel() {

		map = new MapWidget(DEFAULT_CENTER, DEFAULT_ZOOM);
//	    map.setWidth("100%");
//	    map.setHeight("100%");
		map.setSize("300px", "300px");
		map.addControl(new LargeMapControl());
		map.clearOverlays();
		initWidget(map);

		baseIcon = Icon.newInstance();
		baseIcon.setShadowURL("http://www.google.com/mapfiles/shadow50.png");
		baseIcon.setIconSize(Size.newInstance(20, 34));
		baseIcon.setShadowSize(Size.newInstance(37, 34));
		baseIcon.setIconAnchor(Point.newInstance(9, 34));
		baseIcon.setInfoWindowAnchor(Point.newInstance(9, 2));

		final MapMoveEndHandler h = new MapMoveEndHandler() {

			public void onMoveEnd(MapMoveEndEvent e) {
				if (map.getZoomLevel() > 12) {
					if (moveflag == true) {
						double w = map.getBounds().getSouthWest()
								.getLongitude();
						double ee = map.getBounds().getNorthEast()
								.getLongitude();
						double s = map.getBounds().getSouthWest().getLatitude();
						double n = map.getBounds().getNorthEast().getLatitude();
						//delete Maps.coordService.getRTree(w, ee, s, n,	new CoordinateRTreeCallback());
					} else
						moveflag = true;
				}
				// textBox.setText(textBox.getText() + "onMove()");
			}
		};
		map.addMapMoveEndHandler(h);
		
		//delete Maps.coordService.getIPLocation(new CoordinateIPLocation());
	}

	public InfoWindow GetInfoWnd() {
		return map.getInfoWindow();
	}

	public void Clear() {
		map.clearOverlays();
	}

	public void SetMoveFlag() {

	}

	public void MoveBound(LatLngBounds bounds) {
		if (!bounds.isEmpty()) {
			moveflag = false;
			int zoom = map.getBoundsZoomLevel(bounds);
			map.setCenter(bounds.getCenter(), zoom);
		}
		return;
	}

	public void Move(double y, double x, int zoom) {
		map.setCenter(LatLng.newInstance(y, x), zoom);
		return;
	}

	/**
	 * Dramatically recenters the map.
	 */
	protected void recenter() {
		map.setZoomLevel(DEFAULT_ZOOM);
		map.setCenter(DEFAULT_CENTER);
	}

	/**
	 * What you call when you want to resize the map dynamically. Resizes both
	 * the main map widget and the absolutePanel that holds it.
	 * 
	 * @param width
	 *            in pixels
	 * @param height
	 *            in pixels
	 */
	public void resize(int width, int height) {
		map.checkResizeAndCenter();
//		map.setSize(width + "px", height + "px");
	}


	public void AddOverlay( Overlay overlay ) {
		// map
		try {
			map.addOverlay(overlay);
		} catch (Exception e) {
		}
	}
	
	public void AddOverlay(double y, double x, String name, int type) {
		// map
		try {
			LatLng point = LatLng.newInstance(y, x);
			map.addOverlay(createMarker(point, name, type));
		} catch (Exception e) {
		}
	}

	private Marker createMarker(LatLng point, String number, int type) {
		final String n = number;
		Icon icon = Icon.newInstance(baseIcon);
		icon.setIconSize(Size.newInstance(20, 20));
		// icon.setImageURL("http://www.cwb.gov.tw/V5/symbol/symbol04.gif");
		switch (type) {
		case 1:
			icon.setImageURL("images/address.png");
			break;
		default:
			icon.setImageURL("images/others.png");
			break;
		}

		MarkerOptions options = MarkerOptions.newInstance();
		options.setIcon(icon);
		final Marker marker = new Marker(point, options);

		marker.addMarkerClickHandler(new MarkerClickHandler() {

			public void onClick(MarkerClickEvent event) {
				//InfoWindow info = map.getInfoWindow();
				//info.open(event.getSender(), new InfoWindowContent("³o¸Ì¬O <b>"+ n + "</b>"));
				
				Label directions = new Label( n );
			    directions.addClickListener(new ClickListener() {
			      public void onClick(Widget arg0) {
//			        WUF.inPanel.setLocString(n);
//			        WUF.inPanel.signalToAddLoc();
			      }
			    });
				FlowPanel panel = new FlowPanel();
				panel.add(directions);
				InfoWindow info = map.getInfoWindow();
				info.open(event.getSender(), new InfoWindowContent(panel));
				
			}
		});
		return marker;
	}
}
