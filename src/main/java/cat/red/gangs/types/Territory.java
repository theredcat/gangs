package cat.red.gangs.types;

import com.flowpowered.math.vector.Vector3i;

import cat.red.gangs.utils.database.DatabaseEntry;
import cat.red.gangs.utils.database.DatabaseField;

public class Territory extends DatabaseEntry{

	public Territory(Vector3i chunkPosition) throws Exception {
		super(chunkPosition.getX()+"_"+chunkPosition.getY());
	}

	@DatabaseField
	private int x;
	@DatabaseField
	private int y;
	@DatabaseField
	private Gang ownerGang;
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Territory other = (Territory) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	public Gang getGang() throws Exception{
		return (Gang) this.get("ownerGang");
	}
	
	public boolean isClaimed() throws Exception
	{
		return this.getGang() == null;
	}

	public boolean gangCanBuild(Gang gang) throws Exception {
		return gang == this.getGang();
	}

	public boolean gangCanInterract(Gang gang) throws Exception {
		return gang == this.getGang();
	}

	public void claim(Gang playerGang) {
		//TODO
	}
}
