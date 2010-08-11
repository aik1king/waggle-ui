package tw.kayjean.ui.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.activation.URLDataSource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.jdo.PersistenceManager;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.FetchOptions;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItem;

//import org.apache.commons.io.IOUtils;
//目前先忽略commons-io-1.4.jar這個jar

/**
 * The FileUploadServlet suppports file uploading from the client.
 * 
 */
public class FileUploadServlet extends HttpServlet {

	private final String DEFAULT_DELIMITER = ",";
	private String delimiter = DEFAULT_DELIMITER; // default delimiter

	
	//http://groups.google.com/group/google-appengine-java/browse_thread/thread/4ba11a5c220c47c5/4a1d09cd2dd380bf
    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
    throws IOException {
    	String requestType = req.getParameter("table");
    	if( requestType.equals( "ItineraryEntry" ) ){
    		
    		String requestType2 = req.getParameter("filter");
    		if( requestType2 != null && requestType2 != "" )
    		{
	    		DatastoreService datastore =DatastoreServiceFactory.getDatastoreService(); 
	    		Query q = new Query("ItineraryEntry1");
	    		int requestType3 = Integer.parseInt(requestType2);
	    		q.addFilter("segment", FilterOperator.EQUAL , requestType3);
	    		ArrayList<Key> keys = new ArrayList<Key>();
	    		List<Entity> it = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(200));
	    		for (Entity entity : it)
	    			keys.add(entity.getKey());
	    		datastore.delete(keys);
    			PrintWriter out = resp.getWriter();
    			out.write( "datais" + keys.size() );
    		}
    		else
    		{
        		DatastoreService datastore =DatastoreServiceFactory.getDatastoreService(); 
        		Query q = new Query("ItineraryEntry1");
        		ArrayList<Key> keys = new ArrayList<Key>();
        		List<Entity> it = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(200)); 
        		for (Entity entity : it)
        			keys.add(entity.getKey()); 
        		datastore.delete(keys);
    		}
    	} else if( requestType.equals( "TAG" ) ){
    		String requestType2 = req.getParameter("filter");
    		if( requestType2 != null && requestType2 != "" )
    		{
	    		DatastoreService datastore =DatastoreServiceFactory.getDatastoreService(); 
	    		Query q = new Query("TAG");
	    		q.addFilter("segment", FilterOperator.EQUAL , requestType2);
	    		ArrayList<Key> keys = new ArrayList<Key>();
	    		List<Entity> it = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(200));
	    		for (Entity entity : it)
	    			keys.add(entity.getKey());
	    		datastore.delete(keys);
    			PrintWriter out = resp.getWriter();
    			out.write( "datais" + keys.size() );
    		}
    		else
    		{
        		DatastoreService datastore =DatastoreServiceFactory.getDatastoreService(); 
        		Query q = new Query("TAG");
        		ArrayList<Key> keys = new ArrayList<Key>();
        		List<Entity> it = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(200)); 
        		for (Entity entity : it)
        			keys.add(entity.getKey()); 
        		datastore.delete(keys);
    		}
    	} else if( requestType.equals( "CommentEntry" ) ){
    		
    		String requestType2 = req.getParameter("filter");
    		if( requestType2 != null && requestType2 != "" )
    		{
	    		DatastoreService datastore =DatastoreServiceFactory.getDatastoreService(); 
	    		Query q = new Query("CommentEntry");
	    		q.addFilter("segment", FilterOperator.EQUAL , requestType2);
	    		ArrayList<Key> keys = new ArrayList<Key>();
	    		List<Entity> it = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(200));
	    		for (Entity entity : it)
	    			keys.add(entity.getKey());
	    		datastore.delete(keys);
    			PrintWriter out = resp.getWriter();
    			out.write( "datais" + keys.size() );
    		}
    		else
    		{
	    		DatastoreService datastore =DatastoreServiceFactory.getDatastoreService(); 
	    		Query q = new Query("CommentEntry");
	    		ArrayList<Key> keys = new ArrayList<Key>();
	    		List<Entity> it = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(200));
	    		for (Entity entity : it)
	    			keys.add(entity.getKey());
	    		datastore.delete(keys);
    		}
    	} else if( requestType.equals( "Ip2LonLat" ) ){
    		
	    		DatastoreService datastore =DatastoreServiceFactory.getDatastoreService(); 
	    		Query q = new Query("Ip2LonLat");
	    		ArrayList<Key> keys = new ArrayList<Key>();
	    		List<Entity> it = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(200));
	    		for (Entity entity : it)
	    			keys.add(entity.getKey());
	    		datastore.delete(keys);
    	}    	
    }

	
	/**
	 * Extracts upload form values and saves the uploaded file to a temporary
	 * file and throws an exception if problems are found.
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 *      , javax.servlet.http.HttpServletResponse)
	 */
	@Override
  public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			ServletFileUpload upload = new ServletFileUpload();
			upload.setSizeMax(10000000);
			upload.setHeaderEncoding("UTF-8");
			req.setCharacterEncoding("UTF-8");
			//upload.setHeaderEncoding(encoding)
			//req.setCharacterEncoding(arg0)
			res.setContentType("text/plain");
			PrintWriter out = res.getWriter();
			try {
				FileItemIterator iterator = upload.getItemIterator(req);
				while (iterator.hasNext()) {
					FileItemStream item = iterator.next();
					InputStream in = item.openStream();
					String fieldName = item.getFieldName();
					if (item.isFormField()) {
						out.println("Got a form field: " + item.getFieldName());
					} else {
						if (fieldName.equals("file_upload")) {
							String fileName = item.getName().toLowerCase();
							if( fileName.indexOf("_upload") >= 0 ){
						          List<String> missingHeaders = ItemInputStreamPOI( in , delimiter.charAt(0), null);
							}
							if( fileName.indexOf("user.txt") >= 0 ){
						          List<String> missingHeaders = CsvInputStreamUSER( in , delimiter.charAt(0), null);
							}
							if( fileName.indexOf("ip.txt") >= 0 ){
						          List<String> missingHeaders = CsvInputStreamIP( in , delimiter.charAt(0), null);
							}
						}
						else{
							String fileName = item.getName();
							String contentType = item.getContentType();
							out.println("--------------");
							out.println("fileName = " + fileName);
							out.println("field name = " + fieldName);
							out.println("contentType = " + contentType);
							String fileContents = null;
/*							
							try {
								fileContents = IOUtils.toString(in);
								out.println("lenght: " + fileContents.length());
								out.println(fileContents);
							} finally {
								IOUtils.closeQuietly(in);
							}
*/							
						}
					}
				}
			} catch (SizeLimitExceededException e) {
				out.println("You exceeded the maximu size ("
						+ e.getPermittedSize() + ") of the file ("
						+ e.getActualSize() + ")");
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
		
        String out = "";
        res.getWriter().write("{\"onSuccess\": " + out + "}");
	}

	public String getdata( Reader reader ){
		try {
			char[] charbuf = new char[12*1024];
			
		    //int r = reader.read(charbuf,0,7);
	
		    int len = 0;
		    StringBuilder data1 = new StringBuilder();
		    int num2 = 7;
		    while ((len = reader.read(charbuf,0,num2)) != -1) {
			      data1.append(charbuf, 0, len);
			      num2 -= len;
			      if( num2 <= 0 )
			    	  break;
		    }
		    if( len == -1 )
		    	return null;
		    int num = Integer.parseInt( data1.toString().trim() );
	
		    if( num == 0 )
		    	return "";
		    
		    
			StringBuilder data = new StringBuilder();
		    while ((len = reader.read(charbuf,0,num)) != -1) {
		      data.append(charbuf, 0, len);
		      num -= len;
		      if( num <= 0 )
		    	  break;
		    }
		    return data.toString();
		}
		catch( Exception e){
		}
		return "";
	}
	
	public List<String> ItemInputStreamPOI(InputStream csvSource,char delimiter, String[] requiredHeaders) throws IOException , EntityNotFoundException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(csvSource , "UTF-8"));
		StringBuilder builder;
		String[] tokens;
		List<String> missingHeaders = null;
        
        //取出資料
        String rank = getdata(reader);
        String lon = getdata(reader); //這真的很奇怪,居然不一樣??
        String lat = getdata(reader);
        String targetname = getdata(reader);
        String targetl3 = getdata(reader);
        String targetl2 = getdata(reader);
        String targetl1 = getdata(reader);
        String imageurl = getdata(reader);
        String mblogstr = getdata(reader);
        int mblog = Integer.parseInt(mblogstr);
        String recommend = "";
        for( int i = 0 ; i < mblog ; i++ ){
        	String url = getdata(reader);
        	String title = getdata(reader);
        	String desc = getdata(reader);
        	String authorurl = getdata(reader);
        	String authorname = getdata(reader);
        	String date = getdata(reader);
        	recommend += url + title + desc + authorurl + authorname + date;
        }
        
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

		String h = targetname + targetl1 + targetl2 + targetl3;
		int requestType3 = h.hashCode();

        //刪除舊資料
		Query q = new Query("ItineraryEntry2");
		q.addFilter("segment", FilterOperator.EQUAL , requestType3);
		ArrayList<Key> keys = new ArrayList<Key>();
		List<Entity> it = datastoreService.prepare(q).asList(FetchOptions.Builder.withLimit(200));
		for (Entity entity : it){
			keys.add(entity.getKey());
		}
		datastoreService.delete(keys);
        
        //加入資料
		ItineraryEntry2 entry = new ItineraryEntry2(targetname, lat, lon,recommend,imageurl,rank,"", Integer.toString( requestType3 ),"","",targetl1,targetl2,targetl3);
		Entity comEntity = entry.toEntity();
		Key key = datastoreService.put(comEntity);
		key = datastoreService.put(comEntity);
        
		reader.close();
		return missingHeaders;
	}
	
	public List<String> CsvInputStreamUSER(InputStream csvSource,char delimiter, String[] requiredHeaders) throws IOException , EntityNotFoundException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(csvSource , "UTF-8"));
		String[] tokens;
		List<String> missingHeaders = null;
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			tokens = line.split("\t");
			if (tokens.length == 2) {
				UserEntry entry = new UserEntry(tokens[0], tokens[0],tokens[0], "web");
				Entity comEntity = entry.toEntity();
				Key key = datastoreService.put(comEntity);
			}
		}
		reader.close();
		return missingHeaders;
	}

	public List<String> CsvInputStreamIP(InputStream csvSource,char delimiter, String[] requiredHeaders) throws IOException , EntityNotFoundException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(csvSource , "UTF-8"));
		String[] tokens;
		List<String> missingHeaders = null;
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			tokens = line.split("\t");
			if (tokens.length == 3) {
				Ip2LonLat entry = new Ip2LonLat(tokens[0], tokens[1],tokens[2]);
				Entity comEntity = entry.toEntity();
				Key key = datastoreService.put(comEntity);
			}
		}
		reader.close();
		return missingHeaders;
	}
}
