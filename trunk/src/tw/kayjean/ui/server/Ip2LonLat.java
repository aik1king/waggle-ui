package tw.kayjean.ui.server;

import javax.jdo.annotations.*;
import java.io.Serializable;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Ip2LonLat implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;	

	@Persistent
	private Double IPFrom;
	
	@Persistent
	private Double IPTo;
	
	@Persistent
	private Text LonLat;

    public static final String ENTITY_KIND = "Ip2LonLat";
    
    public static final String IPFROM = "IPFrom";
    public static final String IPTO = "IPTo";
    public static final String LONLAT = "LonLat";
    
    public Entity toEntity() {
        Entity entity = new Entity(ENTITY_KIND);
        entity.setProperty(IPFROM, IPFrom);
        entity.setProperty(IPTO, IPTo);
        entity.setProperty(LONLAT, this.LonLat);
        return entity;
    }
	
	public Ip2LonLat( String IPFrom, String IPTo,String LonLat) {
		this.IPFrom = Double.parseDouble(IPFrom);
		this.IPTo = Double.parseDouble(IPTo);
		this.LonLat = new Text(LonLat);
	}

	public Long getid() {
		return id;
	}
	public Double getIPFrom() {
		return IPFrom;
	} 
	public Double getIPTo() {
		return IPTo;
	} 
	public String getLonLat() {
		return LonLat.getValue();
	} 
}
