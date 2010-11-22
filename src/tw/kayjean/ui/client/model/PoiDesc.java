package tw.kayjean.ui.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PoiDesc implements IsSerializable {
	
	public String url;
	public String title;
	public String desc;
	public String authorurl;
	public String authorname;
	public String pubdate;
	
	public String toString(){
		return url + " " + title + " " + desc + " " + authorurl + " " + authorname + " " + pubdate;
	}
}
