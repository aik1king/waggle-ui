package tw.kayjean.ui.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.*;

import java.io.Serializable;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ItineraryEntry2 implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;	

	@Persistent
	private Integer segment;

	@Persistent
	private String title;
//	@Persistent
//	private String alias;

/*	
	@Persistent
	private String u1;
	@Persistent
	private String u2;
	@Persistent
	private String u3;
*/
	@Persistent
	private String u;
	
	@Persistent
	private List<String> nongeo = new ArrayList<String>();
	//private String nongeo;
	
	@Persistent
	private Text Latitude;//緯度

	@Persistent
	private Text Longitude;//經度

	@Persistent
	private Text recommend;
	
	@Persistent
	private Text photo;
	
	@Persistent
	private Integer ranking;
	
	@Persistent
	private String location_geocell_1;
	@Persistent
	private String location_geocell_2;
	@Persistent
	private String location_geocell_3;
	@Persistent
	private String location_geocell_4;
	@Persistent
	private String location_geocell_5;
	@Persistent
	private String location_geocell_6;
	@Persistent
	private String location_geocell_7;
	@Persistent
	private String location_geocell_8;
	@Persistent
	private String location_geocell_9;
	@Persistent
	private String location_geocell_10;
	
	@Persistent
	//private String wizard_1;
	private List<String> wizard_1 = new ArrayList<String>();
	@Persistent
	//private String wizard_2;
	private List<String> wizard_2 = new ArrayList<String>();
	@Persistent
	//private String wizard_3;
	private List<String> wizard_3 = new ArrayList<String>();
	@Persistent
	//private String wizard_4;
	private List<String> wizard_4 = new ArrayList<String>();
	@Persistent
	//private String wizard_5;
	private List<String> wizard_5 = new ArrayList<String>();

//	@Persistent
//	private List<String> wizards = new ArrayList<String>();
	
	
    public static final String ENTITY_KIND = "ItineraryEntry2";
    
    public static final String SEGMENT = "segment";
    public static final String TITLE = "title";
//    public static final String ALIAS = "alias";
//    public static final String U1 = "u1";
//    public static final String U2 = "u2";
//    public static final String U3 = "u3";
    public static final String U = "u";
    
    //public static final String NONGEO = "nongeo";
    public static final String NONGEO = "nongeo";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String RECOMMEND = "recommend";
    public static final String PHOTO = "photo";
    public static final String RANKING = "ranking";
    public static final String LOCATION_GEOCELL_1 = "location_geocell_1";
    public static final String LOCATION_GEOCELL_2 = "location_geocell_2";
    public static final String LOCATION_GEOCELL_3 = "location_geocell_3";
    public static final String LOCATION_GEOCELL_4 = "location_geocell_4";
    public static final String LOCATION_GEOCELL_5 = "location_geocell_5";
    public static final String LOCATION_GEOCELL_6 = "location_geocell_6";
    public static final String LOCATION_GEOCELL_7 = "location_geocell_7";
    public static final String LOCATION_GEOCELL_8 = "location_geocell_8";
    public static final String LOCATION_GEOCELL_9 = "location_geocell_9";
    public static final String LOCATION_GEOCELL_10 = "location_geocell_10";
    
    
    public static final String WIZARD_1 = "wizard_1";
    public static final String WIZARD_2 = "wizard_2";
    public static final String WIZARD_3 = "wizard_3";
    public static final String WIZARD_4 = "wizard_4";
    public static final String WIZARD_5 = "wizard_5";
