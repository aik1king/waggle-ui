package tw.kayjean.ui.client;

//http://examples.roughian.com/index.htm#Widgets~MenuBar

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import tw.kayjean.ui.client.model.Node;

public class ReferencePanel extends Composite {
	ReferenceMap refMap;
	TextBox centerLat = new TextBox();
	TextBox centerLon = new TextBox();
	Grid center = new Grid(1,2);
	
	private VerticalPanel routeTab = new VerticalPanel();

	public ReferencePanel() {
		center.setWidget(0, 0, centerLat);
		center.setWidget(0, 1, centerLon);
		Grid tools = new Grid(2,2);
		refMap = new ReferenceMap(LatLng.newInstance(0.0, 0.0), 0, 256, 360);
		refMap.setDataBounds(LatLngBounds.newInstance(LatLng.newInstance(-89.5, -179.5), LatLng.newInstance(89.5, 179.5)), 1., true);
		RadioButton xy = new RadioButton("tool", "Latitude/Longitude");
		xy.setValue(true);
		xy.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refMap.setToolType("xy");				
			}
		});
		tools.setWidget(0, 0, xy);
		RadioButton y = new RadioButton("tool", "Latitude Line");
		y.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refMap.setToolType("y");			
			}
			
		});
		tools.setWidget(0, 1, y);
		RadioButton x = new RadioButton("tool", "Longitude Line");
		x.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refMap.setToolType("x");				
			}
			
		});
		tools.setWidget(1, 0, x);
		RadioButton pt = new RadioButton("tool", "Point");
		pt.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				refMap.setToolType("pt");				
			}
			
		});
		tools.setWidget(1, 1, pt);
		RegionWidget dataRegions = new RegionWidget(refMap, "Select Data Region", true);
		dataRegions.setChangeListener(new ChangeListener() {
            
			@Override
			public void onChange(Widget sender) {
				RegionWidget rw = (RegionWidget) sender;
				refMap.setDataBounds(rw.getBounds(), 1., true);
				
			}
			
		});
		
		
        routeTab.add(dataRegions);
        routeTab.add(tools);
        routeTab.add(center);
        routeTab.add(refMap);
		initWidget(routeTab);
	}
}
