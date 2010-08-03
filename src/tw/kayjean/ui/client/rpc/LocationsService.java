package tw.kayjean.ui.client.rpc;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("location")
public interface LocationsService extends RemoteService {
    
    /**
     * 取得全部資料
     * We need this here for GWT to optimize. Don't change this unless the
     * parameters or return value changes!
     * 
     * @gwt.typeArgs <java.lang.String,edu.berkeley.cs169.server.graph.Node>
     */
    public Map getLocations();
    public List getLocations(String prefix, int limit);
}
