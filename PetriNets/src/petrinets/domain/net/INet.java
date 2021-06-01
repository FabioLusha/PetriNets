package petrinets.domain.net;

import java.io.Serializable;
import java.util.Set;

//Si estende Serializable per poter utilizzare il metodo clone del common-langs di Apache che fa una deep copy di un oggetto
public interface INet extends Serializable {

    String getName();
    Set<Place> getPlaces();
    Set<Transition> getTransitions();
    Set<OrderedPair> getFluxRelation();
}
