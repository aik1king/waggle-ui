package tw.kayjean.ui.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.kayjean.ui.client.rpc.TastingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TastingServiceImpl extends RemoteServiceServlet implements TastingService {

	public List getTaste(List locations){
		return null;
	}
}
