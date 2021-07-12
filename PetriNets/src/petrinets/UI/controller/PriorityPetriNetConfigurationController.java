package petrinets.UI.controller;

import petrinets.UI.Pair;
import petrinets.UI.view.View;
import petrinets.UI.view.ViewStringConstants;
import petrinets.domain.net.INet;
import petrinets.domain.petrinet.PetriNet;
import petrinets.domain.petrinet.PriorityPetriNet;
import petrinets.domain.PriorityPetriNetLogic;
import systemservices.PropertiesInitializationException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PriorityPetriNetConfigurationController extends AbstractConfigurationController{
    private PriorityPetriNetLogic priorityPetriNetLogic;

    public PriorityPetriNetConfigurationController(View view) throws ReflectiveOperationException,IOException, PropertiesInitializationException {
        super(view);
        priorityPetriNetLogic = new PriorityPetriNetLogic();
        iNetLogic = priorityPetriNetLogic;
    }


    public void priorityPetriNetMenuChoice() {
        switch (view.priorityPetriNetMenu()) {
            case 0:
                saveAndExit();
                break;
            //modifica vlaore priorità
            case 1:
                changePriority();
                view.priorityPetriNetMenu();
                break;
            //visualizza la rete
            case 2:
                visualizeCurrentPriorityPetriNet();
                view.priorityPetriNetMenu();
                break;
            case 3:
                if (!priorityPetriNetLogic.saveCurrentNet()) {
                    view.printToDisplay(ViewStringConstants.ERR_NET_ALREADY_PRESENT);
                }
                view.mainMenu();
                break;
            case 4:
                view.mainMenu();
                break;
            default:
                view.petriNetMenu();
                break;
        }
    }

    public void visualizeCurrentPriorityPetriNet() {
        printPriorityPetriNet(priorityPetriNetLogic.getCurrentPriorityPetriNet());
    }

    public void printPriorityPetriNet(PriorityPetriNet priorityPetriNet) {
        String netname = priorityPetriNet.getName();
        List<String> placesname = priorityPetriNetLogic.getPlaces(priorityPetriNet);
        List<Pair<String, String>> fluxrelations = priorityPetriNetLogic.getFluxRelation(priorityPetriNet);
        List<String> marc = priorityPetriNetLogic.getMarc(priorityPetriNet);
        List<String> values = priorityPetriNetLogic.getValues(priorityPetriNet);
        Map<String, Integer> transAndPriorities = priorityPetriNetLogic.getPriorityMapInString(priorityPetriNet);

        view.printPriorityPetriNet(netname, placesname, marc, transAndPriorities, fluxrelations, values);
    }

    public void managePriorityPetriNetCreation() {
        String pPetriNetName;
        String petriNetName;

        view.visualizeNets(priorityPetriNetLogic.getSavedPetriNetsNames());
        petriNetName = view.readNotEmptyString(ViewStringConstants.INSERT_PETRI_NET_NAME_MSG);

        if (priorityPetriNetLogic.containsINet(petriNetName)) {
            pPetriNetName = view.readNotEmptyString(ViewStringConstants.INSERT_PRIORITY_PETRI_NET_NAME_MSG);
            if (!priorityPetriNetLogic.createPriorityPetriNet(pPetriNetName, (PetriNet) priorityPetriNetLogic.getINet(petriNetName))) {
                view.printToDisplay(ViewStringConstants.ERR_MSG_NET_NAME_ALREADY_EXIST);
                view.mainMenu();
            }

        } else {
            view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
            view.mainMenu();
        }

        view.printToDisplay(ViewStringConstants.PRIORITY_PETRI_NET_INITIALIZED_DEFAULT);
    }

    public void changePriority() {
        PriorityPetriNet currentPriorityPetriNet = priorityPetriNetLogic.getCurrentPriorityPetriNet();

        Map<String, Integer> transPriorities = priorityPetriNetLogic.getPriorityMapInString(currentPriorityPetriNet);

        view.printToDisplay(view.prioritiesFormatter(transPriorities));

        String transName = view.readNotEmptyString(ViewStringConstants.INSERT_TRANSITION_NAME_TO_MODIFY);

        if (!priorityPetriNetLogic.inetContainsTransition(currentPriorityPetriNet, transName))
            view.printToDisplay(ViewStringConstants.ERR_TRANSITION_NOT_PRESENT);
        else {
            int newValue = view.getNotNegativeInt(ViewStringConstants.INSERT_NEW_PRIORITY);
            priorityPetriNetLogic.changePriority(transName, newValue);
        }
    }

    public boolean managePriorityPetriNetVis() {
        List<String> priorityPetriNetNames = priorityPetriNetLogic.getSavedPriorityPetriNetsNames();
        if (priorityPetriNetNames.isEmpty()) {
            view.printToDisplay(ViewStringConstants.ERR_NO_NET_SAVED);
            return false;
        } else {
            view.visualizeNets(priorityPetriNetNames);
            return true;
        }
    }

    public void requestPrintPriorityPetriNet() {
        String netname = view.readFromList(priorityPetriNetLogic.getSavedPriorityPetriNetsNames(),ViewStringConstants.INSERT_PRIORITY_PETRI_NET_NAME_MSG);
        printPriorityPetriNet((PriorityPetriNet) priorityPetriNetLogic.getINet(netname));
    }

    @Override
    public void importNet(INet importedNet) throws ClassCastException, BaseNetNotPresentException {
        if(! (importedNet instanceof PetriNet)) throw new ClassCastException();
        PriorityPetriNet pnpToImport = (PriorityPetriNet) importedNet;
        if(priorityPetriNetLogic.containsINet(pnpToImport.getBasedPetriNet().getName()))
            priorityPetriNetLogic.saveINet(pnpToImport);
        else
            throw new BaseNetNotPresentException();

    }

}