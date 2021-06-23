package petrinets.net;

import java.util.*;

public class PetriNet extends Net{
	private Map<Place,Integer> marcmap = new HashMap<>();
	private Map<OrderedPair, Integer>  valuemap = new HashMap<>();
	
	public PetriNet(String pname, Net scheletro) {
		super(pname);
		
		scheletro.getPlaces().forEach(e -> marcmap.put(e, 1));
		scheletro.getFluxRelation().forEach(e -> valuemap.put(e, 0));
		
	}
	
	public PetriNet() {
		
	}

	public Map<Place, Integer> getMarcmap() {
		return marcmap;
	}

	public void setMarcmap(Map<Place, Integer> marcmap) {
		this.marcmap = marcmap;
	}

	public Map<OrderedPair, Integer> getValuemap() {
		return valuemap;
	}

	public void setValuemap(Map<OrderedPair, Integer> valuemap) {
		this.valuemap = valuemap;
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Nome: " + this.getName()+ "\n");
		output.append("Posti: \n");
		getMarcmap().forEach((p,v) -> output.append("-" + p.getName() + "    marcatura: " + v + "\n"));
		output.append("Transizioni: \n");
		getValuemap().forEach((p,v) -> output.append("-" + p.getCurrentPlace().getName() + "->" + p.getCurrentTransition().getName() + "    valore: " + v + "\n"));
		return output.toString();
		
	}
	
	
}
