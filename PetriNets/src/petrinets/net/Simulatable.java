package petrinets.net;

import java.io.Serializable;
import java.util.Collection;

//Si estende Serializable per poter utilizzare il metodo clone del common-langs di Apache che fa una deep copy di un oggetto
public interface Simulatable extends Serializable,INet{
	void simulate(Transition toExecute);
	Collection<Transition> getActiveTransitions();
}
