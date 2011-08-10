package tw.kayjean.ui.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import com.google.gwt.user.client.rpc.AsyncCallback;

import tw.kayjean.ui.client.model.FBDetail;
import tw.kayjean.ui.client.model.FBFriends;
import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.rpc.CoordinateServiceAsync;

//為了增加cache機制,還有加速運作,有些內容儲存在browser中
//增加這個項目
public class DataSwitch implements CoordinateServiceAsync {

	private final Map<String, Object> cache = new HashMap<String, Object>();

	//所有資料在不同tab間移動時,都要把資料放入這個hash中
	//每次畫圖時,都要檢查這個hash有沒有符合項目
	private final List<Node> cacheTabledel = new ArrayList<Node>(); 
	private final List<Node> cacheTableadd = new ArrayList<Node>();
	
	private static DataSwitch dataSwitch = null;
	
	  public static DataSwitch get() {
		  
		    if (dataSwitch == null) {
		      dataSwitch = new DataSwitch();
		    }
		    return dataSwitch;
		  }

	  private DataSwitch() {
		    // Pings the server every 5 seconds and clears the cache if an update
		    // occurred:

		  }

	  //景點詳細內容,其實根本沒有透過DataSwitch
	  public void sendDetail ( FBDetail fd , FBFriends ffs , final AsyncCallback cb ){
		  
	  }
	  
	public void getNode(String s, final AsyncCallback cb) {

		//內容不需要cache,直接redirect向server取得
		Waggle_ui.coordService.getNode(s, new AsyncCallback<Integer>() {
              public void onFailure(Throwable caught) {
                cb.onFailure(caught);
              }

              public void onSuccess(Integer result) {
                cb.onSuccess(result);
              }
        }
		);
	  }
	
	//使用者IP對應經緯度
	public void getIPLocation( final AsyncCallback cb ){
		
		Waggle_ui.coordService.getIPLocation( new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
              cb.onFailure(caught);
            }

            public void onSuccess(String result) {
              cb.onSuccess(result);
            }
      }
		);
		
	}

	//移轉給別人 或是設定為不要顯示
	public void sendNode( Node nd , final AsyncCallback cb ){
		cacheTabledel.add(nd);
		if( nd.fid.equalsIgnoreCase(nd.tid) ){
			cacheTableadd.add(nd);
		}
		//射後不理
		Waggle_ui.coordService.sendNode( nd , new AsyncCallback<Integer>() {
            public void onFailure(Throwable caught) {
              cb.onFailure(caught);
            }
            public void onSuccess(Integer result) {
              cb.onSuccess(result);
            }
      }
		);
	}


/*	
 * 搜尋使用,先不管搜尋
	public void getLocations( String prefix, int limit , final AsyncCallback cb ){
		
		Waggle_ui.coordService.getLocations( prefix , limit , new AsyncCallback<List>() {
            public void onFailure(Throwable caught) {
              cb.onFailure(caught);
            }

            public void onSuccess(List result) {
              cb.onSuccess(result);
            }
      }
		);
		
	}
*/
	
	//所謂的name是geoname
	public void getRTree ( String username , String name , int type , final AsyncCallback cb){
		if( type == 0 ){
			final String QueryKey = name + "_" + type;
	        List cachResult = (List) cache.get(QueryKey);
	        if (cachResult != null) {
	    		for (Iterator<Node> iter1 = cachResult.iterator(); iter1.hasNext();) {
	    			Node n = iter1.next();
	    			for (Iterator<Node> iter2 = cacheTabledel.iterator(); iter2.hasNext();) {
	    				Node m = iter2.next();
	    				if( n.name.equalsIgnoreCase(m.name)){
	    					iter1.remove();
	    				}
	    			}
	    		}
	            cb.onSuccess(cachResult);
	            return;
	        }
	        else{
	        	Waggle_ui.coordService.getRTree( username , name , type , new AsyncCallback<List>() {
	            public void onFailure(Throwable caught) {
	              cb.onFailure(caught);
	            }
	            public void onSuccess(List result) {
	            	cache.put(QueryKey, result);
	              cb.onSuccess(result);
	            }
	        	}
	        	);
	        }
		}
		if( type == 1 ){
			
	        if( name.equalsIgnoreCase("cache" ) ){
	        	if( cacheTableadd != null && cacheTableadd.size() > 0 ){
	        		//原本cache內容放進系統中,再依照type決定要不要畫出來
	        		cb.onSuccess(cacheTableadd);
	        	}
	        	return;
	        }
			
			final String QueryKey = name + "_" + type;
	        List cachResult = (List) cache.get(QueryKey);
	        if (cachResult != null) {
	            cb.onSuccess(cachResult);
	            return;
	        }
	        else{
				Waggle_ui.coordService.getRTree( username , name , type , new AsyncCallback<List>() {
		            public void onFailure(Throwable caught) {
		              cb.onFailure(caught);
		            }
		            public void onSuccess(List result) {
		            	cache.put(QueryKey, result);
		              cb.onSuccess(result);
		            }
				}
				);
	        }
		}
	}
}
