package migration.clustering.classifier;

import java.util.HashSet;
import java.util.Set;

import migration.clustering.CloneSet;

public class DuplicationChecker {
	private Set<String> set = new HashSet<String>();

	public boolean isMarked(CloneSet candidate) {
		return set.contains(candidate.ID());
	}

	public void mark(CloneSet candidate) {
		set.add(candidate.ID());
	}

}
