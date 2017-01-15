package cat.red.gangs.utils.database;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class Database {
	private RestClient client;
	private String indexName;
	private static Database instance;
	
	public Database(String host, int port, String dbName) {
		if(Database.instance == null) {
			client = RestClient.builder(new HttpHost(host, port, "http")).build();
			indexName = dbName;
			Database.instance = this;
		}else{
			this.client = Database.instance.client;
			this.indexName = Database.instance.indexName;
		}
	}
	

	public static Database getInstance(){
		return Database.instance;
	}
	
	public HashMap<String,String> request(String requestString) throws IOException{	
		HashMap<String,String> fields = new HashMap<String,String>();
		
		Response response = client.performRequest("GET", "/"+this.indexName+"/_search?q="+requestString, Collections.<String, String>emptyMap());
		
		DocumentContext json = JsonPath.parse(response.getEntity().getContent());
		int hits = json.read("$.hits.total");
		
		if(hits == 0)
			return null;
		
		HashMap<String, String> tmp = JsonPath.parse(response.getEntity().getContent()).read("$.hits.hits[0]");
		System.out.println(tmp.toString());
		
		return fields;
	}
}
