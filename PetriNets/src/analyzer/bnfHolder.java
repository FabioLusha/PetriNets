package analyzer;

import java.util.*;

public class bnfHolder {
    /*
        axiom = astrazione di più alto livello, non-terminale radice
        grammar = grammatica del linguaggio, ovvero l'insieme delle regole di produzione
         */
    private Map<String, List<String>> bnf;
    private Set<String> nonTerminals;
    private String axiom;
    public static final char SPACES = '\n' | '\t' | ' ';

    public bnfHolder(String grammar){
        String[] tmp = grammar.split("\\n");
        bnf = new LinkedHashMap<>();
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

    public boolean matchGeneric(String sentence, String option){

        if(option.isEmpty()) return true;

        sentence = sentence.trim();
        option = option.trim();

        if(sentence.startsWith("#"))
            //se la frase è un commento allora analizzo la successiva linea
            sentence = sentence.substring(sentence.indexOf("\n"));

        for(int i = 0, j = 0; i < sentence.length() && i < option.length(); i++, j++) {
            if(option.charAt(j) == '<') {
                String nonTerminal = option.substring(j, option.indexOf(">") + 1);
                return matchNonTerminal(sentence.substring(i), production(nonTerminal));
            }
            char sentenceChar = sentence.charAt(i);
            char optionChar = option.charAt(j);

            //rimozione spazi vuoti
            /*
            ~ inverti i bit dell'argomento (0111 -> 1000)
            SPACES rappresenta l'or bit a bit dei byte che indicano gli spazi (\t, \n,  )
             */
            while((sentenceChar & ~SPACES) == 0){
                i++;
                sentenceChar = sentence.charAt(i);
            }
            while((optionChar & ~SPACES) == 0){
                j++;
                optionChar = option.charAt(j);
            }

            if( sentenceChar != optionChar){
               return false;
            }
        }
        return true;

    }

}
