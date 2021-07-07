package petrinets.UI.controller;

import petrinets.UI.View;
import petrinets.UI.ViewStringConstants;
import petrinets.domain.NetLogic;

import java.io.IOException;

public class NetConfigurationController {
    private NetLogic netLogic;
    private View view;

    public void manageNetCreation(String netName){
        //Il metodo createNet della classe Model non crea la rete se vi e' gia' una rete con questo nome
        if(!netLogic.createNet(netName)) {
            view.printToDisplay(ViewStringConstants.ERR_MSG_NET_NAME_ALREADY_EXIST);
            view.mainMenu();
        }else {
            view.addFluxElement();
        }
    }

    public void addFluxRel(String place, String transitions, int direction){

        if (netLogic.transitionIsNotPointed(place,transitions,direction)) {
            view.printToDisplay(ViewStringConstants.ERR_MSG_NOT_POINTED_TRANSITION);
            view.addFluxElement();
        }else {
            if(checkPlace(place) && checkTransition(transitions)) {
                if(!netLogic.addFluxElem(place, transitions, direction)) {
                    view.printToDisplay(ViewStringConstants.ERR_MSG_FLUX_ELEM_ALREADY_EXSISTS);
                }
                continueAddingElement();
            }
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
            view.addFluxElement();
        }else
            view.saveMenu();
    }

    public void userSavingChoice(int userchoice) {
        switch (userchoice) {
            case 0:
                netLogic.permanentSave();
                Controller.saveAndExit();
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


}
