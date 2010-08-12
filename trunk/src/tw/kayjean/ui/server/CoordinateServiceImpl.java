package tw.kayjean.ui.server;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.rpc.CoordinateService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CoordinateServiceImpl extends RemoteServiceServlet implements CoordinateService {

	public List getRTree(double w, double e, double s, double n) {
		ArrayList avgNodes = new ArrayList();
		
		Node avgNode = new Node();
		avgNode.name = "thisis1";
		avgNode.y = 121.0;
		avgNode.x = 24.5;
		avgNodes.add(avgNode);

		Node avgNode2 = new Node();
		avgNode2.name = "thisis1";
		avgNode2.y = 122.0;
		avgNode2.x = 25.5;
		avgNodes.add(avgNode2);

		return avgNodes;
	}

	public Node getNode(String s){
		return null;
	}
	
	
	static double ip2number(String ip) {
		StringTokenizer st = new StringTokenizer(ip, ".");
		int p = 0;
		double x1 = 0, x2 = 0, x3 = 0, x4 = 0;
		while (st.hasMoreTokens()) {
			double i = Double.parseDouble(st.nextToken());
			if (p == 0) {
				x1 = i * (256 * 256 * 256);
			} else if (p == 1) {
				x2 = i * (256 * 256);
			} else if (p == 2) {
				x3 = i * (256);
			} else if (p == 3) {
				x4 = i;
			}
			p++;
		}
		return x1 + x2 + x3 + x4;
	}
	
	public String getIPLocation() {

/*		
		try{
			String ip = getThreadLocalRequest().getRemoteHost();
	//		ip = "218.160.227.220";
			double d = ip2number(ip);
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			
			Query queryobj = pm.newQuery(Ip2LonLat.class);
			queryobj.setFilter("IPFrom < keywordParam");
			queryobj.declareParameters("Double keywordParam");
			queryobj.setOrdering("IPFrom desc");
			queryobj.setRange(0, 10);
	
	//		Query queryobj = pm.newQuery(Ip2LonLat.class, "IPFrom < 3410338903");
			
			Double ipto = Double.MIN_VALUE;
			String r = null;
			try {
				List<Ip2LonLat> results = (List<Ip2LonLat>) queryobj.execute( d );
				for (Ip2LonLat e : results) {
					ipto = e.getIPTo();
					if( ipto > d )
						r = e.getLonLat();
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				queryobj.closeAll();
				pm.close();
			}
			// String sql = "select country_name from ip2country where " +
			// Double.toString(d) + " >= ip_from and " + Double.toString(d) +
			// " <= ip_to";
	 
			return r;

		}
		catch(Exception e){
			//return "DEFAULT-24.5-121.0";
			return "";
		}
*/
		return "";
	}
	
}
