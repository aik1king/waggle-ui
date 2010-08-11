package tw.kayjean.ui.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import tw.kayjean.ui.server.graph.Node;

@RemoteServiceRelativePath("coordinate")
public interface CoordinateService extends RemoteService{
	 public Node getNode(String s);
	 public String getIPLocation( );
}
