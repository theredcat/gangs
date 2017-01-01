package cat.red.gangs.utils.database;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import cat.red.gangs.types.Gang;
import cat.red.gangs.types.Territory;
import cat.red.gangs.utils.Cache;
import cat.red.gangs.utils.Config;

public class Database {
	
	private Cache<UUID,Gang> gangCache;
	private Cache<Location<World>,Territory> territoryCache;
	
	private RestClient client;
	private String indexName;
	
	private static final String ES_HOSTS_CONFIG_KEY = "database.hosts";
	
	private static final String ES_INDEX_NAME = "database.indexName";

	public Database(String host, int port, String dbName) {
		this.gangCache = new Cache<UUID,Gang>();
		this.territoryCache = new Cache<Location<World>,Territory>();
				
		this.client = RestClient.builder(new HttpHost(host, port, "http")).build();
		this.indexName = dbName;
	}

	public Gang getGang(UUID uniqueId) {
		Gang gang = gangCache.getObject(uniqueId);
		if (gang != null){
			return (Gang) gang;
		} else {
			try {
				Response response = this.client.performRequest(
					"GET", 
					String.format("/%1$s/%2$s/_search", indexName),
					Collections.singletonMap("q", String.format("members", uniqueId.toString()))
				);
				System.out.println(EntityUtils.toString(response.getEntity()));
				return new Gang("yolo");
			} catch (ParseException | IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public boolean createGang(){
		// TODO Auto-generated method stub
		return false;
	}

	public Territory getTerritory(Location<World> location) {
		return null;
	}
	
	public Territory createTerritory(int x, int y){
		// TODO Auto-generated method stub
		return null;
		
	}

}
