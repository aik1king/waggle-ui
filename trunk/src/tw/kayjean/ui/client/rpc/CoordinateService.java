package tw.kayjean.ui.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import tw.kayjean.ui.client.model.Node;

@RemoteServiceRelativePath("coordinate")
public interface CoordinateService extends RemoteService{
	//某個景點名稱丟入,取出詳細描述和說明
	 public Node getNode(String s);
	 public String getIPLocation();
	 public List getRTree( String username , String name );
	 
	 public Integer sendNode( String username , int type , String name , double x , double y , String geocell );
	 
	 public List getLocations(String prefix, int limit);
}
