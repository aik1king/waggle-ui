package tw.kayjean.ui.server;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TimeZone;
import java.io.FileReader;
import java.io.FileWriter;

import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.xerox.amazonws.sdb.Domain;
import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.QueryWithAttributesResult;
import com.xerox.amazonws.sdb.SimpleDB;

import org.jets3t.service.S3Service;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.utils.ServiceUtils;

import com.thoughtworks.xstream.XStream;

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
	
	S3Service s3Service = null;
	S3Bucket testBucket1 = null;
	S3Bucket testBucket2 = null;
	
	public boolean preparedb( String dbname ) {
		if (sds == null) {
			try {
				//http://code.google.com/p/typica/wiki/TypicaSampleCode
                //sds = SamplesUtils.loadASWDB(); 
                sds = new SimpleDB("" , "" , false);
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

		if( testBucket1 == null || testBucket2 == null ){
			try {
				AWSCredentials awsCredentials = new AWSCredentials("" , "");
				s3Service = new RestS3Service(awsCredentials);
				testBucket1 = s3Service.getOrCreateBucket("xmlgeodata-kayjean");
				testBucket2 = s3Service.getOrCreateBucket("xmlservertemp-kayjean");
			}
			catch( Exception e ){
				System.out.println( e.toString() );
			}
		}

		return true;
	}

	public List getRTree( String username , String name ) {
		ArrayList avgNodes = new ArrayList();
		XStream xstream = new XStream();
		//讀取一班資料
		boolean awsexist = false;
		S3Object objectComplete2 = null;
        try {
        	objectComplete2 = s3Service.getObject(testBucket1, name);
        	awsexist = true;
        } catch ( Exception e) {
        }
        if( awsexist == false ){
	    	
			Node avgNode = new Node();
			avgNode.name = "thisis1";
			avgNode.y = 121.0;
			avgNode.x = 24.5;
			avgNode.type = 0;
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
	            		
	            		avgNode2.type = 0;
	                    for (ItemAttribute attr : items.get(id)) {
	                        //System.out.println("  "+attr.getName()+" = "+filter(attr.getValue()));
	                    	if( attr.getName().equalsIgnoreCase("lon") )
	                    		avgNode2.y = Double.parseDouble(attr.getValue());
	                    	else if( attr.getName().equalsIgnoreCase("lat") )
	                    		avgNode2.x = Double.parseDouble(attr.getValue());
	                    	else if( attr.getName().equalsIgnoreCase("targetname") )
	                    		avgNode2.name = attr.getValue();
	                    	else if( attr.getName().equalsIgnoreCase("l10") )
	                    		avgNode2.geocell = attr.getValue();
	                    }
	                    itemCount++;
	            		avgNodes.add(avgNode2);
	                } 
	                nextToken = qwar.getNextToken(); 
	                System.out.println("Box Usage :"+qwar.getBoxUsage()); 
	            } while (nextToken != null && !nextToken.trim().equals("") && itemCount < 10 ); 
	        }
	        catch(Exception e){
	        	System.out.println( e);
	        }
	        long end = System.currentTimeMillis(); 
	        System.out.println("Time : "+((int)(end-start)/1000.0)); 
	        System.out.println("Number of items returned : "+itemCount); 
			}
	
			
			try{
				//將東西變成一個XML
				String s = xstream.toXML(avgNodes);
				//將XML存進S3裡面
				S3Object stringObject = new S3Object( name , s );
				s3Service.putObject(testBucket1, stringObject);
			}
			catch(Exception e ){
				System.out.println( e );
			}
        }
        else
        {
        	//使用上次內容
        	try{
        		avgNodes.addAll( (List<Node>)xstream.fromXML( ServiceUtils.readInputStreamToString(objectComplete2.getDataInputStream(), "UTF-8") ) );
			}
			catch(Exception e ){
				System.out.println( e );
			}
        }
        
		//讀取client相關資料
        if( username != null && !username.equalsIgnoreCase("" ) ){
        try {
        	objectComplete2 = s3Service.getObject(testBucket2, username );
        	awsexist = true;
        } catch ( Exception e) {
        }
        if( awsexist == false ){
        	//如果沒有內容,完全不需要處理
        }
        else
        {
        	//讀取先前內容
        	try{
        		//應該是讀取後,依照name過濾後才加入
        		//目前先用最笨方法
        		List<Node> l = null;
        		l = (List<Node>)xstream.fromXML( ServiceUtils.readInputStreamToString(objectComplete2.getDataInputStream(), "UTF-8") );
        		for( int i = 0 ; i < l.size() ; i++ ){
        			Node n = l.get(i);
        			double t = n.x;
        			n.x = n.y;
        			n.y = t;
        			if( n.geocell.startsWith(name)){
        				//準備加入,但最好可以簡化
        				//Collections.sort
        				for( int j = 0 ; j < avgNodes.size() ; j++ ){
        					Node n2 = (Node)avgNodes.get(j);
        					if( n2.name.equalsIgnoreCase(n.name )){
        						//移除地圖,保留個人設定
        						avgNodes.remove(j);
        						break;
        					}
        				}
        				avgNodes.add(n);
        			}
        		}
			}
			catch(Exception e ){
				System.out.println( e );
			}
        }
        }
		return avgNodes;
	}

	public Node getNode(String s){
		return null;
	}
	
	public List getLocations(String name, int limit){
		//輸入一些文字,找出適當內容
		ArrayList avgNodes = new ArrayList();

		if( dom == null ){
			System.out.println( "error" );
		}
		else{
		String qattr = "k" + Integer.toString(name.length());

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
            		
                    for (ItemAttribute attr : items.get(id)) {
                        //System.out.println("  "+attr.getName()+" = "+filter(attr.getValue()));
                    	if( attr.getName().equalsIgnoreCase("lon") )
                    		avgNode2.y = Double.parseDouble(attr.getValue());
                    	else if( attr.getName().equalsIgnoreCase("lat") )
                    		avgNode2.x = Double.parseDouble(attr.getValue());
                    	else if( attr.getName().equalsIgnoreCase("targetname") )
                    		avgNode2.name = attr.getValue();
                    } 
                    itemCount++;
            		avgNodes.add(avgNode2);
                } 
                nextToken = qwar.getNextToken(); 
                System.out.println("Box Usage :"+qwar.getBoxUsage()); 
            } while (nextToken != null && !nextToken.trim().equals("") && itemCount < 10 ); 
        }
        catch(Exception e){
        	System.out.println( e);
        }
        long end = System.currentTimeMillis(); 
        System.out.println("Time : "+((int)(end-start)/1000.0)); 
        System.out.println("Number of items returned : "+itemCount); 
		}
		
		return avgNodes;
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
	
	public Integer sendNode( String username , int type , String name , double x , double y , String geocell ){
		Node n = new Node();
		n.type = type;
		n.name = name;
		n.x = x;
		n.y = y;
		n.geocell = geocell;
		
		if( type == 1 ){
			//寫入興趣點
			XStream xstream = new XStream();
			boolean awsexist = false;
			S3Object objectComplete2 = null;
	        try {
	        	objectComplete2 = s3Service.getObject(testBucket2, username);
	        	awsexist = true;
	        } catch ( Exception e) {
	        }
	        if( awsexist == false ){
				try{
					ArrayList avgNodes = new ArrayList();
					avgNodes.add(n);
					
					//將東西變成一個XML
					String s = xstream.toXML(avgNodes);
					//將XML存進S3裡面
					S3Object stringObject = new S3Object( username , s );
					s3Service.putObject(testBucket2, stringObject);
				}
				catch(Exception e ){
					System.out.println( e );
				}
	        }
	        else
	        {
	        	//使用上次內容
	        	try{
	        		List<Node> avgNodes = null;
	        		avgNodes = (List<Node>)xstream.fromXML( ServiceUtils.readInputStreamToString(objectComplete2.getDataInputStream(), "UTF-8") );
	        		avgNodes.add( n );
	        		
					//將東西變成一個XML
					String s = xstream.toXML(avgNodes);
					//將XML存進S3裡面
					S3Object stringObject = new S3Object( username , s );
					s3Service.putObject(testBucket2, stringObject);
				}
				catch(Exception e ){
					System.out.println( e );
				}
	        }
		}
		return 1;
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
