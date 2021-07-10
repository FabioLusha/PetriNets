package petrinets;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.PrintWriter;

import petrinets.UI.controller.Starter;
import systemservices.PropertiesHandler;


public class Main {
	public static void main(String[] args) {
		if(! new File(PropertiesHandler.PROPERTIES_DIR).exists())
			PropertiesHandler.initializeProperties();
		Starter starter = new Starter(System.in, System.out);
		starter.startView();
	}
}
