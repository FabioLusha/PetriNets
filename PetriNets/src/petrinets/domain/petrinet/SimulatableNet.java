package petrinets.domain.petrinet;

import petrinets.domain.net.INet;
import petrinets.domain.net.OrderedPair;
import petrinets.domain.net.Place;
import petrinets.domain.net.Transition;

import java.util.Map;
import java.util.Set;


public interface SimulatableNet extends INet {

	void fire(Transition toFire);
	Set<Transition> getEnabledTransitions();

	int getFluxRelValue(OrderedPair pair);
	int getMarcValue(Place n1);
	void changeMarc(Place n1, int newValue);
	void changeFluxRel(OrderedPair pair, int newFluxRelValue);
}
