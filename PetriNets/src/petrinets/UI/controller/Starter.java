package petrinets.UI.controller;

import java.io.IOException;
import java.io.PrintWriter;


import petrinets.UI.view.View;
import petrinets.UI.view.ViewStringConstants;

import petrinets.domain.PetriNetLogic;
import petrinets.domain.net.INet;


import systemservices.NetImportExport;


public class Starter {

    private View view;
    private NetConfigurationController configNetContr;
    private PetriNetConfigurationController configPetriNetContr;
	private PriorityPetriNetConfigurationController configPriorityPNContr;

    public Starter(PrintWriter out){
		this.view = new View(this,out);

		try {
			configNetContr = new NetConfigurationController(view);
			configPetriNetContr = new PetriNetConfigurationController(view);
			configPriorityPNContr = new PriorityPetriNetConfigurationController(view);
		} catch (ReflectiveOperationException e) {
			view.printToDisplay(ViewStringConstants.ERR_INTERNAL);
			//TODO
		} catch (IOException e) {
			view.printToDisplay(ViewStringConstants.ERR_MSG_DESERIALIZATION_FAILED);
		}
	}

    public void logMenuChoice(int scegli) {
		switch(scegli) {
			case 0:
				closeApp();
				break;
			//scelto configuratore
			case 1:
				view.mainMenu();
				break;
			//scelto fruitore
			case 2:
				try {
					new SimulatorController(view, this, new PetriNetLogic());
				} catch (IOException e) {
					view.printToDisplay(ViewStringConstants.ERR_MSG_DESERIALIZATION_FAILED);
				} catch (ReflectiveOperationException e) {
					view.printToDisplay(ViewStringConstants.ERR_INTERNAL);
				}
				break;
			default:
				view.loginMenu();
				break;			
		}
		
	}

	public void startView(){
       view.loginMenu();
    }

    public void mainMenuChoice(int choice) {
		switch (choice) {
			case 1:
				configNetContr.manageNetCreation();
				break;
			case 2:
				manageNetsVis();
				break;
			case 3:
				managePetriNetCreation();
				break;
			case 4:
				requestPrintPetriNet();
				break;
			case 5:
				priorityPetriNetCreation();
				break;
			case 6:
				priorityPetriNetVis();
				break;
			case 7:
				exportNet();
				break;
			case 8:
				importNet();
				break;
			default:
				configNetContr.saveAndExit();
				break;
	
		}
	}


	public void manageNetsVis() {
		configNetContr.manageNetsVis();
		view.mainMenu();
	}

	public void closeApp(){
    	System.exit(0);
	}

	//PARTE RETRI NET
    public void managePetriNetCreation() {
    	configPetriNetContr.managePetriNetCreation();
		configPetriNetContr.petriNetMenuChoice();
    }

	public boolean managePetriNetVis() {
		return configPetriNetContr.managePetriNetVis();
	}

	public void requestPrintPetriNet() {
		if(managePetriNetVis())
			configPetriNetContr.requestPetriNetToPrint();
		view.mainMenu();
	}

	//PARTE RETI DI PETRI CON PRIORITA'


	public void priorityPetriNetCreation() {
		configPriorityPNContr.managePriorityPetriNetCreation();
		configPriorityPNContr.priorityPetriNetMenuChoice();
	}


	private void priorityPetriNetVis() {
		if(configPriorityPNContr.managePriorityPetriNetVis())
			configPriorityPNContr.requestPrintPriorityPetriNet();
		view.mainMenu();
	}
	//Versione 5
	
	public void exportNet() {
		try {
			configNetContr.exportNet();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		view.mainMenu();
	}
	
	
	public void importNet() {
		view.printToDisplay(ViewStringConstants.MSG_DIR);
		String fileName = view.readNotEmptyString(ViewStringConstants.INSERT_NET_NAME_IMPORT);

		try {
			INet importedNet = NetImportExport.importINet(fileName);

			AbstractConfigurationController controllers[] = {configNetContr,configPetriNetContr,configPriorityPNContr};
			for(AbstractConfigurationController controller: controllers){
				try{
					controller.importNet(importedNet);
					view.printToDisplay(ViewStringConstants.SUCCESSFUL_IMPORT);
					return;
				}catch(ClassCastException e){
					continue;
				}catch(BaseNetNotPresentException e){
					view.printToDisplay(ViewStringConstants.ERR_MSG_BSDNET_NOTPRESENT);
					return;
				}
			}

		} catch (IOException e) {
			view.printToDisplay(ViewStringConstants.ERR_NET_IMPORT + e.getMessage());
		}
		view.mainMenu();
	}
}