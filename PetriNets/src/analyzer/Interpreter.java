package analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.Net;

public class Interpreter {
	SemanticAnalyzer semanalyzer = new SemanticAnalyzer();

    enum State {

        inizio(
                new ArrayList<>(Arrays.asList("<init-stat>", "<view-ins>", "<erase-ins>", "<view-pred-succ-ins>"))
        ){
            public State nextStep(String command){
                if(command.equalsIgnoreCase("<init-stat>")) return dichiarata_rete;
                else return inizio;
            }
        },

        dichiarata_rete(
                new ArrayList<>(Arrays.asList("<stat-list>", "<stat>"))
        ){

            public State nextStep(String command){
                return rete_non_vuota;
            }
        },
        rete_non_vuota(
                new ArrayList<>(Arrays.asList("<stat-list>", "<stat>", "<end-stat>"))
        ){

            public State nextStep(String command){
                if (command.equalsIgnoreCase("<end-stat>"))
                    return inizio;
                else
                    return rete_non_vuota;
            }
        };

        public List<String> commands;

        State(ArrayList<String> strings) {
            commands = strings;
        }

        public abstract State nextStep(String command);
    }

    public State state;

    public Interpreter(){
        state = State.inizio;
        semanalyzer = new SemanticAnalyzer();
    }

    public State nextStep(String command){
        state = state.nextStep(command);
        return state;
    }
    
    public void toDoCommand(String command, String sentence) {
    	switch(command) {
    	case "<init-stat>" : {
    		semanalyzer.toDoInit_Stat(sentence);
    		break;
    	}
    	case "<stat-list>" : {
    		semanalyzer.toDoStat_List(sentence);
    		break;
    	}
    	case "<view-ins>" : {
    		
    	}
    	
    	default : {
    		System.out.println("Nessun comando");
    		break;
    	}
    	}
    }
    
    public Net returnNet() {
		return semanalyzer.returnNet();
	}
	


}
