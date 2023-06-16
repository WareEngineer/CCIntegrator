package migration.merging.entity;

public class TaggedLines implements Comparable<TaggedLines>{
	private final String id;
	private final String[] lines;
	
	public TaggedLines(String id, String[] lines) {
		this.id = id;
		this.lines = lines;
	}
	
	public String getId() {
		return id;
	}
	
	public String[] getCode() {
		return lines;
	}

	@Override
	public int compareTo(TaggedLines other) {
		return this.lines.length - other.lines.length;
	}
	
	
}
