package petrinets;

import petrinets.UI.controller.Starter;
import systemservices.PropertiesHandler;


public class Main {
	public static void main(String[] args) {


		Starter starter = new Starter(System.in, System.out);
		starter.startView();
	}

}