//    public static final String WIZARDS = "wizards";
    
    public Entity toEntity() {
        Entity entity = new Entity(ENTITY_KIND);
        entity.setProperty(SEGMENT, segment);
        entity.setProperty(TITLE, title);
//        entity.setProperty(ALIAS, alias);
        
//        entity.setProperty(U1, u1);
//        entity.setProperty(U2, u2);
//        entity.setProperty(U3, u3);
        entity.setProperty(U, u);
        
        entity.setProperty(NONGEO, nongeo);
        entity.setProperty(LATITUDE, Latitude);
        entity.setProperty(LONGITUDE, Longitude);
        entity.setProperty(RECOMMEND, this.recommend);
        entity.setProperty(PHOTO, this.photo);
        entity.setProperty(RANKING, ranking);
        entity.setProperty(LOCATION_GEOCELL_1, location_geocell_1);
        entity.setProperty(LOCATION_GEOCELL_2, location_geocell_2);
        entity.setProperty(LOCATION_GEOCELL_3, location_geocell_3);
        entity.setProperty(LOCATION_GEOCELL_4, location_geocell_4);
        entity.setProperty(LOCATION_GEOCELL_5, location_geocell_5);
        entity.setProperty(LOCATION_GEOCELL_6, location_geocell_6);
        entity.setProperty(LOCATION_GEOCELL_7, location_geocell_7);
        entity.setProperty(LOCATION_GEOCELL_8, location_geocell_8);
        entity.setProperty(LOCATION_GEOCELL_9, location_geocell_9);
        entity.setProperty(LOCATION_GEOCELL_10, location_geocell_10);

        entity.setProperty(WIZARD_1, wizard_1);
        entity.setProperty(WIZARD_2, wizard_2);
        entity.setProperty(WIZARD_3, wizard_3);
        entity.setProperty(WIZARD_4, wizard_4);
        entity.setProperty(WIZARD_5, wizard_5);
//        entity.setProperty(WIZARDS, wizards);
        return entity;
}

	public ItineraryEntry2( String title, String Latitude,String Longitude,String recommend,String photo ,String ranking, String geohash , String segment , String nongeo , String alias , String u1 , String u2 , String u3 ) {
		//this.segment = segment;
		this.segment = Integer.parseInt(segment);
		this.title = title;
		this.recommend = new Text(recommend);
		this.photo = new Text(photo);
		this.ranking = Integer.parseInt(ranking);
		if( u1 == null ) u1 = "";
		if( u2 == null ) u2 = "";
		if( u3 == null ) u3 = "";

//		this.alias = alias;
		final List<String> w = new ArrayList<String>();
		String[] aliastemp = alias.split("\\|");
		for( int i = 0 ; i < aliastemp.length ; i++ ){
			if( aliastemp[i].equalsIgnoreCase("")) continue;
			w.add(aliastemp[i]);
			if( aliastemp[i].length() > 0 && ( this.ranking > 0 || u3.indexOf("台灣") < 0 ) )
				addWizard( "1" , aliastemp[i].substring(0, 1) );
			if( aliastemp[i].length() > 1 )
				addWizard( "2" , aliastemp[i].substring(0, 2) );
			if( aliastemp[i].length() > 2 )
				addWizard( "3" , aliastemp[i].substring(0, 3) );
			if( aliastemp[i].length() > 3 )
				addWizard( "4" , aliastemp[i].substring(0, 4) );
			if( aliastemp[i].length() > 4 )
				addWizard( "5" , aliastemp[i].substring(0, 5) );
		}

		
//		this.u1 = u1;
//		this.u2 = u2;
//		this.u3 = u3;
		this.u = u1+"|"+u2+"|"+u3;
		
		if( !Longitude.equals("0") ){
			this.Longitude = new Text(Longitude);
			this.Latitude = new Text(Latitude);

			if( geohash == null || geohash.equals( "" ) || geohash.equals(" ") ){
				Double lat = Double.parseDouble(Latitude);
				Double lon = Double.parseDouble(Longitude);
				Geocell e = new Geocell();
				geohash = e.compute(lat , lon);
			}
			if( this.ranking > 0 || u3.indexOf("台灣") < 0 ){
//				this.location_geocell_1 = geohash.substring(0, 1);
				this.location_geocell_2 = geohash.substring(0, 2);
				this.location_geocell_3 = geohash.substring(0, 3);
				this.location_geocell_4 = geohash.substring(0, 4);
				this.location_geocell_5 = geohash.substring(0, 5);
			}
		    this.location_geocell_6 = geohash.substring(0, 6);
		    this.location_geocell_7 = geohash.substring(0, 7);
		    this.location_geocell_8 = geohash.substring(0, 8);
		    this.location_geocell_9 = geohash.substring(0, 9);
		    this.location_geocell_10 = geohash.substring(0, 10);
		}
		
/*		
 * 原本方式
		if( title.length() > 0 && ( this.ranking > 0 || this.u3.indexOf("台灣") < 0 ) )
			this.wizard_1 = title.substring(0, 1);
		if( title.length() > 1 )
			this.wizard_2 = title.substring(0, 2);
		if( title.length() > 2 )
			this.wizard_3 = title.substring(0, 3);
		if( title.length() > 3 )
			this.wizard_4 = title.substring(0, 4);
		if( title.length() > 4 )
			this.wizard_5 = title.substring(0, 5);
*/			

		if( title.length() > 0 && ( this.ranking > 0 || u3.indexOf("台灣") < 0 ) )
			addWizard( "1" , title.substring(0, 1) );
		if( title.length() > 1 )
			addWizard( "2" , title.substring(0, 2) );
		if( title.length() > 2 )
			addWizard( "3" , title.substring(0, 3) );
		if( title.length() > 3 )
			addWizard( "4" , title.substring(0, 4) );
		if( title.length() > 4 )
			addWizard( "5" , title.substring(0, 5) );
		
		
		//this.nongeo = nongeo;
		String[] nongeotemp = nongeo.split("\\|");
		for( int i = 0 ; i < nongeotemp.length ; i++ ){
			if( nongeotemp[i].equalsIgnoreCase("")) continue;
			w.add(nongeotemp[i]);

			//餐廳這種字,也要放入系統嗎?
			//關鍵字也要加入查詢系統中
			//所以查詢系統可以查詢地點,分類,還有使用者
			//至少別名也可以用這種方式處理了
			if( nongeotemp[i].length() > 0 && ( this.ranking > 0 || u3.indexOf("台灣") < 0 ) )
				addWizard( "1" , nongeotemp[i].substring(0, 1) );
			if( nongeotemp[i].length() > 1 )
				addWizard( "2" , nongeotemp[i].substring(0, 2) );
			if( nongeotemp[i].length() > 2 )
				addWizard( "3" , nongeotemp[i].substring(0, 3) );
			if( nongeotemp[i].length() > 3 )
				addWizard( "4" , nongeotemp[i].substring(0, 4) );
			if( nongeotemp[i].length() > 4 )
				addWizard( "5" , nongeotemp[i].substring(0, 5) );
		}
		if( w.size() > 0 ) this.nongeo = w;
		
	}
	
