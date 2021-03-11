package analyzer;

public class Grammar {

    public static final String NET_GRAMMAR =
            "<net-description> ::= <init-stat> <stat-list> <end-stat>\n" +
            "<init-stat> ::= net <\\ID> begin\n" +
            "<end-stat> ::= end\n" +
            "<stat-list> ::= <stat> | <stat> <stat-list>\n" +
            "<stat> ::= <flux-stat> ; | <\\comment>\n" +
            "<flux-stat> ::= <type> <id> -> <flux-stat> | <type> <id> -> <id>\n" +
            "<type> ::= transition | place\n" +
            "<id> ::= { <list-id> } | <\\ID>\n" +
            "<list-id> ::= <\\ID> , <list-id> | <\\ID>\n" +
            "<\\comment> ::=#.*(\\n)?\n" +
            "<\\ID> ::=" + LexicalAnalyzer.ID_PATTERN + "\n";

    public static final String INSTRUCTION_GRAMMAR =
            "<program> ::= <instruction-list>\n" +
            "<instruction-list> ::= <instruction> <instruction-list> | <instruction>\n" +
            "<instruction> ::= <net-description> | <view-ins> | <erase-ins> | <view-pred-succ-ins>\n"+
            "<view-ins> ::= view places of <\\ID> | view transictions of <\\ID>\n" +
            "<erase-ins> ::= erase <id> from <\\ID>\n" +
            "<view-pred-succ-ins> ::= viewpred of <\\ID> in <\\ID>| viewsucc <\\ID> of in <\\ID>\n" +
            NET_GRAMMAR;


    private String grammar;

}
