package tw.kayjean.ui.server;

import java.util.Hashtable;
import java.util.List;

import tw.kayjean.ui.client.model.Node;

public class MemCache {

	private static Hashtable<String, CacheData> cacheList;

	public MemCache()
	{
		cacheList = new Hashtable<String, CacheData>();
	}

	
	public List<Node> getcache( String data ){
		CacheData ci = cacheList.get(data);
		if (null == ci){
			return null;
		}
		else{
			return ci.getdata();
		}
	}
	
	public boolean addcache( String data , List<Node> d ){
		CacheData ci = cacheList.get(data);
		if (null == ci){
			ci = new CacheData( d ); 
			cacheList.put(data, ci );
		}
		return (true);
	}
	
	public boolean addcacheitem( String data , Node d ){
		CacheData ci = cacheList.get(data);
		if (null == ci){
			return (false);
		}
		ci.additem( d );
		return (true);
	}
	
}
