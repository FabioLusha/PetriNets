package it.unibs.fp.mylib;

public class MyMenu2 {
    final private static String CORNICE = "--------------------------------";
    final private static String VOCE_USCITA = "0\tEsci";
    final private static String RICHIESTA_INSERIMENTO = "Digita il numero dell'opzione desiderata > ";

    private String titolo;
    private String [] voci;
    private BufferedInputDati io;


    public MyMenu2(BufferedInputDati io, String titolo, String [] voci)
    {
        this.titolo = titolo;
        this.voci = voci;
        this.io = io;
    }

    public int scegli ()
    {
        stampaMenu();
        return io.leggiIntero(RICHIESTA_INSERIMENTO, 0, voci.length);
    }

    public void stampaMenu ()
    {
        io.println(CORNICE);
        io.println(titolo);
        io.println(CORNICE);
        for (int i=0; i<voci.length; i++)
        {
            io.println( (i+1) + "\t" + voci[i]);
        }
        io.println("");
        io.println(VOCE_USCITA);
        io.println("");
    }
}
