package petrinets.UI.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import petrinets.UI.View;
import petrinets.UI.ViewStringConstants;
import petrinets.domain.Model;
import petrinets.domain.net.INet;
import petrinets.domain.petrinet.PetriNet;

import petrinets.domain.petrinet.PriorityPetriNet;
import systemservices.NetImportExport;


public class Starter {


	private Model model;
    private View view;
    private NetConfigurationController configNetContr;
    private PetriNetConfigurationController configPetriNetContr;
	private PriorityPetriNetConfigurationController configPriorityPNContr;

    public Starter(PrintWriter out){
		this.view = new View(this,out);
    	try {
			this.model = new Model();
			System.out.println("Creato Model");
			//TODO Implementare i casi per la creazione della repository
		}catch(Exception e){
    		view.printToDisplay(ViewStringConstants.ERR_MSG_DESERIALIZATION_FAILED);
		}

		try {
			configNetContr = new NetConfigurationController(view);
			configPetriNetContr = new PetriNetConfigurationController(view);
			configPriorityPNContr = new PriorityPetriNetConfigurationController(view);
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
				//new SimulatorController(view, this, model);
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
				configPriorityPNContr.managePriorityPetriNetCreation();
				view.priorityPetriNetMenu();
				break;
			case 6:
				if(configPriorityPNContr.managePriorityPetriNetVis())
					configPriorityPNContr.requestPrintPriorityPetriNet(view.readNotEmptyString(ViewStringConstants.INSERT_NET_TO_VIEW));
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

	public void manageNetsVis() {
		configNetContr.manageNetsVis();
	}

	public void closeApp(){
    	System.exit(0);
	}

	public void saveAndExit() {

	}
    

    public void petriNetMenuChoice(int choice) {
		configPetriNetContr.petriNetMenuChoice(choice);
	}

	//PARTE RETRI NET
    public void managePetriNetCreation() {
    	configPetriNetContr.managePetriNetCreation();
    }

	public void visualizeCurrentPetriNet() {
    	configPetriNetContr.visualizeCurrentPetriNet();
	}

	public boolean managePetriNetVis() {
		return configPetriNetContr.managePetriNetVis();
	}
	
	

	public void requestPrintPetriNet(String netname) {
		configPetriNetContr.requestPrintPetriNet(netname);
	}
	
	public void changeMarc() {
		configPetriNetContr.changeMarc();
	}
	
	public void changeFluxRelationVal() {
		configPetriNetContr.changeFluxRelationVal();
	}

	//PARTE RETI DI PETRI CON PRIORITA'
	public void priorityPetriNetMenuChoice(int choice) {
		configPriorityPNContr.priorityPetriNetMenuChoice(choice);
	}

	private void visualizeCurrentPriorityPetriNet() {
		configPriorityPNContr.visualizeCurrentPriorityPetriNet();
	}

	private void printPriorityPetriNet(PriorityPetriNet priorityPetriNet) {

		configPriorityPNContr.printPriorityPetriNet(priorityPetriNet);
	}

	public void managePriorityPetriNetCreation(){

		configPriorityPNContr.managePriorityPetriNetCreation();
	}

	public void changePriority(){
		configPriorityPNContr.changePriority();
	}

	public boolean managePriorityPetriNetVis() {
		return configPriorityPNContr.managePriorityPetriNetVis();
	}

	public void requestPrintPriorityPetriNet(String netname) {
		configPriorityPNContr.requestPrintPriorityPetriNet(netname);
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

			AbstractConfigurationController controllers[] = {configNetContr,configPetriNetContr,configPriorityPNContr};
			for(AbstractConfigurationController controller: controllers){
				try{
					controller.importNet(importedNet);
					//TODO Se il cast è giusto ma la rete base non è presente l'import non ha successo
					view.printToDisplay(ViewStringConstants.SUCCESSFUL_IMPORT);
					return;
				}catch(ClassCastException e){
					continue;
				}
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
