package tw.kayjean.ui.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import tw.kayjean.ui.client.model.Node;

public class MemCache {

	private static Hashtable<String, CacheData> cacheList= new Hashtable<String, CacheData>();

	public MemCache()
	{
//		cacheList = new Hashtable<String, CacheData>();
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

	public static void showdata(){
		//輪流顯示全部內容
		try{
		Collection c = cacheList.values();
		Iterator itr = c.iterator();
		// iterate through HashMap values iterator
		while (itr.hasNext()) {
			List<Node> d = ((CacheData)itr.next()).getdata();
			for( int j = 0 ; j < d.size() ; j++ ){
				System.out.println("Last Item : "+((Node)d.get(j)).name );
			}
		}
		}
		catch(Exception e )
		{
			System.out.println(e);
		}
	}
}
