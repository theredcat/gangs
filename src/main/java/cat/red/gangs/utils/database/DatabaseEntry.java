package cat.red.gangs.utils.database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

public abstract class DatabaseEntry {
	
	protected String id;
	private String type;
	private String parentId;
	
	private static HashMap<String, HashMap<String, Class<?>>> schemas;
	
	public DatabaseEntry(String id) throws Exception{
		this.id = id;
		this.type = this.getObjectTypeName();
		System.out.println("Loading "+type+":"+id);
		this.load();
	}
	
	public void load() throws Exception{
		Database db = Database.getInstance();
		db.request("_id:"+this.id);
	}
	
	public void save(){
		
	}
	
	public Object get(String field) throws Exception{
		Class<?> fieldType = this.getFieldType(field);
		if(DatabaseEntry.class.isAssignableFrom(fieldType)){
			Constructor<?> cons =  fieldType.getConstructor(String.class);
			return cons.newInstance(this.parentId);
		}else if(fieldType == Boolean.class ||fieldType == Integer.class || fieldType == String.class){
			return this.getClass().getDeclaredField(field).get(fieldType);
		}else{
			throw new Exception("Unrecognized field "+field+" in class "+this.type);
		}
	}
	
	private Class<?> getFieldType(String field) throws Exception {
		if(DatabaseEntry.schemas == null){
			DatabaseEntry.schemas = new HashMap<String, HashMap<String, Class<?>>>();
		}
		if(!DatabaseEntry.schemas.containsKey(this.type)){
			DatabaseEntry.schemas.put(this.type, this.getSchema());
		}
		System.out.println("Schemas:"+DatabaseEntry.schemas.toString());
		System.out.println("Obj:"+DatabaseEntry.schemas.get(this.type)+" => "+DatabaseEntry.schemas.get(this.type).size());
		System.out.println("Field: "+field+" => "+DatabaseEntry.schemas.get(this.type).get(field));
		return DatabaseEntry.schemas.get(this.type).get(field);
	}
	
	public String getObjectTypeName(){
		String fullClassName = this.getClass().getName();
		return fullClassName.substring(fullClassName.lastIndexOf('.') + 1).toLowerCase();
	}
	
	public HashMap<String,Class<?>> getSchema() throws Exception{
		HashMap<String, Class<?>> result = new HashMap<String,Class<?>>();
		Field[] declaredFields = this.getClass().getDeclaredFields();
		boolean hasParent = false;
		
		for (Field field : declaredFields) {
			if(field.isAnnotationPresent(DatabaseField.class)){
				Class<?> fieldType = field.getType();
				String fieldName = field.getName();
				
				if (DatabaseEntry.class.isAssignableFrom(fieldType)){
					if(hasParent){
						throw new Exception("Objects cannot have more than one parent");
					}
					hasParent = true;
					result.put(fieldName, fieldType);
					
				}else if(fieldType == String.class){
					result.put(fieldName, String.class);
					
				}else if(fieldType.getTypeName() == "int" || fieldType == Integer.class){
					result.put(fieldName, Integer.class);
					
				}else if(fieldType.getTypeName() == "boolean" || fieldType == Boolean.class){
					result.put(fieldName, Boolean.class);
					
				}else{
					throw new Exception("Field "+fieldName+" in class "+this.getClass().getName()+" is marked with DatabaseField annotation but can't be used as such since it's not a base type (int, bool, string) and it doesn't implement DatabaseEntry class");
				}
			}
		}
		return result;
	}
}
