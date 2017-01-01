package cat.red.gangs.utils.database;

import java.lang.reflect.Field;
import java.util.HashMap;

public class DatabaseEntry {
	public DatabaseEntry(String id){

	}
	
	public void save(){
		
	}
	
	public HashMap<String,Object> getSchema(){
		HashMap<String,Object> result = new HashMap<String,Object>();
	    Field[] declaredFields = this.getClass().getDeclaredFields();
	    for (Field field : declaredFields) {
	    	System.out.println(field.getName() + field.isAccessible());
	    }
	    return result;
	}
}
