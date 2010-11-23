package tw.kayjean.ui.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import com.google.gwt.user.client.rpc.AsyncCallback;

import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.rpc.CoordinateServiceAsync;

//為了增加cache機制,還有加速運作,有些內容儲存在browser中
//增加這個項目
public class DataSwitch implements CoordinateServiceAsync {

	private final Map<String, Object> cache = new HashMap<String, Object>();

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

	  //景點詳細內容
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
	public void sendNode( String username , int type , Node nd , final AsyncCallback cb ){

		//一個景點,只能設定為是否刪除,或是提供給別人,而且不能轉換

		//只是為了提供給後面畫圖使用
		int i = cacheTable.size();
		for( int j = (i - 1) ; j >= 0 ; j-- ){
			Node n = (Node)cacheTable.get(j);
			if( n.name.equalsIgnoreCase(nd.name) )
				cacheTable.remove(j);
		}

		Node n2 = new Node();
		n2.name = nd.name;
		n2.fullname = nd.fullname;
		n2.type = type;
		n2.x = nd.x;
		n2.y = nd.y;
		n2.geocell = nd.geocell;
		n2.rank = nd.rank;
		cacheTable.add(n2);

		//射後不理
		Waggle_ui.coordService.sendNode( username , type , nd , new AsyncCallback<Integer>() {
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
	public void getRTree ( String username , String name , final AsyncCallback cb){
        if( name.equalsIgnoreCase("cache" ) ){
        	if( cacheTable != null && cacheTable.size() > 0 ){
        		//原本cache內容放進系統中,再依照type決定要不要畫出來
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
            	cache.put(QueryKey, result);            	
              cb.onSuccess(result);
            }
      }
		);
	}
}
