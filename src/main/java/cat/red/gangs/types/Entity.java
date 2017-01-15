package cat.red.gangs.types;

import java.util.UUID;

import cat.red.gangs.utils.database.DatabaseEntry;
import cat.red.gangs.utils.database.DatabaseField;

public class Entity extends DatabaseEntry {
	
	@DatabaseField
	private Gang gang;

	public Entity(UUID uuid) throws Exception {
		super(uuid.toString());
	}
	
	public Gang getGang() throws Exception{
		return (Gang) this.get("gang");
	}
}
