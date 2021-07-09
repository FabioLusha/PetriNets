package it.unibs.fp.mylib;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BufferedInputDati {

    private final static String ERRORE_FORMATO = "Attenzione: il dato inserito non e' nel formato corretto";
    private final static String ERRORE_MINIMO= "Attenzione: e' richiesto un valore maggiore o uguale a ";
    private final static String ERRORE_STRINGA_VUOTA= "Attenzione: non hai inserito alcun carattere";
    private final static String ERRORE_MASSIMO= "Attenzione: e' richiesto un valore minore o uguale a ";
    private final static String MESSAGGIO_AMMISSIBILI= "Attenzione: i caratteri ammissibili sono: ";

    private final static char RISPOSTA_SI='S';
    private final static char RISPOSTA_NO='N';


    private Scanner lettore;
    private PrintWriter outputWriter;

    public BufferedInputDati(InputStream in, OutputStream out){
        lettore = new Scanner(in);
        lettore.useDelimiter(System.lineSeparator());
        outputWriter = new PrintWriter(new BufferedOutputStream(out));
    }


    public String leggiStringa (String messaggio)
    {
        print(messaggio);
        return lettore.next();
    }

    public String leggiStringaNonVuota(String messaggio)
    {
        boolean finito=false;
        String lettura = null;
        do
        {
            lettura = leggiStringa(messaggio);
            lettura = lettura.trim();
            if (lettura.length() > 0)
                finito=true;
            else
                println(ERRORE_STRINGA_VUOTA);
        } while (!finito);

        return lettura;
    }

    public char leggiChar (String messaggio)
    {
        boolean finito = false;
        char valoreLetto = '\0';
        do
        {
            print(messaggio);
            String lettura = lettore.next();
            if (lettura.length() > 0)
            {
                valoreLetto = lettura.charAt(0);
                finito = true;
            }
            else
            {
                println(ERRORE_STRINGA_VUOTA);
            }
        } while (!finito);
        return valoreLetto;
    }

    public char leggiUpperChar (String messaggio, String ammissibili)
    {
        boolean finito = false;
        char valoreLetto = '\0';
        do
        {
            valoreLetto = leggiChar(messaggio);
            valoreLetto = Character.toUpperCase(valoreLetto);
            if (ammissibili.indexOf(valoreLetto) != -1)
                finito  = true;
            else
                println(MESSAGGIO_AMMISSIBILI + ammissibili);
        } while (!finito);
        return valoreLetto;
    }


    public int leggiIntero (String messaggio)
    {
        boolean finito = false;
        int valoreLetto = 0;
        do
        {
            print(messaggio);
            try
            {
                valoreLetto = lettore.nextInt();
                finito = true;
            }
            catch (InputMismatchException e)
            {
                println(ERRORE_FORMATO);
                String daButtare = lettore.next();
            }
        } while (!finito);
        return valoreLetto;
    }

    public int leggiInteroPositivo(String messaggio)
    {
        return leggiInteroConMinimo(messaggio,1);
    }

    public int leggiInteroNonNegativo(String messaggio)
    {
        return leggiInteroConMinimo(messaggio,0);
    }


    public int leggiInteroConMinimo(String messaggio, int minimo)
    {
        boolean finito = false;
        int valoreLetto = 0;
        do
        {
            valoreLetto = leggiIntero(messaggio);
            if (valoreLetto >= minimo)
                finito = true;
            else
                println(ERRORE_MINIMO + minimo);
        } while (!finito);

        return valoreLetto;
    }

    public int leggiIntero(String messaggio, int minimo, int massimo)
    {
        boolean finito = false;
        int valoreLetto = 0;
        do
        {
            valoreLetto = leggiIntero(messaggio);
            if (valoreLetto >= minimo && valoreLetto<= massimo)
                finito = true;
            else
            if (valoreLetto < minimo)
                println(ERRORE_MINIMO + minimo);
            else
                println(ERRORE_MASSIMO + massimo);
        } while (!finito);

        return valoreLetto;
    }


    public double leggiDouble (String messaggio)
    {
        boolean finito = false;
        double valoreLetto = 0;
        do
        {
            print(messaggio);
            try
            {
                valoreLetto = lettore.nextDouble();
                finito = true;
            }
            catch (InputMismatchException e)
            {
                println(ERRORE_FORMATO);
                String daButtare = lettore.next();
            }
        } while (!finito);
        return valoreLetto;
    }

    public double leggiDoubleConMinimo (String messaggio, double minimo)
    {
        boolean finito = false;
        double valoreLetto = 0;
        do
        {
            valoreLetto = leggiDouble(messaggio);
            if (valoreLetto >= minimo)
                finito = true;
            else
                println(ERRORE_MINIMO + minimo);
        } while (!finito);

        return valoreLetto;
    }


    public boolean yesOrNo(String messaggio)
    {
        String mioMessaggio = messaggio + "("+RISPOSTA_SI+"/"+RISPOSTA_NO+")";
        char valoreLetto = leggiUpperChar(mioMessaggio,String.valueOf(RISPOSTA_SI)+String.valueOf(RISPOSTA_NO));

        if (valoreLetto == RISPOSTA_SI)
            return true;
        else
            return false;
    }

    public void  println(Object message){
        outputWriter.println(message);
        outputWriter.flush();
    }

    public void print(Object message){
        outputWriter.print(message);
        outputWriter.flush();
    }
}
