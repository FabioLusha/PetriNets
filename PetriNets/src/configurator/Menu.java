package configurator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu {

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
        public String messaggioiniziale;

        State(ArrayList<String> strings, String pmessaggioiniziale) {
            commands = strings;
            this.messaggioiniziale = pmessaggioiniziale;
        }

        public abstract State nextStep(String command);
    }

    public State state;

    public Menu(){
        state = State.inizio;
    }

    public State nextStep(String command){
        state = state.nextStep(command);
        return state;
    }


}
