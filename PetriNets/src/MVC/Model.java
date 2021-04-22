package MVC;

import net.*;

public class Model {

    private NetArchive netArchive;
    private Net controlNet;

    public Model(NetArchive netArchive){
        this.netArchive = netArchive;
    }

    //return true se in Archive non è presente alcun elemento con chiave name
    public boolean createNet(String name){

        if(netArchive.contains(name))
            return false;
        else{
            controlNet = new Net(name);
        }
        return true;
    }

    //Trasizione non puntata de nessun posto
    public boolean controlloTransizioneVuota(String posto, String trans, int direction){

        if (OrderedPair.typePair.ordinalToType(direction) == OrderedPair.typePair.tp
                && !controlNet.containsTransition(new Transition(trans))) {
            return false;
        }
        return true;
    }

    public boolean giaPresente(String posto, String trans, int direction){
        return controlNet.addFluxRelElement(
                new OrderedPair(
                        new Place(posto), new Transition(trans), OrderedPair.typePair.ordinalToType(direction))
        );
    }


}
