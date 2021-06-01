package petrinets.net;

import java.io.Serializable;
import java.util.Set;

public interface INet extends Serializable {

    String getName();
    Set<Place> getPlaces();
    Set<Transition> getTransitions();
    Set<OrderedPair> getFluxRelation();
}
