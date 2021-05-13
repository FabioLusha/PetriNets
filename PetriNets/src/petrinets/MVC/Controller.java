package petrinets.MVC;

import java.io.IOException;
import java.util.List;

public class Controller {


	private Model model;
    private View view;

    public Controller(){
		this.view = new View(this);
    	try {
			this.model = new Model();
		}catch(Exception e){
    		view.printToDisplay(ViewStringConstants.ERR_MSG_DESERIALIZATION_FAILED);
		}
    }

    public void startView(){
        view.mainMenu();
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
        		view.userInputContinueAdding();;
        	}
        }

    }
    
    public void continueAddingElement(boolean userchoice) {
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
			exit();
			break;
			//salva la rete
		case 1: {
			//addNet: salva la rete nella Lista
			if(!model.addNet()) {
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
				menagePetriNetVis();
				break;
			default:
				exit();

    	}
    }

	private void manageNetsVis() {
		if (model.getSavedNetsNames().isEmpty()) {
			view.printToDisplay(ViewStringConstants.ERR_SAVED_NET_NOT_PRESENT);
			view.mainMenu();
		} else {
			view.visualizeNets(model.getSavedNetsNames());
			requestPrintNet(view.getInput(ViewStringConstants.INSERT_NET_TO_VIEW));
		}
	}

	private void exit() {
		try {
			model.permanentSave();
		} catch(IOException e) {
			view.printToDisplay(e.getMessage());
		}
		System.exit(0);
	}
    
    public void requestPrintNet(String netname) {
    	if(model.containsNet(netname)) {
    		List<String> placesname = model.getPlaces(netname);
    		List<String> transitionsname = model.getTransitions(netname);
    		List<Pair<String,String>> fluxrelations = model.getFluxRelation(netname);
    		view.printNet(netname, placesname, transitionsname, fluxrelations);
    		view.mainMenu();
    	}
    	else {
    		view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
    		view.visualizeNets(model.getSavedNetsNames());
    	}
    }



    //PARTE RETRI NET
    public void managePetriNetCreation() {
    	String petrinetname;
    	String netname;
    	
    	//TODO
		view.visualizeNets(model.getSavedNetsNames());
    	netname = view.getInput(ViewStringConstants.INSERT_NET_NAME_MSG);
    	
    	if(model.containsNet(netname)) {

    		petrinetname = view.getInput(ViewStringConstants.INSERT_PETRI_NET_NAME_MSG);
    		if(!model.createPetriNet(petrinetname, model.getNet(netname))) {
				view.printToDisplay(ViewStringConstants.ERR_MSG_NET_NAME_ALREADY_EXIST);
				view.mainMenu();
			}
			model.saveCurrentPetriNet();
    	}else {
    		view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
    		view.mainMenu();
    	}

    	view.printToDisplay(ViewStringConstants.PETRI_NET_INITIALIZED_DEFAULT);
    }

	public void petriNetMenuChoice(int choice) {
		switch (choice){
			case 0:
				exit();
			//modifica vlaore maracatura
			case 1:
				//TODO
				break;
				//modifica valore pesi rel flusso
			case 2:
				//TODO;
				break;
			case 3:
				visualizeCurrentPetriNet();
				break;
			case 4:
				model.saveCurrentPetriNet();
				break;
			case 5:
				model.remove(model.getCurrentPetriNetName());
				view.mainMenu();
				break;
			default:
				view.petriNetMenu();
				break;
		}
	}

	private void visualizeCurrentPetriNet() {
    	requestPrintPetriNet(model.getCurrentPetriNetName());
	}

	private void menagePetriNetVis() {
		if (model.getSavedPetriNetsNames().isEmpty()) {
			view.printToDisplay(ViewStringConstants.ERR_SAVED_NET_NOT_PRESENT);
			view.mainMenu();
		} else {
			view.visualizeNets(model.getSavedPetriNetsNames());
			requestPrintPetriNet(view.getInput(ViewStringConstants.INSERT_NET_TO_VIEW));
		}
	}

	public void requestPrintPetriNet(String netname) {
		if(model.containsNet(netname)) {
			List<String> placesname = model.getPlaces(netname);
			List<String> transitionsname = model.getTransitions(netname);
			List<Pair<String,String>> fluxrelations = model.getFluxRelation(netname);
			var marc = model.getMarc(netname);
			var values = model.getValues(netname);
			view.printPetriNet(netname, placesname, marc, transitionsname, fluxrelations, values);
			view.petriNetMenu();
		}
		else {
			view.printToDisplay(ViewStringConstants.ERR_NET_NOT_PRESENT);
			view.visualizeNets(model.getSavedPetriNetsNames());
		}
	}
}
