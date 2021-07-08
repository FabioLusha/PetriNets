package petrinets.UI.controller;

import petrinets.UI.View;
import petrinets.UI.ViewStringConstants;
import petrinets.domain.AbstractINetLogic;
import petrinets.domain.net.INet;
import petrinets.domain.net.Net;
import petrinets.domain.petrinet.PetriNet;
import petrinets.domain.petrinet.PriorityPetriNet;
import systemservices.NetImportExport;

import java.util.*;
import java.io.IOException;

public abstract class AbstractConfigurationController {
    protected View view;
    protected AbstractINetLogic iNetLogic;

    public AbstractConfigurationController(View view){
        this.view = view;
    }

    public void saveAndExit() {
        try {
            iNetLogic.permanentSave();
        } catch(IOException e) {
            view.printToDisplay(e.getMessage());
        }
        view.loginMenu();
    }

    //TODO handle the exception
    public void exportNet() throws ClassNotFoundException {

        List<String> savedNetsName = iNetLogic.getSavedGenericNetsNames(INet.class.getName());
        if (savedNetsName.isEmpty()) {
            view.printToDisplay(ViewStringConstants.ERR_NO_NET_SAVED);
        } else {
            view.visualizeNets(savedNetsName);
            String netName = view.readNotEmptyString(ViewStringConstants.INSERT_NET_NAME_TO_EXPORT);
            if(savedNetsName.contains(netName)) {
                try {
                    NetImportExport.exportINet(iNetLogic.getINet(netName));
                    view.printToDisplay(ViewStringConstants.MSG_EXPORT_COMPLETED);
                } catch (IOException e) {
                    view.printToDisplay(ViewStringConstants.ERR_NET_EXPORT + e.getMessage());
                }
            }else
                view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
        }
    }

    public void importNetInteraction(){
        view.printToDisplay(ViewStringConstants.MSG_DIR);
        String fileName = view.readNotEmptyString(ViewStringConstants.INSERT_NET_NAME_IMPORT);
        try {
            INet importedNet = NetImportExport.importINet(fileName);
            importNet(importedNet);
        } catch (IOException e) {
            view.printToDisplay(ViewStringConstants.ERR_NET_IMPORT + e.getMessage());
        }

    }
    public abstract void importNet(INet importedNet);

}
