package migration.clustering;

import java.util.HashSet;
import java.util.Set;

import migration.parsing.Product;

public class CloneSet {
	private Set<Chain> chains;
	private Set<CloneSet> inners;
	
	public CloneSet(Chain chain) {
		chains = new HashSet<Chain>();
		inners = new HashSet<CloneSet>();
		chains.add(chain);
	}
	
	public CloneSet(Set<Chain> chains, Set<CloneSet> inner) {
		this.chains = chains;
		this.inners = inner;
		if(chains == null) this.chains = new HashSet<Chain>();
		if(inner == null) this.inners = new HashSet<CloneSet>();
	}

	public Set<Chain> getChains() {
		return chains;
	}

	public Set<CloneSet> getInners() {
		return inners;
	}
	
	public int NOP() {
		Set<Product> set = new HashSet<Product>();
		for(Chain chain : chains) set.add(chain.getProduct());
		return set.size();
	}

	public int NOC() {
		return chains.size();
	}
	
	public String ID() {
		StringBuffer buffer = new StringBuffer();
		for(Chain chain : chains) {
			buffer.append(chain.hashCode());
			buffer.append(".");
		}
		return buffer.toString();
	}
	
	public CloneSet clone() {
		return new CloneSet(new HashSet<Chain>(chains), new HashSet<CloneSet>(inners));
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format("NOP: %2d, NOC: %2d\n", NOP(), NOC()));
		for(Chain chain : chains) {
			buffer.append(String.format("|-> CLASS: %30s,   FILE: %s\n", chain.getClazz().getName(), chain.getJavaFile().getPath()));
		}
		return buffer.toString();
	}

}
