package tw.kayjean.ui.server;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.xerox.amazonws.sdb.Domain;
import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.QueryWithAttributesResult;
import com.xerox.amazonws.sdb.SimpleDB;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.QueueService;

import org.jets3t.service.S3Service;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.utils.ServiceUtils;

import com.thoughtworks.xstream.XStream;

import tw.kayjean.ui.client.model.FBDetail;
import tw.kayjean.ui.client.model.FBFriends;
import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.model.Poi;
import tw.kayjean.ui.client.rpc.CoordinateService;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CoordinateServiceImpl extends RemoteServiceServlet implements CoordinateService {

    public void init() throws ServletException {
    	System.out.println("CoordinateServiceImpl init");
    	
        super.init();
        preparedb( "poi" );
        
        System.out.println("CoordinateServiceImpl end");
    }
	
	//db相關, POI DB
	public SimpleDB sds = null;
	public Domain dom = null;
	
	S3Service s3Service = null;
	S3Bucket testBucket1 = null;
	S3Bucket testBucket2 = null;
	S3Bucket testBucket4 = null;
	
	public QueueService qs = null;
	public MessageQueue msgQueue = null;
	
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
                
				if( qs == null ){
					qs = new QueueService( "" , "" );
					msgQueue = qs.getOrCreateMessageQueue("servicerecordfriends");
				}
			}
			catch( Exception e ){
				System.out.println( e.toString() );
			}
		}

		if( testBucket1 == null || testBucket2 == null || testBucket4 == null ){
			try {
				AWSCredentials awsCredentials = new AWSCredentials("" , "");
				s3Service = new RestS3Service(awsCredentials);
				//儲存某個geocell內容
				testBucket1 = s3Service.getOrCreateBucket("xmlgeodata-kayjean");
				//儲存個人資料..這個方法怪怪的,不過暫時有效
				testBucket2 = s3Service.getOrCreateBucket("xmlservertemp-kayjean");
				//景點詳細資料
				testBucket4 = s3Service.getOrCreateBucket("upload-kayjean");
			}
			catch( Exception e ){
				System.out.println( e.toString() );
			}
		}
		return true;
	}

	// 可以開始選擇項目
	// 感受系統問題
	// 主要目的是
	// 1.找出並且列出所有地方
	// 2.使用者可以用不同ranking紀錄曾經去過的項目
	// 3.推薦可以去的地方(包括顯示詳細資料)
	public List getRTree(String username, String name , int type ) {
		System.out.println("CoordinateServiceImpl getRTree " + username
				+ " and " + name + " and " + type );
		ArrayList retNodes = new ArrayList();
		XStream xstream = new XStream();
		boolean awsexist = false;
		S3Object objectComplete2 = null;

		if( type == 0 ){
		
		
		// 基本地圖功能
		try {
			objectComplete2 = s3Service.getObject(testBucket1, name);
			awsexist = true;
		} catch (Exception e) {
		}

		if (awsexist == false) {

			if (dom == null) {
				System.out.println("error very important");
			} else {
				String qattr = "l" + Integer.toString(name.length());
				String line = "select * from poi where " + qattr + " = \""
						+ name
						+ "\" and rank is not null order by rank desc limit 10";
				int itemCount = 0;
				long start = System.currentTimeMillis();
				String nextToken = null;
				try {
					do {
						QueryWithAttributesResult qwar = dom.selectItems(line,
								nextToken);
						Map<String, List<ItemAttribute>> items = qwar
								.getItems();
						for (String id : items.keySet()) {
							System.out.println("Item : " + id);
							Node n = new Node();
							n.fullname = id;
							n.type = 0;
							for (ItemAttribute attr : items.get(id)) {
								if (attr.getName().equalsIgnoreCase("lon"))
									n.x = Double.parseDouble(attr
											.getValue());
								else if (attr.getName().equalsIgnoreCase("lat"))
									n.y = Double.parseDouble(attr
											.getValue());
								else if (attr.getName().equalsIgnoreCase(
										"targetname"))
									n.name = attr.getValue();
								else if (attr.getName().equalsIgnoreCase("l10"))
									n.geocell = attr.getValue();
								else if (attr.getName()
										.equalsIgnoreCase("rank"))
									n.rank = Integer.parseInt(attr
											.getValue());
							}
							itemCount++;
							retNodes.add(n);
						}
						nextToken = qwar.getNextToken();
						System.out.println("Box Usage :" + qwar.getBoxUsage());
					} while (nextToken != null && !nextToken.trim().equals("")
							&& itemCount < 10);
				} catch (Exception e) {
					System.out.println(e);
				}
				long end = System.currentTimeMillis();
				System.out.println("Time : " + ((int) (end - start) / 1000.0));
				System.out.println("Number of items returned : " + itemCount);
			}

			try {
				// 將東西變成一個XML
				String s = xstream.toXML(retNodes);
				// 將XML存進S3裡面
				S3Object stringObject = new S3Object(name, s);
				s3Service.putObject(testBucket1, stringObject);
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			// 使用上次內容
			try {
				retNodes.addAll((List<Node>) xstream.fromXML(ServiceUtils
						.readInputStreamToString(objectComplete2
								.getDataInputStream(), "UTF-8")));
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		}

		// 讀取client相關資料
		if (username != null && !username.equalsIgnoreCase("")) {

			readdatainsertintocache( username );
			//當作沒有處理過,重新來過
			List<Node> l = MemCache.getcache(username);

			if (l != null && l.size() > 0 ) {
				for (int i = 0; i < l.size(); i++) {
					Node n = l.get(i);
					if (n.geocell != null && n.geocell.startsWith(name)) {
						// 準備加入
						if( type == 0 ){
							//我開頭的全部刪除
							if( n.fid.equalsIgnoreCase(username) ){
								//要有互斥運作
								for (int j = retNodes.size() - 1; j >= 0; j--) {
									Node n2 = (Node) retNodes.get(j);
									if (n2.name.equalsIgnoreCase(n.name)) {
										// 移除地圖,保留個人設定
										retNodes.remove(j);
										break;
									}
								}
							}
						}
						if( type == 1 ){
							//我結尾的全部加入
							retNodes.add(n);
						}
					}
				}
			}
		}
		return retNodes;
	}
	
	public void readdatainsertintocache( String username ){
		// 讀取區域檔案,存入cache
		List<Node> l = MemCache.getcache(username);
		if (l == null) {
			//我傳送給別人的項目
			S3Object objectComplete = null;
			boolean awsexist = false;
			try {
				objectComplete = s3Service.getObject(testBucket2, username);
				awsexist = true;
			} catch (Exception e) {
			}
			if (awsexist == true) {
				//舊使用者
				try {
					XStream xstream = new XStream();
					l = (List<Node>) xstream.fromXML(ServiceUtils
							.readInputStreamToString(objectComplete.getDataInputStream(), "UTF-8"));
					
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			
			//別人傳送給我的項目
			awsexist = false;
			try {
				objectComplete = s3Service.getObject(testBucket2, username + "_r" );
				awsexist = true;
			} catch (Exception e) {
			}
			if (awsexist == true) {
				//舊使用者
				try {
					XStream xstream = new XStream();
					l.addAll( (List<Node>) xstream.fromXML(ServiceUtils
							.readInputStreamToString(objectComplete.getDataInputStream(), "UTF-8")) );
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			
			if( l != null && l.size() > 0 ){
				MemCache.addcache(username, l);
			}
			else
			{
				//就算沒有原本設定,還是要產生一個MemCache資料,避免之後每次都要讀取S3
				ArrayList retNodes2 = new ArrayList();
				MemCache.addcache(username, retNodes2);
			}
		}
	}

	public Integer sendNode(Node n) {
		// 寫入興趣點透過memorycache
		readdatainsertintocache( n.fid );
		MemCache.addcacheitem( n.fid , n);
		return 1;
	}

	public Poi getNode(String s){
		//輸入一個景點,找出詳細描述內容,顯示在BROWSER上
		boolean awsexist = false;
		S3Object objectComplete2 = null;
        try {
        	objectComplete2 = s3Service.getObject(testBucket4, s );
        	awsexist = true;
        } catch ( Exception e) {
        }
        if( awsexist == true )
        {
        	try{
        		XStream xstream = new XStream();
        		return (Poi)xstream.fromXML(
        				ServiceUtils.readInputStreamToString
        				(objectComplete2.getDataInputStream(), "UTF-8")
        				);
			}
			catch(Exception e ){
				System.out.println( e );
			}
        }
		return null;
	}
	
	
	public FBFriends sendDetail(FBDetail fd , FBFriends ffs) {
		boolean awsexist = false;

		//id_d
		S3Object A_d_S3Object = null;
		awsexist = false;
		try {
			A_d_S3Object = s3Service.getObject(testBucket2, fd.id + "_d" );
			awsexist = true;
		} catch (Exception e) {
		}
		if (awsexist == true) {
			//舊使用者,讀入資料,更新
			try {
				XStream A_d_xstream = new XStream();
				FBDetail A_d = (FBDetail) A_d_xstream
					.fromXML(ServiceUtils.readInputStreamToString(
							A_d_S3Object.getDataInputStream(), "UTF-8"));
				if( A_d.updated_time.equalsIgnoreCase("") ){
					// 將東西變成一個XML
					XStream xstream = new XStream();
					String s = xstream.toXML(fd);
					//將XML存進S3裡面
					S3Object A_d_S3Object_t = new S3Object(fd.id + "_d", s);
					s3Service.putObject(testBucket2, A_d_S3Object_t);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		else{
			//新使用者
			try {
				// 將東西變成一個XML
				XStream xstream = new XStream();
				String s = xstream.toXML(fd);
				//將XML存進S3裡面
				S3Object A_d_S3Object_t = new S3Object(fd.id + "_d", s);
				s3Service.putObject(testBucket2, A_d_S3Object_t);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		//id_f
		S3Object A_f_S3Object = null;
		awsexist = false;
		FBFriends A_f = null;
		try {
			A_f_S3Object = s3Service
					.getObject(testBucket2, fd.id + "_f" );
			awsexist = true;
		} catch (Exception e) {
		}
		if (awsexist == true) {
			//舊使用者,讀入資料,更新
			try {
				XStream A_f_xstream = new XStream();
				A_f = (FBFriends) A_f_xstream
					.fromXML(ServiceUtils.readInputStreamToString(
							A_f_S3Object.getDataInputStream(), "UTF-8"));
				if( A_f.items.size() < ffs.items.size() ){
					// 將東西變成一個XML
					//以A_f為主,沒見過的人,通通加進來
					for( int i = 0 ; i < ffs.items.size() ; i++ ){
						int i2 = 0;
						for (; i2 < A_f.items.size(); i2++) {
							if (A_f.items.get(i2).id.equalsIgnoreCase( ffs.items.get(i).id )) {
								break;
							}
						}
						if (i2 < A_f.items.size()) {
							// 中途跳出,表示有符合過的
						} else {
							// 加入資料
							A_f.items.add(ffs.items.get(i));
						}
					}
					XStream xstream = new XStream();
					String s = xstream.toXML(A_f);
					//將XML存進S3裡面
					S3Object A_f_S3Object_t = new S3Object(fd.id + "_f", s);
					s3Service.putObject(testBucket2, A_f_S3Object_t);
				}
					
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		else{
			//新使用者
			try {
				// 將東西變成一個XML
				XStream xstream = new XStream();
				String s = xstream.toXML(ffs);
				//將XML存進S3裡面
				S3Object A_f_S3Object_t = new S3Object(fd.id + "_f", s);
				s3Service.putObject(testBucket2, A_f_S3Object_t);
				//會有process為每個朋友建立基本檔案,進行id2friends時可以增加項目
				String msgId = msgQueue.sendMessage(fd.id);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		//id_r
		S3Object A_r_S3Object = null;
		awsexist = false;
		try {
			A_r_S3Object = s3Service.getObject(testBucket2, fd.id + "_r" );
			awsexist = true;
		} catch (Exception e) {
		}
		if (awsexist == true) {
		}
		else{
			//新使用者
			try {
				XStream B_r_xstream = new XStream();
				ArrayList retNodes = new ArrayList();
				//Node n = new Node();
				//retNodes.add(n);
				String B_r_t = B_r_xstream.toXML(retNodes);
				S3Object B_r_S3Object_t = new S3Object( fd.id + "_r" , B_r_t );
				s3Service.putObject(testBucket2, B_r_S3Object_t );
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		return A_f;
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
		//照理說會回傳經緯度,移動到類似地點

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

/*	
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
                    		avgNode2.x = Double.parseDouble(attr.getValue());
                    	else if( attr.getName().equalsIgnoreCase("lat") )
                    		avgNode2.y = Double.parseDouble(attr.getValue());
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
*/
	
}
