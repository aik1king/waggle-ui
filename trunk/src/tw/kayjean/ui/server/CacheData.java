package tw.kayjean.ui.server;

import java.util.List;

import tw.kayjean.ui.client.model.Node;

public class CacheData {

	private long cacheLifetime;
	private List<Node> data;
	
	public CacheData( List<Node> d ){
		this.data = d;
	}
	
	public List<Node> getdata(){
		return data;
	}

	public void additem( Node d ){
		data.add(d);
	}
}