//	for( int i = 0 ; i < title.length()-1 ; i++ )
//	addWizard( title.substring( i,i+2 ) );
    public void addWizard(String fieldname , String data ) {
    	if( fieldname.equalsIgnoreCase("1") ){
    		final List<String> w = new ArrayList<String>(wizard_1);
    		w.add(data);
    		wizard_1 = w;
    	}
    	else if( fieldname.equalsIgnoreCase("2") ){
    		final List<String> w = new ArrayList<String>(wizard_2);
    		w.add(data);
    		wizard_2 = w;
    	}
    	else if( fieldname.equalsIgnoreCase("3") ){
    		final List<String> w = new ArrayList<String>(wizard_3);
    		w.add(data);
    		wizard_3 = w;
    	}
    	else if( fieldname.equalsIgnoreCase("4") ){
    		final List<String> w = new ArrayList<String>(wizard_4);
    		w.add(data);
    		wizard_4 = w;
    	}
    	else if( fieldname.equalsIgnoreCase("5") ){
    		final List<String> w = new ArrayList<String>(wizard_5);
    		w.add(data);
    		wizard_5 = w;
    	}
    }
	

	public Long getid() {
		return id;
	} 
	
	public String getlocation_geocell_3(){
		return location_geocell_3;
	}

/*	
	public String getsegment() {
		return segment;
	} 
*/	
	public Integer getsegment() {
		return segment;
	} 
	
	public String gettitle() {
		return title;
	} 

//	public String getalias() {
//		return alias;
//	}
	
/*	
	public String getu1() {
		return u1;
	} 
	public String getu2() {
		return u2;
	} 
	public String getu3() {
		return u3;
	} 
*/
	public String getu() {
		return u;
	} 
	
	public String getLatitude() {
		return Latitude.getValue();
	} 
	public String getLongitude() {
		return Longitude.getValue();
	} 
	public String getrecommend() {
		return recommend.getValue();
	} 
	public String getphoto() {
		return photo.getValue();
	}
	public Integer getranking() {
		return ranking;
	} 
	
	public String getnongeo() {
		return nongeo.toString();
	} 
	
}

