package cat.red.gangs.types;

public class Territory {
	
	private boolean isClaimed;

	public Territory()
	{
		
	}
	
	public boolean isClaimed()
	{
		return this.isClaimed;
	}

	public Gang getGang() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean gangCanBuild(Gang gang) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean gangCanInterract(Gang gang) {
		// TODO Auto-generated method stub
		return false;
	}
}
