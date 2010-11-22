package tw.kayjean.ui.client.model;

import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Poi implements IsSerializable {
	  //目前是直接從parser的mydata直接拷貝過來,不過還要加入一些其他項目
	public int rank;
	public double lat;
	public double lon;
	public String targetname;
	public String targetl3; //國
	public String targetl2; //市
	public String targetl1; //區
	public String imageurl;
	
	//物件包含不同評論,包含其他物件
	public List<PoiDesc> items;
	
	public String toString( ){
		String r1 =  targetname + " " + targetl3 + " " + targetl2 + " " + targetl1;
		String r2 = "";
		if( imageurl != null && imageurl.equalsIgnoreCase("")){
			r2 = "<img src=\"" + imageurl + "\">";
		}
		String r3 = "";
		if( items != null && items.size() > 0 ){
			for( int i = 0 ; i < items.size() ; i++ ){
				r3 += items.get(i).toString();
				r3 += "<p>";
			}
		}
		return r1 + r2 + r3;
	}

}
