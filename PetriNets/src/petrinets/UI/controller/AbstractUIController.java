package petrinets.UI.controller;

import petrinets.UI.view.View;
import petrinets.UI.view.ViewStringConstants;
import petrinets.domain.AbstractINetLogic;
import petrinets.domain.net.INet;
import systemservices.INetExporter;
import systemservices.INetImporter;
import systemservices.PropertiesInitializationException;

import java.util.*;
import java.io.IOException;

public abstract class AbstractUIController {
    protected View view;
    protected AbstractINetLogic iNetLogic;

    public AbstractUIController(View view){
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
    public void exportNet() {

        List<String> savedNetsName = iNetLogic.getSavedGenericNetsNames(INet.class.getName());

        if (savedNetsName.isEmpty()) {
            view.printToDisplay(ViewStringConstants.ERR_NO_NET_SAVED);
        } else {
            view.visualizeNets(savedNetsName);
            String netName = view.readNotEmptyString(ViewStringConstants.INSERT_NET_NAME_TO_EXPORT);
            if(savedNetsName.contains(netName)) {
                try {
                    INetExporter.exportINet(iNetLogic.getINet(netName));
                    view.printToDisplay(ViewStringConstants.MSG_EXPORT_COMPLETED);
                } catch (IOException | PropertiesInitializationException e) {
                    view.printToDisplay(ViewStringConstants.ERR_NET_EXPORT + e.getMessage());
                }
            }else
                view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
        }
    }

    public abstract void importNet(INet importedNet) throws BaseNetNotPresentException;

}
