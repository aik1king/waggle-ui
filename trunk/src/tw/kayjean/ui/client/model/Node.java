package tw.kayjean.ui.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

//public class Node implements IsSerializable, Comparable {
public class Node implements IsSerializable {
	public String fullname;
    public String name;
    public double x; //lon
    public double y; //lat
    public int rank;
    public int type; // 0 : fact   1 : fav  2 : ignore 3 : love
//    public int rankintype; //這個可能可以移除
    public String geocell;
}
