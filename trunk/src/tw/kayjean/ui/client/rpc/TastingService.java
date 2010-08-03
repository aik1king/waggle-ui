package tw.kayjean.ui.client.rpc;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("taste")
public interface TastingService extends RemoteService {

    /**
     * 
     * We need this here for GWT to optimize. Don't change this unless the
     * parameters or return value changes!
     * 
     * @gwt.typeArgs locations <java.lang.String>
     * @gwt.typeArgs stopovers <edu.berkeley.cs169.client.helpers.StopoversSet>
     * @gwt.typeArgs options <java.lang.String,java.lang.Boolean>
     * @gwt.typeArgs <edu.berkeley.cs169.server.graph.Path>
     */
    public List getTaste(List locations);
}