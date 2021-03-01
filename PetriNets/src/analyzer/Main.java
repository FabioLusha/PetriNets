package analyzer;

public class Main {
    public static void main(String[] args){
        System.out.println( LexicalAnalyzer.isID("{thisx"));
        String[] a = "aaa c0".split("->");
        System.out.println("\\\\");

        bnfHolder bnf = new bnfHolder(SyntaxAnalyzer.GRAMMAR);
        System.out.println(bnf.toString());
        String[] str = " a b \n c".split(" ");
        for(String s : str){
            System.out.println(s);
        }

        System.out.println( bnf.matchGeneric(" { this}-  > {t1 }", "{this } ->{ t1    \n}"));
        //SyntaxAnalyzer.belongsTo("net rete1 begin n1 -> n2 -> m3; end Re", SyntaxAnalyzer.GRAMMAR);

    }
}
