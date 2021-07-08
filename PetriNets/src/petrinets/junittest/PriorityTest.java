package petrinets.junittest;

import org.junit.jupiter.api.Test;

import petrinets.domain.petrinet.PetriNet;
import petrinets.domain.petrinet.PriorityPetriNet;
import petrinets.domain.net.Net;
import petrinets.domain.net.OrderedPair;
import petrinets.domain.net.Place;
import petrinets.domain.net.Transition;

class PriorityTest {

	@Test
	void checkEnabledTransitions() {
		
		Net rete = new Net("net1");
		Place n1 = new Place("n1");
		Transition t1 = new Transition("t1");
		rete.addFluxRelElement(new OrderedPair(n1, t1));
		Transition t2 = new Transition("t2");
		Place n2 = new Place("n2");
		rete.addFluxRelElement(new OrderedPair(n2, t2));
		
		PetriNet reteDiPetri = new PetriNet("pn1", rete);
		reteDiPetri.changeMarc(n2, 2);
		
		PriorityPetriNet reteDiPetriWP = new PriorityPetriNet("pnp1", reteDiPetri);
		reteDiPetriWP.changePriorityTransition(t2, 10);

		assert reteDiPetriWP.getEnabledTransitions().iterator().next().equals(t2);
	}
	
	@Test
	void testChangePriorityValue() {
		Net rete = new Net("net1");
		Place n1 = new Place("n1");
		Transition t1 = new Transition("t1");
		
		rete.addFluxRelElement(new OrderedPair(n1 , t1));
		Transition t2 = new Transition("t2");
		Place n2 = new Place("n2");
		rete.addFluxRelElement(new OrderedPair(n2, t2));
		
		PetriNet reteDiPetri = new PetriNet("pn1", rete);
		
		PriorityPetriNet reteDiPetriWP = new PriorityPetriNet("pnp1", reteDiPetri);
		reteDiPetriWP.changePriorityTransition(t1,5);
		
		
		assert reteDiPetriWP.getPriority(t1) == 5;
		
	}

}
