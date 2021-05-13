package petrinets.net;

import java.util.Set;

public interface INet {

    String getName();
    Set<Place> getPlaces();
    Set<Transition> getTransitions();
    Set<OrderedPair> getFluxRelation();
}
