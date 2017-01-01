package cat.red.gangs.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.spongepowered.api.block.BlockType;


public class Config extends Properties {

	private Path configDir;
	private String configFile;
	private HashMap<String, Object> properties;

	public Config(Path configDir, String name) throws IOException {
		super();
		this.configDir = configDir;
		
		if (!Files.exists(configDir))
		{
			try
			{
				Files.createDirectories(configDir);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		if (Files.exists(configDir.resolve(name)))
		{
			try
			{
				Files.move(configDir.resolve(name), configDir);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		this.configFile = configDir.resolve(name).resolve("config.properties").toString();
		
		InputStream config = new FileInputStream(this.configFile);
		this.load(config);
	}
	
	public boolean getBool(String key) throws Exception {
		String ret = this.getProperty(key);
		
		if(ret != null){
			if(ret.matches("^(true|True)$")){
				return true;
			}else if(ret.matches("^(false|False)$")){
				return false;
			}else{
				throw new Exception("Using getBool on property "+key+"but value \""+ret+"\" can't be parsed to boolean");
			}
		} else if(properties.containsKey(key)) {
			Object prop = properties.get(key);
			if(prop instanceof Boolean){
				return (Boolean) prop;
			}else{
				throw new Exception("Using getBool on property "+key+"but default value \""+prop.toString()+"\" can't be parsed to integer");
			}
		} else {
			throw new Exception("No value or default value for key \""+key+"\""); 
		}
	}
	
	public BlockType getBlock(String key){
		// TODO Auto-generated method stub
		return null;
	}

	public String getString(String key) throws Exception {
		String ret = this.getProperty(key);
		
		if(ret != null){
			return ret;
		} else if(properties.containsKey(key)) {
			return (String) properties.get(key);
		} else {
			throw new Exception("No value or default value for key \""+key+"\"");
		}
	}

	public int getInt(String key) throws Exception {
		String ret = this.getProperty(key);
		
		if(ret != null){
			if(ret.matches("^?\\d+$")){
				return Integer.parseInt(ret);
			}else{
				throw new Exception("Using getInt on property "+key+"but value \""+ret+"\" can't be parsed to integer");
			}
		} else if(properties.containsKey(key)) {
			Object prop = properties.get(key);
			if(prop instanceof Integer){
				return (Integer) prop;
			}else{
				throw new Exception("Using getInt on property "+key+"but default value \""+prop.toString()+"\" can't be parsed to integer");
			}
		} else {
			throw new Exception("No value or default value for key \""+key+"\""); 
		}
	}

	public void createProperty(String key, String s) {
		properties.put(key, new String(s));
	}

	public void createProperty(String key, int i) {
		properties.put(key, new Integer(i));
	}

}
