package petrinets.UI.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import petrinets.UI.Pair;
import petrinets.UI.View;
import petrinets.UI.ViewStringConstants;
import petrinets.domain.Model;
import petrinets.domain.PetriNet;

import petrinets.domain.PriorityPetriNet;
import petrinets.domain.net.INet;
import petrinets.domain.net.Net;
import systemservices.NetImportExport;


public class Starter {

	private PriorityPetriNetConfigurationController priorityPetriNetConfigurationController;
	private Model model;
    private View view;
    private NetConfigurationController configNetContr;
    private PetriNetConfigurationController configPetriNetContr;

    public Starter(PrintWriter out){
		this.view = new View(this,out);
    	try {
			this.model = new Model();
			System.out.println("Creato Model");
			//TODO Implementare i casi per la creazione della repository
		}catch(Exception e){
    		view.printToDisplay(ViewStringConstants.ERR_MSG_DESERIALIZATION_FAILED);
		}
    	configNetContr = new NetConfigurationController();
    	configPetriNetContr = new PetriNetConfigurationController();
		try {
			priorityPetriNetConfigurationController = new PriorityPetriNetConfigurationController();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			//TODO
		} catch (IOException e) {
			e.printStackTrace();
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
				new SimulatorController(view, this, model);
				break;
			default:
				view.loginMenu();
				break;			
		}
		
	}

	public void startView(){
        view.loginMenu();
    }

    public void mainMenuChoice(int menuchoice) {
		switch (menuchoice) {
			case 1:
				view.initializeNet();
				break;
			case 2:
			{
				manageNetsVis();
				view.mainMenu();
				break;
			}
			case 3:
				managePetriNetCreation();
				view.petriNetMenu();
				break;
			case 4:
				if(managePetriNetVis())
					requestPrintPetriNet(view.readNotEmptyString(ViewStringConstants.INSERT_NET_TO_VIEW));
				view.mainMenu();
				break;
			case 5:
				priorityPetriNetConfigurationController.managePriorityPetriNetCreation();
				view.priorityPetriNetMenu();
				break;
			case 6:
				if(priorityPetriNetConfigurationController.managePriorityPetriNetVis())
					priorityPetriNetConfigurationController.requestPrintPriorityPetriNet(view.readNotEmptyString(ViewStringConstants.INSERT_NET_TO_VIEW));
				view.mainMenu();
				break;
			case 7:
				exportNet();
				view.mainMenu();
				break;
				
			case 8:
				importNet();
				view.mainMenu();
				break;
			default:
				saveAndExit();
	
		}
	}



	public void manageNetCreation(String netName){
   		configNetContr.manageNetCreation(netName);
    }

    public void addFluxRel(String place, String transitions, int direction){
 		configNetContr.addFluxRel(place,transitions,direction);
    }


    public void userSavingChoice(int userchoice) {
		configNetContr.userSavingChoice(userchoice);
    }
    
    public void requestPrintNet(String netname) {
		configNetContr.requestPrintNet(netname);
	}

	public void manageNetsVis() {
		configNetContr.manageNetsVis();
	}

	public void closeApp(){
    	System.exit(0);
	}

	public void saveAndExit() {
		try {
			model.permanentSave();
		} catch(IOException e) {
			view.printToDisplay(e.getMessage());
		}
		view.loginMenu();
	}
    

    public void petriNetMenuChoice(int choice) {
		switch (choice){
			case 0:
				saveAndExit();
				break;
			//modifica vlaore maracatura
			case 1:
				changeMarc();
				view.petriNetMenu();
				break;
				//modifica valore pesi rel flusso
			case 2:
				changeFluxRelationVal();
				view.petriNetMenu();
				break;
			case 3:
				visualizeCurrentPetriNet();
				view.petriNetMenu();
				break;
			case 4:
				if(!model.saveCurrentPetriNet()) {
					view.printToDisplay(ViewStringConstants.ERR_NET_ALREADY_PRESENT);
				}
				
				view.mainMenu();
				break;
			case 5:
				view.mainMenu();
				break;
			default:
				view.petriNetMenu();
				break;
		}
	}

	//PARTE RETRI NET
    public void managePetriNetCreation() {
    	String petrinetname;
    	String netname;
    	
		view.visualizeNets(model.getSavedNetsNames());
    	netname = view.readNotEmptyString(ViewStringConstants.INSERT_NET_NAME_MSG);
    	
    	if(model.containsINet(netname)) {
    		petrinetname = view.readNotEmptyString(ViewStringConstants.INSERT_PETRI_NET_NAME_MSG);
    		if(!model.createPetriNet(petrinetname, netname)) {
				view.printToDisplay(ViewStringConstants.ERR_MSG_NET_NAME_ALREADY_EXIST);
				view.mainMenu();
			}
    		
    	}else {
    		view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
    		view.mainMenu();
    	}

    	view.printToDisplay(ViewStringConstants.PETRI_NET_INITIALIZED_DEFAULT);
    }

	public void visualizeCurrentPetriNet() {
    	printPetriNet(model.getCurrentPetriNet());
	}

	public boolean managePetriNetVis() {
		if (model.getSavedPetriNetsNames().isEmpty()) {
			view.printToDisplay(ViewStringConstants.ERR_NO_NET_SAVED);
			return false;
		} else {
			view.visualizeNets(model.getSavedPetriNetsNames());
			return true;
		
		}
	}
	
	

