package tw.kayjean.ui.server;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jets3t.service.S3Service;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import com.thoughtworks.xstream.XStream;

import tw.kayjean.ui.client.model.Node;

public class MemCache {

	private static Hashtable<String, CacheData> cacheList= new Hashtable<String, CacheData>();
	static S3Service s3Service = null;
	static S3Bucket testBucket2 = null;
	static XStream xstream = new XStream();

	public MemCache()
	{
		if( testBucket2 == null ){
			try {
				AWSCredentials awsCredentials = new AWSCredentials("AKIAJHOKOT2THLYRLS3A" , "FnAdaK7zEjbVgHweS1FMM28VFljLe0u8mzi7G0eI");
				s3Service = new RestS3Service(awsCredentials);
				//儲存個人資料..這個方法怪怪的,不過暫時有效
				testBucket2 = s3Service.getOrCreateBucket("xmlservertemp-kayjean");
			}
			catch( Exception e ){
				System.out.println( e.toString() );
			}
		}
	}

	
	public static List<Node> getcache( String data ){
		CacheData ci = cacheList.get(data);
		if (null == ci){
			return null;
		}
		else{
			return ci.getdata();
		}
	}
	
	public static boolean addcache( String data , List<Node> d ){
		CacheData ci = cacheList.get(data);
		if (null == ci){
			ci = new CacheData( d ); 
			cacheList.put(data, ci );
			return (true);
		}
		ci.additem( d );
		return (true);
	}
	
	public static boolean addcacheitem( String data , Node d ){
		CacheData ci = cacheList.get(data);
		if (null == ci){
			return (false);
		}
		ci.additem( d );
		return (true);
	}

	public static void showdata() {
		// 輪流顯示全部內容
		try {
			
			for(Iterator i = cacheList.entrySet().iterator(); i.hasNext(); )
			{
				//先不管時間問題
				Map.Entry e = (Map.Entry)i.next();
				String username = e.getKey().toString();
				List<Node> d = ((CacheData)e.getValue()).getdata();
				for (int j = 0; j < d.size(); j++) {
					System.out.println("Last Item : " + ((Node) d.get(j)).name);
				}

				if( d.size() > 0 ){
					// 將東西變成一個XML
					String s = xstream.toXML(d);
					//將XML存進S3裡面,直接蓋入
					S3Object stringObject = new S3Object(username, s);
					s3Service.putObject(testBucket2, stringObject);
				}

				//提供給別人項目,用queue形式,某個人一整批存入同一個人,因為可以存入很長資料
			}
			
			
/*			
			Collection c = cacheList.values();
			Iterator itr = c.iterator();
			// iterate through HashMap values iterator
			while (itr.hasNext()) {

				//對於每一個使用者
				List<Node> d = ((CacheData) itr.next()).getdata();
				for (int j = 0; j < d.size(); j++) {
					System.out.println("Last Item : " + ((Node) d.get(j)).name);
				}
			}
			
				//如果沒有

				// 寫入興趣點透過檔案,因為是直接寫入,直接蓋入
				XStream xstream = new XStream();
				boolean awsexist = false;
				S3Object objectComplete2 = null;
				try {
					objectComplete2 = s3Service.getObject(testBucket2, username);
					awsexist = true;
				} catch (Exception e) {
				}
				if (awsexist == false) {
					try {
						ArrayList retNodes = new ArrayList();
						retNodes.add(d);
						// 將東西變成一個XML
						String s = xstream.toXML(retNodes);
						// 將XML存進S3裡面
						S3Object stringObject = new S3Object(username, s);
						s3Service.putObject(testBucket2, stringObject);
					} catch (Exception e) {
						System.out.println(e);
					}
				} else {
					// 使用上次內容
					try {
						List<Node> retNodes = null;
						retNodes = (List<Node>) xstream.fromXML(ServiceUtils
							.readInputStreamToString(objectComplete2
									.getDataInputStream(), "UTF-8"));
						retNodes.addAll(d);
						// 將東西變成一個XML
						String s = xstream.toXML(retNodes);
						// 將XML存進S3裡面
						S3Object stringObject = new S3Object(username, s);
						s3Service.putObject(testBucket2, stringObject);
					} catch (Exception e) {
						System.out.println(e);
					}
				}
*/				
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
