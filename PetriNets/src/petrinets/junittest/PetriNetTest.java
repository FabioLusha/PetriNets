package petrinets.junittest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import petrinets.domain.PetriNet;
import petrinets.domain.net.Net;
import petrinets.domain.net.OrderedPair;
import petrinets.domain.net.Place;
import petrinets.domain.net.Transition;

class PetriNetTest {

	@Test
	void testChangeMarc() {
		Net rete = new Net("net1");
		Place n1 = new Place("n1");
		Transition t1 = new Transition("t1");
		
		rete.addFluxRelElement(new OrderedPair(n1 , t1));
		Transition t2 = new Transition("t2");
		Place n2 = new Place("n2");
		rete.addFluxRelElement(new OrderedPair(n2, t2));
		
		PetriNet reteDiPetri = new PetriNet("pn1", rete);
		
		assert reteDiPetri.getMarcmap().get(n1)  == 0;
		
		reteDiPetri.changeMarc(n1, 2);
		
		assert reteDiPetri.getMarcmap().get(n1) == 2;
	}
	
	@Test
	void testGetMarcValue() {
		Net rete = new Net("net1");
		Place n1 = new Place("n1");
		Transition t1 = new Transition("t1");
		
		rete.addFluxRelElement(new OrderedPair(n1 , t1));
		Transition t2 = new Transition("t2");
		Place n2 = new Place("n2");
		rete.addFluxRelElement(new OrderedPair(n2, t2));
		
		PetriNet reteDiPetri = new PetriNet("pn1", rete);
		
		assert reteDiPetri.getMarcValue(n1) == 0;
	}
	
	@Test
	void testChangeFluxRelation() {
		Net rete = new Net("net1");
		Place n1 = new Place("n1");
		Transition t1 = new Transition("t1");
		
		OrderedPair pair = new OrderedPair(n1 , t1);
		rete.addFluxRelElement(pair);
		
		PetriNet reteDiPetri = new PetriNet("pn1", rete);
		reteDiPetri.changeFluxRel(pair, 5);
		assert reteDiPetri.getFluxRelValue(pair) == 5;
	}

}
