package migration.clustering.measure;

public class Info {
	private int entire;
	private int common;
	
	public Info(int entire, int common) {
		this.entire = entire;
		this.common = common;
	}
	
	public int getEntier() {
		return entire;
	}
	
	public int getCommon() {
		return common;
	}

}
