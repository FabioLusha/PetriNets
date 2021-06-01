package petrinets.net;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

//Si estende Serializable per poter utilizzare il metodo clone del common-langs di Apache che fa una deep copy di un oggetto
public interface SimulatableNet extends Serializable,INet {

	void fire(Transition toFire);
	Set<Transition> getEnabledTransitions();
	Map<Place, Integer> getMarcmap();

}
