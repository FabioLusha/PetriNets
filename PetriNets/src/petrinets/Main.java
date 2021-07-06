package petrinets;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;

import petrinets.UI.controller.Controller;
import systemservices.PropertiesHandler;


public class Main {
	public static void main(String[] args) {
		PropertiesHandler.initializeProperties();
		Controller controller = new Controller(new PrintWriter(new BufferedOutputStream(System.out)));
		controller.startView();
	}
}
