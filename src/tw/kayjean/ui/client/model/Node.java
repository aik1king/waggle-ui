package tw.kayjean.ui.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

//public class Node implements IsSerializable, Comparable {
public class Node implements IsSerializable {
    public String name;
    public double x;
    public double y;
    public int rank;
    public int type; // 0 : fact   1 : fav  2 : ignore 3 : love
    public int rankintype;
    public String geocell;
}
