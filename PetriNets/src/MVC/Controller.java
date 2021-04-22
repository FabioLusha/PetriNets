package MVC;

public class Controller {

    private Model model;
    private View view;

    public void manageNetCreatrion(String netName){
        if(!model.createNet(netName))
            view.notifyError(Message.ERR_MSG_NET_NAME_ALREADY_EXIST);
    }

    public void addFluxRel(String posto, String transizione, int direction){
        if ( model.controlloTransizioneVuota(posto,transizione,direction))
            if( model.giaPresente(posto, transizione, direction) )
                view.notifyError(Message.ERR_MSG_FLUX_ELEM_ALREADY_EXSISTS);
        else{
                view.notifyError(Message.ERR_MSG_NOT_POINTED_TRANSITION);
        }
    }

}
