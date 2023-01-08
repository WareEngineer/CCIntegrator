package migration.clustering;

import java.util.PriorityQueue;

public class HierarchicalPriorityQueue<T extends HPQitem<?>> {
	private PriorityQueue<T>[] PQs;
	
	@SuppressWarnings("unchecked")
	public HierarchicalPriorityQueue(int size) {
		PQs = new PriorityQueue[size+1];
		for(int i=0; i<PQs.length; i++) {
			PQs[i] = new PriorityQueue<T>();
		}
	}

	public void put(T item) {
		PQs[item.hierarchy()].add(item);
	}
	
	public boolean isEmpty() {
		for(int i=PQs.length-1; i>=0; i--) {
			if(!PQs[i].isEmpty()) return false;
		}
		return true;
	}
	
	public T poll() {
		for(int i=PQs.length-1; i>=0; i--) {
			if(PQs[i].isEmpty()) continue;
			return PQs[i].poll();
		}
		return null;
	}

}