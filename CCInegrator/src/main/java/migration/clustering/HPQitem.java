package migration.clustering;

public class HPQitem<T> implements Comparable<HPQitem<T>> {
	private T content;
	private int hierarchy;
	private double value;
	
	public HPQitem(T content, int hierarchy, double value) {
		this.content = content;
		this.hierarchy = hierarchy;
		this.value = value;
	}
	
	public T content() {
		return content;
	}
	
	public int hierarchy() {
		return hierarchy;
	}

	public double value() {
		return value;
	}
	
	@Override
	public int compareTo(HPQitem<T> o) {
		double cmp = this.value - o.value;
		if		(cmp < 0) return  1;
		else if (cmp > 0) return -1;
		else 			  return  0;
	}

}
