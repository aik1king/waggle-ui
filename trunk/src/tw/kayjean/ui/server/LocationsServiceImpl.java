package tw.kayjean.ui.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import tw.kayjean.ui.client.rpc.LocationsService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class LocationsServiceImpl extends RemoteServiceServlet implements LocationsService {

	public Map getLocations() {
		
        // Since we need both the location name and avg_node, map strings => avg_node
        Map locStringsCoords = new HashMap(); //Set locStrings = new HashSet();
        locStringsCoords.put("aaaaa", "11111");
        locStringsCoords.put("bbbbb", "22222");
        locStringsCoords.put("ccccc", "33333");
		return locStringsCoords; 
	}
	public List getLocations(String prefix, int limit)
	{
		//輸入文字,查詢合適內容
		//目前用simpledb,還不需要放入s3
		return null;
	}
}
