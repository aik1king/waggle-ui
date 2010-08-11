package tw.kayjean.ui.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Entity;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserEntry {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private String name;

	@Persistent
	private String email;
	
	@Persistent
	private String url;

	@Persistent
	private String type;

	@Persistent
	private String sessionId;
	
	public UserEntry(String name, String email, String url, String type) {
		this.name = name;
		this.email = email;
		this.url = url;
		this.type = type;
	}

	public UserEntry() {
	}
	
	public String getname() {
		return name;
	} 
	public String getemail() {
		return email;
	} 
	public String geturl() {
		return url;
	} 
	public String gettype() {
		return type;
	}
	public String getsessionId() {
		return sessionId;
	}
	public void setsessionId( String sessionId ){
		this.sessionId = sessionId;
	}
	
    public static final String ENTITY_KIND = "UserEntry";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String URL = "url";
    public static final String TYPE = "type";
    public static final String SESSIONID = "sessionId";
    
    public Entity toEntity() {
        Entity entity = new Entity(ENTITY_KIND);
        entity.setProperty(NAME, name);
        entity.setProperty(EMAIL, email);
        entity.setProperty(URL, url);
        entity.setProperty(TYPE, type);
        entity.setProperty(SESSIONID, sessionId);
        return entity;
    }
}