	public void requestPrintPetriNet(String netname) {
		if(model.containsPetriNet(netname)) {
			PetriNet petriNet = (PetriNet) model.getINet(netname);
			printPetriNet(petriNet);
		}
		else {
			view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
		}
	}

	public void printPetriNet(PetriNet petriNet){

    	String netname = petriNet.getName();
		List<String> placesname = model.getPlaces(petriNet);
		List<String> transitionsname = model.getTransitions(petriNet);
		List<Pair<String,String>> fluxrelations = model.getFluxRelation(petriNet);
		List<String> marc = model.getMarc(petriNet);
		List<String> values = model.getValues(petriNet);

		view.printPetriNet(netname, placesname, marc, transitionsname, fluxrelations, values);
	}
	
	public void changeMarc() {

		PetriNet currentPetriNet = model.getCurrentPetriNet();
		
		List<String> placesname = model.getPlaces(currentPetriNet);
		List<String> marc = model.getMarc(currentPetriNet);

		view.printToDisplay(view.marcFormatter(placesname, marc));

		String name = view.readNotEmptyString(ViewStringConstants.INSERT_PLACE_NAME_TO_MODIFY);

		if(!model.inetContainsPlace(currentPetriNet, name))
			view.printToDisplay(ViewStringConstants.ERR_PLACE_NOT_PRESENT);	
		else {
			int newValue  = view.getNotNegativeInt(ViewStringConstants.INSERT_NEW_MARC);
			model.changeMarc(name, newValue);
		}
	}
	
	public void changeFluxRelationVal() {

	}

	//PARTE RETI DI PETRI CON PRIORITA'
	public void priorityPetriNetMenuChoice(int choice) {
		priorityPetriNetConfigurationController.priorityPetriNetMenuChoice(choice);
	}

	private void visualizeCurrentPriorityPetriNet() {
		priorityPetriNetConfigurationController.visualizeCurrentPriorityPetriNet();
	}

	private void printPriorityPetriNet(PriorityPetriNet priorityPetriNet) {

		priorityPetriNetConfigurationController.printPriorityPetriNet(priorityPetriNet);
	}

	public void managePriorityPetriNetCreation(){

		priorityPetriNetConfigurationController.managePriorityPetriNetCreation();
	}

	public void changePriority(){

		priorityPetriNetConfigurationController.changePriority();
	}

	public boolean managePriorityPetriNetVis() {
		return priorityPetriNetConfigurationController.managePriorityPetriNetVis();
	}

	public void requestPrintPriorityPetriNet(String netname) {
		priorityPetriNetConfigurationController.requestPrintPriorityPetriNet(netname);
	}
	
	
	
	
	
	
	
	//Versione 5
	
	public void exportNet() {
		List<String> savedNetsName = model.getSavedNetsNames();
		savedNetsName.addAll(model.getSavedPetriNetsNames());
		savedNetsName.addAll(model.getSavedPriorityPetriNetsNames());

		if (savedNetsName.isEmpty()) {
			view.printToDisplay(ViewStringConstants.ERR_NO_NET_SAVED);
		} else {
			view.visualizeNets(savedNetsName);
			String netName = view.readNotEmptyString(ViewStringConstants.INSERT_NET_NAME_TO_EXPORT);
			if(savedNetsName.contains(netName)) {
				try {
					NetImportExport.exportINet(model.getINet(netName));
					view.printToDisplay(ViewStringConstants.MSG_EXPORT_COMPLETED);
				} catch (IOException e) {
					view.printToDisplay(ViewStringConstants.ERR_NET_EXPORT + e.getMessage());
				}
			}else
				view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
		}
	}
	
	
	public void importNet() {
		view.printToDisplay(ViewStringConstants.MSG_DIR);
		String fileName = view.readNotEmptyString(ViewStringConstants.INSERT_NET_NAME_IMPORT);
		try {
			INet importedNet = NetImportExport.importINet(fileName);
			if(importedNet instanceof PetriNet) {
				PetriNet importedPetriNet = (PetriNet) importedNet;
				if(basedNetExsists(importedPetriNet))
					model.saveINet(importedPetriNet);
				else
					view.printToDisplay(ViewStringConstants.ERR_MSG_BSDNET_NOTPRESENT);
			} else if(importedNet instanceof PriorityPetriNet) {
				PriorityPetriNet importedPriorityPetriNet = (PriorityPetriNet) importedNet;
				if(basedPetriNetExsists(importedPriorityPetriNet)) {
					model.saveINet(importedPriorityPetriNet);
				} else
					view.printToDisplay(ViewStringConstants.ERR_MSG_BSDNET_NOTPRESENT);
			} else if (importedNet instanceof Net) {
				model.saveINet(importedNet);
			}
			
		} catch (IOException e) {
			view.printToDisplay(ViewStringConstants.ERR_NET_IMPORT + e.getMessage());
		}
	}
	
	public boolean basedNetExsists(PetriNet importedNet) {
			if(model.containsNet(importedNet.getBasedNet().getName())) {
				return true;
			}
				return false;
			
	}
	
	public boolean basedPetriNetExsists(PriorityPetriNet importedNet) {
		if(model.containsPetriNet(importedNet.getBasedPetriNet().getName())) {
			return true;
		}
			return false;
		
}
	
}
