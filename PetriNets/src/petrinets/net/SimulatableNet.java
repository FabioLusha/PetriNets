package petrinets.net;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


public interface SimulatableNet extends INet {

	void fire(Transition toFire);
	Set<Transition> getEnabledTransitions();
	Map<Place, Integer> getMarcmap();

}
