package MVC;

public class Pair <K,V>{
	
	private K first;
	private V second;
	
	public Pair(K pfirst, V psecond) {
		this.first = pfirst;
		this.second = psecond;
	}

	public K getFirst() {
		return first;
	}

	public void setFirst(K first) {
		this.first = first;
	}

	public V getSecond() {
		return second;
	}

	public void setSecond(V second) {
		this.second = second;
	}
	
	
}
