package petrinets.UI.controller;

import petrinets.UI.Pair;
import petrinets.UI.view.View;
import petrinets.UI.view.ViewStringConstants;
import petrinets.domain.net.INet;
import petrinets.domain.petrinet.PetriNet;
import petrinets.domain.PetriNetLogic;
import systemservices.PropertiesInitializationException;

import java.io.IOException;
import java.util.List;

public class PetriNetConfigurationController extends AbstractConfigurationController{
    private PetriNetLogic petriNetLogic;

    public PetriNetConfigurationController(View view) throws ReflectiveOperationException, IOException, PropertiesInitializationException {
        super(view);
        petriNetLogic = new PetriNetLogic();
        iNetLogic = petriNetLogic;
    }

    public void petriNetMenuChoice() {
        switch (view.petriNetMenu()){
            case 0:
                saveAndExit();
                break;
            //modifica vlaore maracatura
            case 1:
                changeMarc();
                petriNetMenuChoice();
                break;
            //modifica valore pesi rel flusso
            case 2:
                changeFluxRelationVal();
                petriNetMenuChoice();
                break;
            case 3:
                visualizeCurrentPetriNet();
                petriNetMenuChoice();
                break;
            case 4:
                if(!petriNetLogic.saveCurrentNet()) {
                    view.printToDisplay(ViewStringConstants.ERR_NET_ALREADY_PRESENT);
                }
                view.mainMenu();
                break;
            case 5:
                view.mainMenu();
                break;
            default:
                petriNetMenuChoice();;
                break;
        }
    }

    public void managePetriNetCreation() {
        String petrinetname;
        String netname;

        if(petriNetLogic.getSavedNetsNames().isEmpty()){
            view.printToDisplay(ViewStringConstants.ERR_NO_NET_SAVED);
            view.mainMenu();
            return;
        }

        view.visualizeNets(petriNetLogic.getSavedNetsNames());
        netname = view.readFromList(petriNetLogic.getSavedNetsNames(), ViewStringConstants.INSERT_NET_NAME_MSG);

        if(petriNetLogic.containsINet(netname)) {
            petrinetname = view.readNotEmptyString(ViewStringConstants.INSERT_PETRI_NET_NAME_MSG);
            if(!petriNetLogic.createPetriNet(petrinetname, netname)) {
                view.printToDisplay(ViewStringConstants.ERR_MSG_NET_NAME_ALREADY_EXIST);
                view.mainMenu();
                return;
            }

        }else {
            view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
            view.mainMenu();
            return;
        }

        view.printToDisplay(ViewStringConstants.PETRI_NET_INITIALIZED_DEFAULT);
    }

    public void visualizeCurrentPetriNet() {
        printPetriNet(petriNetLogic.getCurrentPetriNet());
    }

    public boolean managePetriNetVis() {
        if (petriNetLogic.getSavedPetriNetsNames().isEmpty()) {
            view.printToDisplay(ViewStringConstants.ERR_NO_NET_SAVED);
            return false;
        } else {
            view.visualizeNets(petriNetLogic.getSavedPetriNetsNames());
            return true;

        }
    }

    public void requestPetriNetToPrint(){
        String petriNetName = view.readFromList(petriNetLogic.getSavedPetriNetsNames(), ViewStringConstants.INSERT_PETRI_NET_NAME_MSG);
        PetriNet petriNet = (PetriNet) petriNetLogic.getINet(petriNetName);
        printPetriNet(petriNet);

    }


    public void printPetriNet(PetriNet petriNet){

        String netname = petriNet.getName();
        List<String> placesname = petriNetLogic.getPlaces(petriNet);
        List<String> transitionsname = petriNetLogic.getTransitions(petriNet);
        List<Pair<String,String>> fluxrelations = petriNetLogic.getFluxRelation(petriNet);
        List<String> marc = petriNetLogic.getMarc(petriNet);
        List<String> values = petriNetLogic.getValues(petriNet);

        view.printPetriNet(netname, placesname, marc, transitionsname, fluxrelations, values);
    }

    public void changeMarc() {

        PetriNet currentPetriNet = petriNetLogic.getCurrentPetriNet();

        List<String> placesname = petriNetLogic.getPlaces(currentPetriNet);
        List<String> marc = petriNetLogic.getMarc(currentPetriNet);

        view.printToDisplay(view.marcFormatter(placesname, marc));

        String name = view.readNotEmptyString(ViewStringConstants.INSERT_PLACE_NAME_TO_MODIFY);

        if(!petriNetLogic.inetContainsPlace(currentPetriNet, name))
            view.printToDisplay(ViewStringConstants.ERR_PLACE_NOT_PRESENT);
        else {
            int newValue  = view.getIntInput(ViewStringConstants.INSERT_NEW_MARC, PetriNet.DEFAULT_MARC_VALUE, Integer.MAX_VALUE);
            petriNetLogic.changeMarc(name, newValue);
        }
    }

    public void changeFluxRelationVal() {

        var currentPetriNet = petriNetLogic.getCurrentPetriNet();
        var relFluxName = petriNetLogic.getFluxRelation(currentPetriNet);
        var values = petriNetLogic.getValues(currentPetriNet);
        view.printToDisplay(view.fluxRelFormatter(relFluxName, values));

        String placeName = view.readNotEmptyString(ViewStringConstants.INSERT_PLACE_MSG).trim();
        String transitionName = view.readNotEmptyString(ViewStringConstants.INSERT_TRANSITION_MSG).trim();

        int direction = view.getIntInput(ViewStringConstants.INSERT_DIRECTION_MSG
                        + String.format(ViewStringConstants.FLUX_DIRECTION_MSG, 0, placeName, transitionName)
                        + String.format(ViewStringConstants.FLUX_DIRECTION_MSG, 1, transitionName, placeName) + "\n > ",
                0, 1);

        if(!petriNetLogic.containsFluxRel(placeName, transitionName, direction))
            view.printToDisplay(ViewStringConstants.ERR_FLUX_REL_NOT_PRESENT);
        else {
            int newValue  = view.getIntInput(ViewStringConstants.INSERT_NEW_COST, PetriNet.DEFAULT_FLUX_VALUE, Integer.MAX_VALUE);
            petriNetLogic.changeFluxRelVal(placeName, transitionName, direction, newValue);
        }
    }

    @Override
    public void importNet(INet importedNet) throws ClassCastException, BaseNetNotPresentException {
        if(! (importedNet instanceof PetriNet)) throw new ClassCastException();

        PetriNet pnToImport = (PetriNet) importedNet;
        if(petriNetLogic.containsINet(pnToImport.getBasedNet().getName()))
            petriNetLogic.saveINet(pnToImport);
        else
            throw new BaseNetNotPresentException();

    }

}
