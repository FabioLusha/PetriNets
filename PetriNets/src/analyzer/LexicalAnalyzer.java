package analyzer;

import java.util.regex.Pattern;

public class LexicalAnalyzer {

    static final String[] KEYWORDS = {"net", "start", "end", "->", "{", "}", ","};
    // \\{ per ricercare efftivamente il letterale { altrimenti { assume funzione di metacarattere nella regex
    static final String ID_PATTERN = "[A-Za-z][0-9A-Za-z_]*";

    public static boolean isID(String word){
        return Pattern.matches(ID_PATTERN, word);
    }


    public static boolean isKeyword(String word){

        for( int i = 0; i < KEYWORDS.length; i++)
            if( KEYWORDS[i].equalsIgnoreCase(word)) return true;

        return false;
    }

    public static boolean isLexicalCorrect(String word){
        return isID(word) || isKeyword(word);
    }
}