package petrinets.UI.controller;

import petrinets.UI.Pair;
import petrinets.UI.view.View;
import petrinets.UI.view.ViewStringConstants;
import petrinets.domain.NetLogic;
import petrinets.domain.net.INet;
import petrinets.domain.net.Net;
import systemservices.PropertiesInitializationException;

import java.io.IOException;
import java.util.List;

public class NetUIController extends AbstractUIController {
    private NetLogic netLogic;

    public NetUIController(View view) throws ReflectiveOperationException, IOException, PropertiesInitializationException {
        super(view);
        netLogic = new NetLogic();
        iNetLogic = netLogic;
    }


    public void manageNetCreation(){
        //Il metodo createNet della classe Model non crea la rete se vi e' gia' una rete con questo nome
        String netName = view.readNotEmptyString(ViewStringConstants.INSERT_NET_NAME_MSG);
        if(!netLogic.createNet(netName)) {
            view.printToDisplay(ViewStringConstants.ERR_MSG_NET_NAME_ALREADY_EXIST);
            view.mainMenu();
        }else {
            addFluxRel();
        }
    }

    public void addFluxRel(){
        String placeName = view.readNotEmptyString(ViewStringConstants.INSERT_PLACE_MSG).trim();
        String transitionName;
        
        do {
        	transitionName = view.readNotEmptyString(ViewStringConstants.INSERT_TRANSITION_MSG).trim();
        	if(transitionName.equals(placeName))
        		view.printToDisplay(String.format(ViewStringConstants.ERR_TRANSITION_AS_PLACE, transitionName));
        }while(transitionName.equals(placeName));
        	
        
        int direction = view.getIntInput(ViewStringConstants.INSERT_DIRECTION_MSG
                        + String.format(ViewStringConstants.FLUX_DIRECTION_MSG, 0, placeName, transitionName)
                        + String.format(ViewStringConstants.FLUX_DIRECTION_MSG, 1, transitionName, placeName) + "\n > ",
                0, 1);

        if (netLogic.transitionIsNotPointed(placeName,transitionName,direction)) {
            view.printToDisplay(ViewStringConstants.ERR_MSG_NOT_POINTED_TRANSITION);
            addFluxRel();
        }else {
            if(checkPlace(placeName) && checkTransition(transitionName)) {
                if(!netLogic.addFluxElem(placeName, transitionName, direction)) {
                    view.printToDisplay(ViewStringConstants.ERR_MSG_FLUX_ELEM_ALREADY_EXSISTS);
                }            
            }
            continueAddingElement();
        }

    }

    public boolean checkPlace(String name) {
        if(netLogic.containsTransition(name)) {
            view.printToDisplay(String.format(ViewStringConstants.ERR_PLACE_AS_TRANSITION, name));
            return false;
        }
        return true;
    }


    public boolean checkTransition(String name) {
        if(netLogic.containsPlace(name)) {
            view.printToDisplay(String.format(ViewStringConstants.ERR_TRANSITION_AS_PLACE, name));
            return false;
        }
        return true;
    }

    public void continueAddingElement() {
        boolean userchoice = view.userInputContinueAdding(ViewStringConstants.CONTINUE_ADDING_QUESTION);
        if(userchoice) {
            addFluxRel();
        }else
            userSavingChoice();
    }

    public void userSavingChoice() {
        switch (view.saveMenu()) {
            case 0:
                saveAndExit();
                break;
            //salva la rete
            case 1: {
                //addNet: salva la rete nella Lista
                if(!netLogic.saveCurrentNet()) {
                    view.printToDisplay(ViewStringConstants.ERR_NET_ALREADY_PRESENT);
                }

                view.mainMenu();
                break;
            }
            // case 2: //Ritorna al menu senza salvare
            default: {
                view.mainMenu();
            }
        }
    }

    public void requestPrintNet(String netname) {
        if(netLogic.containsNet(netname)) {
            Net net = (Net) netLogic.getINet(netname);
            List<String> placesname = netLogic.getPlaces(net);
            List<String> transitionsname = netLogic.getTransitions(net);
            List<Pair<String,String>> fluxrelations = netLogic.getFluxRelation(net);
            view.printNet(netname, placesname, transitionsname, fluxrelations);
        }
        else {
            view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
        }
        view.mainMenu();
    }

    public void manageNetsVis() {
        if (netLogic.getSavedNetsNames().isEmpty()) {
            view.printToDisplay(ViewStringConstants.ERR_NO_NET_SAVED);
        } else {
            view.visualizeNets(netLogic.getSavedNetsNames());
            requestPrintNet(view.readNotEmptyString(ViewStringConstants.INSERT_NET_TO_VIEW));
        }
    }

    @Override
    public void importNet(INet importedNet) throws ClassCastException{
        if(! (importedNet instanceof Net)) throw new ClassCastException();
        netLogic.saveINet(importedNet);
    }

    public List<String> getNets(){
        return netLogic.getSavedNetsNames();
    }
}
