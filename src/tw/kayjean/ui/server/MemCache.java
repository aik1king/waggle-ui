package tw.kayjean.ui.server;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.QueueService;

import tw.kayjean.ui.client.model.Node;

public class MemCache {

	private static Hashtable<String, CacheData> cacheList= new Hashtable<String, CacheData>();
	static S3Service s3Service = null;
	static S3Bucket testBucket2 = null;
	static QueueService qs = null;
	static XStream xstream = new XStream();

	public MemCache()
	{
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
	
	//第一次加入資料,可能是從FILE,也可能是個空的EMPTY
	public static boolean addcache( String data , List<Node> d ){
		CacheData ci = cacheList.get(data);
		if (null == ci){
			ci = new CacheData( d ); 
			cacheList.put(data, ci );
			return (true);
		}
		ci.additem( d );
		ci.ischange( false );
		return (true);
	}
	
	//新增加進來的
	public static boolean addcacheitem( String data , Node d ){
		CacheData ci = cacheList.get(data);
		if (null == ci){
			//不太可能啦
			return (false);
		}
		ci.additem( d );
		ci.ischange( true );
		ci.resetcount();
		return (true);
	}

	public static void showdata() {
		// 輪流顯示全部內容
		try {
			System.out.println( "目前cache大小為" + cacheList.size() );
			ArrayList removesets = new ArrayList();
			for(Iterator i = cacheList.entrySet().iterator(); i.hasNext(); )
			{
				//先不管時間問題
				Map.Entry e = (Map.Entry)i.next();
				String username = e.getKey().toString();
				List<Node> d = ((CacheData)e.getValue()).getdata();
				

				if( ((CacheData)e.getValue()).needDiscard() ){
					System.out.println( username + "停留很久,沒有變動,準備刪除" );
					removesets.add(username);
				}
				else if( ((CacheData)e.getValue()).needSave() ){
					System.out.println( username + "停留很久,有變動,準備儲存入檔案系統中並且刪除" );
					
					//type == 1的部份,進行存檔
					ArrayList type1 = new ArrayList();
					for (int j = 0 ; j < d.size(); j++) {
						if( ((Node)d.get(j)).type == 1 )
							type1.add( (Node)d.get(j) );
					}
					if( type1.size() > 0 ){
						String s = xstream.toXML(type1);
						S3Object stringObject = new S3Object(username, s);
						if( testBucket2 == null ){
							try {
								AWSCredentials awsCredentials = new AWSCredentials("" , "");
								s3Service = new RestS3Service(awsCredentials);
								//儲存個人資料..這個方法怪怪的,不過暫時有效
								testBucket2 = s3Service.getOrCreateBucket("xmlservertemp-kayjean");
							}
							catch( Exception ee ){
								System.out.println( ee.toString() );
							}
						}
						s3Service.putObject(testBucket2, stringObject);
					}
					
					//提供給別人項目,用queue形式,某個人一整批存入同一個人,因為可以存入很長資料
					if( qs == null ){
						qs = new QueueService( "" , "" );
					}
					MessageQueue msgQueue = qs.getOrCreateMessageQueue("serviceid2friends");
					String msgId = msgQueue.sendMessage(username);

/*					
					//type == 2的部份,進行存檔
					ArrayList type2 = new ArrayList();
					for (int j = 0 ; j < d.size(); j++) {
						if( ((Node)d.get(j)).type == 2 )
							type2.add( (Node)d.get(j) );
					}
					if( type2.size() > 0 ){
						String s = xstream.toXML(type2);
						S3Object stringObject = new S3Object(username + "_s" , s);
						if( testBucket2 == null ){
							try {
								AWSCredentials awsCredentials = new AWSCredentials("" , "");
								s3Service = new RestS3Service(awsCredentials);
								//儲存個人資料..這個方法怪怪的,不過暫時有效
								testBucket2 = s3Service.getOrCreateBucket("xmlservertemp-kayjean");
							}
							catch( Exception ee ){
								System.out.println( ee.toString() );
							}
						}
						s3Service.putObject(testBucket2, stringObject);
					}
*/
					
					//type == 4的部份,進行存檔
					ArrayList type4 = new ArrayList();
					for (int j = 0 ; j < d.size(); j++) {
						if( ((Node)d.get(j)).type == 2 )
							type4.add( (Node)d.get(j) );
					}
					if( type4.size() > 0 ){
						String s = xstream.toXML(type4);
						S3Object stringObject = new S3Object(username + "_w" , s);
						if( testBucket2 == null ){
							try {
								AWSCredentials awsCredentials = new AWSCredentials("" , "");
								s3Service = new RestS3Service(awsCredentials);
								//儲存個人資料..這個方法怪怪的,不過暫時有效
								testBucket2 = s3Service.getOrCreateBucket("xmlservertemp-kayjean");
							}
							catch( Exception ee ){
								System.out.println( ee.toString() );
							}
						}
						s3Service.putObject(testBucket2, stringObject);
					}

					
					//準備刪除掉這個項目
					removesets.add(username);
				}
				else
				{
					System.out.println( username + "還在運作中,數值加1" + ((CacheData)e.getValue()).getUnChangeCount()  );
					((CacheData)e.getValue()).incUnchange();					
				}
			}
			
			//刪除memcache內容
			for (int j = removesets.size() - 1; j >= 0; j--) {
				System.out.println( "進行刪除" + removesets.get(j) );
				cacheList.remove( removesets.get(j) );
			}
			
			System.out.println( "目前cache大小為" + cacheList.size() );
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
