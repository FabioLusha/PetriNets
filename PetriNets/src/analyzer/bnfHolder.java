package analyzer;

import java.util.*;

public class bnfHolder {
    /*
    axiom = astrazione di più alto livello, non-terminale radice
    grammar = grammatica del linguaggio, ovvero l'insieme delle regole di produzione
     */
    private HashMap<String, List<String>> bnf;
    private Set<String> nonTerminals;
    private String axiom;

    public bnfHolder(String grammar){
        String[] tmp = grammar.split("\\n");
        bnf = new HashMap<>();
        nonTerminals = new HashSet<>();
        axiom = tmp[0].split("->")[0].replaceAll("[ \\t]+","");

        for(String str : tmp){
            String[] strList = str.split("->");
            //elimino i spazi
            String lhs = strList[0].replaceAll("[ \\t]+","");

            //se split non trova la regex in input ritorna l'intera stringa
            String[] rhs = strList[1].split("\\|");
            for(int i = 0; i < rhs.length; i++)
                rhs[i] = rhs[i].replaceAll("[ \\t]+"," ");

            bnf.put(lhs, Arrays.asList(rhs));
            nonTerminals.add(lhs);
        }

    }

    public String toString(){
        StringBuffer output = new StringBuffer();

        for(Map.Entry<String, List<String>> entry : bnf.entrySet()){
            output.append(entry.getKey() + "->");
            for(int i = 0; i < entry.getValue().size(); i++){
                if(i > 0 ) output.append("|");
                output.append(entry.getValue().get(i));
            }
            output.append("\n");
        }

        return output.toString();
    }

    public boolean isTerminal(String word){
        return !nonTerminals.contains(word);
    }

    public List<String> production(String lhs){
        return bnf.get(lhs);
    }

    public String getRoot(){
        return axiom;
    }

    public boolean belongsTo(String sentence){
        return matchNonTerminal(sentence, production(axiom));
    }


    private boolean matchNonTerminal(String sentence, List<String> lhs) {

        for(String option : lhs){
            if(matchGeneric(sentence, option))
                return true;
        }
        //ATTENZIONE, MESSO SOLO PER DEBUG
        return false;
    }

    private boolean matchGeneric(String sentence, String option){

        if(sentence.startsWith(" "))
            sentence = sentence.substring(1);
        if(option.startsWith(" "))
            option = option.substring(1);

        if(option.isEmpty()) return true;
        
        String[] rhsSymbols = option.split(" ");
        for(int i = 0; i < rhsSymbols.length; i++) {
            if (isTerminal(rhsSymbols[i])){
                    String firstElement = sentence.split(" ")[0];
                    if(rhsSymbols[i].equals("ID")) {
                        firstElement = firstElement.replaceAll("[}{]", "");
                        if(LexicalAnalyzer.isID(firstElement))
                            //fai il match del restante della stringa
                            matchGeneric(sentence.substring(firstElement.length()), option.substring("ID".length()));
                    }
                    else if(rhsSymbols[i].equalsIgnoreCase(firstElement))
                             matchGeneric(sentence.substring(firstElement.length()), option.substring(firstElement.length()));
                    else return false;
                }
            else matchNonTerminal(sentence, production(rhsSymbols[i]));
        }
        return false;

    }

}
