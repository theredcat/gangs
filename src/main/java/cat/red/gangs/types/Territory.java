package cat.red.gangs.types;

import cat.red.gangs.Gangs;
import cat.red.gangs.utils.database.DatabaseEntry;

public class Territory extends DatabaseEntry{
	
	public Territory(String id) {
		super(id);
	}

	private boolean isClaimed = false;
	private int x;
	private int y;
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
	
	public boolean isClaimed()
	{
		return this.isClaimed;
	}

	public Gang getGang() {
		return this.ownerGang;
	}

	public boolean gangCanBuild(Gang gang) {
		return gang == ownerGang;
	}

	public boolean gangCanInterract(Gang gang) {
		return gang == ownerGang;
	}

	public void claim(Gang playerGang) {
		Gangs.getDatabase().createTerritory(this.x, this.y);
	}
}
