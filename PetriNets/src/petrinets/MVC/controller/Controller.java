package petrinets.MVC.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import petrinets.MVC.Model;
import petrinets.MVC.Pair;
import petrinets.MVC.View;
import petrinets.MVC.ViewStringConstants;
import petrinets.domain.PetriNet;

import petrinets.domain.PriorityPetriNet;
import petrinets.domain.net.Net;


public class Controller {


	
	private Model model;
    private View view;

    public Controller(PrintWriter out){
		this.view = new View(this,out);
    	try {
			this.model = new Model();
		}catch(IOException e){
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
				break;
			}
			case 3:
				managePetriNetCreation();
				view.petriNetMenu();
				break;
			case 4:
				if(menagePetriNetVis())
					requestPrintPetriNet(view.readNotEmptyString(ViewStringConstants.INSERT_NET_TO_VIEW));
				view.mainMenu();
				break;
			case 5:
				removeNet();
				view.mainMenu();
				break;
			default:
				saveAndExit();
	
		}
	}

	public void manageNetCreation(String netName){
    	//Il metodo createNet della classe Model non crea la rete se vi e' gi� una rete con questo nome
        if(!model.createNet(netName)) {
            view.printToDisplay(ViewStringConstants.ERR_MSG_NET_NAME_ALREADY_EXIST);
            view.mainMenu();
        }else {
        	view.addFluxElement();
        }
    }

    public void addFluxRel(String place, String transitions, int direction){

        if (model.transitionIsNotPointed(place,transitions,direction)) {
        	view.printToDisplay(ViewStringConstants.ERR_MSG_NOT_POINTED_TRANSITION);
        	view.addFluxElement();
        }else {
        	if(checkPlace(place) && checkTransition(transitions)) {
        		if(!model.addFluxElem(place, transitions, direction)) {
        			view.printToDisplay(ViewStringConstants.ERR_MSG_FLUX_ELEM_ALREADY_EXSISTS);
        		}
        		continueAddingElement();
        	}
        }

    }
    
    public void continueAddingElement() {
    	boolean userchoice = view.userInputContinueAdding(ViewStringConstants.CONTINUE_ADDING_QUESTION);
    	if(userchoice) {
    		view.addFluxElement();
    	}else
    		view.saveMenu();
    }
    
    public boolean checkPlace(String name) {
    	if(model.containsTransition(name)) {
    		view.printToDisplay(String.format(ViewStringConstants.ERR_PLACE_AS_TRANSITION, name));
    		return false;
    	}
    	return true;
    }
    
    
    public boolean checkTransition(String name) {
    	if(model.containsPlace(name)) {
    		view.printToDisplay(String.format(ViewStringConstants.ERR_TRANSITION_AS_PLACE, name));
    		return false;
    	}
    	return true;
    }
    
    public void userSavingChoice(int userchoice) {
		switch (userchoice) {
		case 0:
			saveAndExit();
			break;
			//salva la rete
		case 1: {
			//addNet: salva la rete nella Lista
			if(!model.saveCurrentNet()) {
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
		if(model.containsNet(netname)) {
			Net net = (Net) model.getINet(netname);
			List<String> placesname = model.getPlaces(net);
			List<String> transitionsname = model.getTransitions(net);
			List<Pair<String,String>> fluxrelations = model.getFluxRelation(net);
			view.printNet(netname, placesname, transitionsname, fluxrelations);
		}
		else {
			view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
		}
		view.mainMenu();
	}

	private void manageNetsVis() {
		if (model.getSavedNetsNames().isEmpty()) {
			view.printToDisplay(ViewStringConstants.ERR_NO_NET_SAVED);
			view.mainMenu();
		} else {
			view.visualizeNets(model.getSavedNetsNames());
			requestPrintNet(view.readNotEmptyString(ViewStringConstants.INSERT_NET_TO_VIEW));
		}
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
    
    public void removeNet(){
    	String name = view.readNotEmptyString(ViewStringConstants.INSERT_NET_NAME_TO_REMOVE);
    	if(model.containsINet(name)){
    		model.remove(name);
    	}else
    		view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
    }



    public void petriNetMenuChoice(int choice) {
		switch (choice){
			case 0:
				saveAndExit();
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
    		if(!model.createPetriNet(petrinetname, (Net) model.getINet(netname))) {
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

	public boolean menagePetriNetVis() {
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

		if(!model.petriNetContainsPlace(name)) 
			view.printToDisplay(ViewStringConstants.ERR_PLACE_NOT_PRESENT);	
		else {
			int newValue  = view.getNotNegativeInt(ViewStringConstants.INSERT_NEW_MARC);
			model.changeMarc(name, newValue);
		}
	}
	
	public void changeFluxRelationVal() {

		var currentPetriNet = model.getCurrentPetriNet();
		var relFluxName = model.getFluxRelation(currentPetriNet);
		var values = model.getValues(currentPetriNet);
		view.printToDisplay(view.fluxrelFormatter(relFluxName, values));
		
		String placeName = view.readNotEmptyString(ViewStringConstants.INSERT_PLACE_MSG).trim();
    	String transitionName = view.readNotEmptyString(ViewStringConstants.INSERT_TRANSITION_MSG).trim();

    	int direction = view.getIntInput(ViewStringConstants.INSERT_DIRECTION_MSG
				+ String.format(ViewStringConstants.FLUX_DIRECTION_MSG, 0, placeName, transitionName)
				+ String.format(ViewStringConstants.FLUX_DIRECTION_MSG, 1, transitionName, placeName) + "\n > ",
				0, 1);
    	
		if(!model.containsFluxRel(placeName, transitionName, direction))
			view.printToDisplay(ViewStringConstants.ERR_FLUX_REL_NOT_PRESENT);	
		else {
			int newValue  = view.getNotNegativeInt(ViewStringConstants.INSERT_NEW_VAL);
			model.changeFluxRelVal(placeName, transitionName, direction, newValue);
		}
	}

	//PARTE RETI DI PETRI CON PRIORITA'
	public void priorityPetriNetMenuChoice(int choice) {
		switch (choice){
			case 0:
				saveAndExit();
				//modifica vlaore maracatura
			case 1:
				changePriority();
				view.priorituPetriNetMenu();
				break;
			//visualizza la rete
			case 2:
				visualizeCurrentPriorityPetriNet();
				view.priorityPetriNetMenu();
				break;
			case 3:
				if(!model.saveCurrentPriorityPetriNet()) {
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

	private void visualizeCurrentPriorityPetriNet() {
    	printPriorityPetriNet(model.getCurrentPriorityPetriNet());
	}

	private void printPriorityPetriNet(PriorityPetriNet currentPriorityPetriNet) {
		//TODO
	}

	public void managePriorityPetriNetCreation(){
		String pPetriNetName;
		String petriNetName;

		view.visualizeNets(model.getSavedPetriNetsNames());
		petriNetName = view.readNotEmptyString(ViewStringConstants.INSERT_PETRI_NET_NAME_MSG);

		if(model.containsINet(petriNetName)) {
			pPetriNetName = view.readNotEmptyString(ViewStringConstants.INSERT_PRIORITY_PETRI_NET_NAME_MSG);
			if(!model.createPriorityPetriNet(pPetriNetName, (PetriNet) model.getINet(petriNetName))) {
				view.printToDisplay(ViewStringConstants.ERR_MSG_NET_NAME_ALREADY_EXIST);
				view.mainMenu();
			}

		}else {
			view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
			view.mainMenu();
		}

		view.printToDisplay(ViewStringConstants.PETRI_NET_INITIALIZED_DEFAULT);
	};
	
}
