package MVC;

import analyzer.XMLmanager;
import net.*;

import java.io.IOException;
import java.util.Map;

public class Model {

    private static XMLmanager netxmlmanager = new XMLmanager<Map<String, Net>>("nets.xml");

    private NetArchive netArchive;
    private Net controlNet;

    public Model(NetArchive netArchive){
        this.netArchive = netArchive;
    }

    public Model(){
        if (!(netxmlmanager.isEmpty())) {
            try {
                netArchive = new NetArchive ((Map<String, Net>) netxmlmanager.deserializeFromXML());

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else
            netArchive = new NetArchive();
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
