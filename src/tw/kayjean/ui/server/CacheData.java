package tw.kayjean.ui.server;

import java.util.List;

import tw.kayjean.ui.client.model.Node;

public class CacheData {

	//private long cacheLifetime;
	//沒有變動次數超過三次,就存入檔案,清除自己
	private List<Node> data;
	private int UnChangeCount;
	private boolean IsChange;
	
	public CacheData( List<Node> d ){
		this.data = d;
		this.UnChangeCount = 0;
		this.IsChange = false;
	}
	
	public void ischange( boolean change ){
		this.IsChange = change;
	}
	
	public int getUnChangeCount(){
		return this.UnChangeCount;
	}

	public void resetcount(){
		this.UnChangeCount = 0;
	}

	public boolean needSave(){
		if( UnChangeCount > 2 && IsChange == true && data.size() > 0 )
			return true;
		return false;
	}

	public boolean needDiscard(){
		if( UnChangeCount > 2 && IsChange == false )
			return true;
		return false;
	}
	
	public void incUnchange(){
		this.UnChangeCount++;
	}
	
	public List<Node> getdata(){
		return data;
	}

	public void additem( Node d ){
		data.add(d);
	}
	
	public void additem( List<Node> d ){
		data.addAll(d);
	}
	
}
