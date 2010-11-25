package tw.kayjean.ui.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import tw.kayjean.ui.client.model.FBDetail;
import tw.kayjean.ui.client.model.FBFriends;
import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.model.Poi;

@RemoteServiceRelativePath("coordinate")
public interface CoordinateService extends RemoteService{
	//某個景點名稱丟入,取出詳細描述和說明
	 public Poi getNode(String s);
	 public String getIPLocation();
	 public List getRTree( String username , String name );
	 public Integer sendNode( String username , int type , Node nd );
	 public List sendDetail ( FBDetail fd , FBFriends ffs );	 
//	 public List getLocations(String prefix, int limit);
}
