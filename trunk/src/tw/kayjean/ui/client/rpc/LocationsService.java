package tw.kayjean.ui.client.rpc;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("location")
public interface LocationsService extends RemoteService {
    
    /**
     * We need this here for GWT to optimize. Don't change this unless the
     * parameters or return value changes!
     * 
     * @gwt.typeArgs <java.lang.String,edu.berkeley.cs169.server.graph.Node>
     */
	
	//某個文字丟進去,跑出適合的結果,查詢結果
    public Map getLocations();
    public List getLocations(String prefix, int limit);
}
