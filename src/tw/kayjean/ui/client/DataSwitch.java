package tw.kayjean.ui.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import com.google.gwt.user.client.rpc.AsyncCallback;

import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.rpc.CoordinateServiceAsync;

public class DataSwitch implements CoordinateServiceAsync {

	private final Map<String, Object> cache = new HashMap<String, Object>();

	//這組是專門用來處理這次運作資料區
	//所有資料在不同tab間移動時,都要把資料放入這個hash中
	//每次畫圖時,都要檢查這個hash有沒有符合項目
	private final List<Node> cacheTable = new ArrayList<Node>(); 
	
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
	
	public void getNode(String s, final AsyncCallback cb) {
		
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
	
	public void sendNode( String username , int type , String name , double x , double y , String geocell , final AsyncCallback cb ){

		//尋找是否有出現過
		int i = cacheTable.size();
		for( int j = 0 ; j < i ; j++ ){
			Node n = (Node)cacheTable.get(j);
			if( n.name.equalsIgnoreCase(name) )
				cacheTable.remove(j);
		}
		Node n2 = new Node();
		n2.name = name;
		n2.type = type;
		n2.x = x;
		n2.y = y;
		n2.geocell = geocell;
		cacheTable.add(n2);
		
		Waggle_ui.coordService.sendNode( username , type , name , x , y , geocell , new AsyncCallback<Integer>() {

            public void onFailure(Throwable caught) {
              cb.onFailure(caught);
            }

            public void onSuccess(Integer result) {
              cb.onSuccess(result);
            }
      }
		);
		
	}


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
	
	public void getRTree ( String username , String name , final AsyncCallback cb){

        if( name.equalsIgnoreCase("cache" ) ){
        	if( cacheTable != null && cacheTable.size() > 0 ){
        		//畫出原本cache內容
        		cb.onSuccess(cacheTable);
        	}
        	return;
        }
        
		final String QueryKey = name;
        List cachResult = (List) cache.get(QueryKey);
        if (cachResult != null) {
        	
    		for (Iterator<Node> iter1 = cachResult.iterator(); iter1.hasNext();) {
    			Node n = iter1.next();
    			for (Iterator<Node> iter2 = cacheTable.iterator(); iter2.hasNext();) {
    				Node m = iter2.next();
    				if( n.name.equalsIgnoreCase(m.name)){
    					if( n.type == m.type ){
        					//相同type,刪除cacheTable裡面那個項目,自己還是可以畫出來
    						iter2.remove();
    					}
    					else{
    						//不同type,不用畫出來
    						iter1.remove();
    					}
    				}
    			}
    		}
        	
            cb.onSuccess(cachResult);
            return;
        }
		Waggle_ui.coordService.getRTree( username , name , new AsyncCallback<List>() {
            public void onFailure(Throwable caught) {
              cb.onFailure(caught);
            }

            public void onSuccess(List result) {
            	cache.put(QueryKey, result);
            	
        		for (Iterator<Node> iter1 = result.iterator(); iter1.hasNext();) {
        			Node n = iter1.next();
        			for (Iterator<Node> iter2 = cacheTable.iterator(); iter2.hasNext();) {
        				Node m = iter2.next();
        				if( n.name.equalsIgnoreCase(m.name)){
        					if( n.type == m.type ){
            					//相同type,刪除cacheTable裡面那個項目,自己還是可以畫出來
        						iter2.remove();
        					}
        					else{
        						//不同type,不用畫出來
        						iter1.remove();
        					}
        				}
        			}
        		}
            	
            	
              cb.onSuccess(result);
            }
      }
		);
		
	}
	
}
