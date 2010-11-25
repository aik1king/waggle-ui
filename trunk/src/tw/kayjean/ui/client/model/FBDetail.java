package tw.kayjean.ui.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/*
"id":"100000121863275", 
"name":"Kay Jean", 
"first_name":"Kay", 
"last_name":"Jean", 
"link":"http://www.facebook.com/profile.php?id=100000121863275", 
"birthday":"03/22/1972", 
"bio":"test", 
"gender":"male", 
"timezone":8, 
"locale":"zh_TW", 
"updated_time":"2010-08-26T05:06:50+0000"
*/

public class FBDetail implements IsSerializable {
	public String id;
	public String name;
	public String first_name;
	public String last_name;
	public String link;
	public String birthday;
	public String bio;
	public String gender;
	public String timezone;
	public String locale;
	public String updated_time;
}
