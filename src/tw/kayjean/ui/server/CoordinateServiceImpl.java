package tw.kayjean.ui.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.xerox.amazonws.sdb.Domain;
import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.QueryWithAttributesResult;
import com.xerox.amazonws.sdb.SimpleDB;

import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.rpc.CoordinateService;


/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CoordinateServiceImpl extends RemoteServiceServlet implements CoordinateService {

    public void init() throws ServletException {
        super.init();
        preparedb( "poi" );
    }
	
	//db相關, POI DB
	public SimpleDB sds = null;
	public Domain dom = null;
	public boolean preparedb( String dbname ) {
		if (sds == null) {
			try {
				//http://code.google.com/p/typica/wiki/TypicaSampleCode
                //sds = SamplesUtils.loadASWDB(); 
                sds = new SimpleDB("AKIAJHOKOT2THLYRLS3A" , "FnAdaK7zEjbVgHweS1FMM28VFljLe0u8mzi7G0eI" , false);
                sds.setSignatureVersion(1);
                
                /*
                ListDomainsResult result = sds.listDomains(); 
                List<Domain> domains = result.getDomainList(); 
                for (Domain d : domains) { 
                        System.out.println(d.getName()); 
                } 
                */

                dom = sds.getDomain(dbname);
                if (dom == null) {
                	//根本不會跑到這裡
                    System.out.println("domain must be set! we create this time");
                    sds.createDomain(dbname);
                    return false;
                }
			}
			catch( Exception e ){
				System.out.println( e.toString() );
			}
		}
		return true;
	}

	public List getRTree( String name ) {
		ArrayList avgNodes = new ArrayList();

		Node avgNode = new Node();
		avgNode.name = "thisis1";
		avgNode.y = 121.0;
		avgNode.x = 24.5;
		avgNodes.add(avgNode);
		
		//依照名稱到 s3中尋找需要檔案
		//如果有就將資料填入,並且返回
		//如果沒有就送入message queue
		//或是直接到db中查詢,查詢結果製作成檔案並且回傳
		
		//先直接查詢db

		if( dom == null ){
			System.out.println( "error" );
		}
		else{
		String qattr = "l" + Integer.toString(name.length());

		String line = "select * from poi where " + qattr + " = \"" + name + "\" and rank is not null order by rank desc limit 10";
        int itemCount = 0;
        long start = System.currentTimeMillis(); 
        String nextToken = null;
        try{
            do {
                QueryWithAttributesResult qwar = dom.selectItems(line, nextToken);
                Map<String, List<ItemAttribute>> items = qwar.getItems();
                for (String id : items.keySet()) { 
                	System.out.println("Item : "+id);

            		Node avgNode2 = new Node();
            		avgNode2.name = id;
                    for (ItemAttribute attr : items.get(id)) {
                        //System.out.println("  "+attr.getName()+" = "+filter(attr.getValue()));
                    	if( attr.getName().equalsIgnoreCase("lon") )
                    		avgNode2.y = Double.parseDouble(attr.getValue());
                    	else if( attr.getName().equalsIgnoreCase("lat") )
                    		avgNode2.x = Double.parseDouble(attr.getValue());
                    } 
            		avgNodes.add(avgNode2);
                } 
                nextToken = qwar.getNextToken(); 
                System.out.println("Box Usage :"+qwar.getBoxUsage()); 
            } while (nextToken != null && !nextToken.trim().equals("") ); 
        }
        catch(Exception e){
        	System.out.println( e);
        }
        long end = System.currentTimeMillis(); 
        System.out.println("Time : "+((int)(end-start)/1000.0)); 
        System.out.println("Number of items returned : "+itemCount); 
		}

/*        
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
*/
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
