package petrinets.MVC;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Controller {

    private Model model;
    private View view;

    public Controller(Model model){
        this.model = model;
        this.view = new View(this);
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
			System.exit(0);
			break;
		case 1: {
			if(!model.addNet()) {
				view.printToDisplay(ViewStringConstants.ERR_NET_ALREADY_PRESENT);
			}
			
			try {
				model.permanentSave();
			} catch(IOException e) {
				view.printToDisplay(e.getMessage());
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
    
    public void menuChoice(int menuchoice) {
    	switch (menuchoice) {
        case 1:
            view.initializeNet();
            break;
        case 2:{
        	if(model.getSavedNetsNames().isEmpty()) {
        		view.printToDisplay(ViewStringConstants.ERR_SAVED_NET_NOT_PRESENT);
        		view.mainMenu();
        	}
        	else
        		view.visualizeNets(model.getSavedNetsNames());
        }
            break;
       
        default:
            System.exit(0);

    }
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
    
}
