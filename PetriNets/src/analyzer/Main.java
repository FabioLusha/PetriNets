package analyzer;

import MVC.Controller;
import MVC.Model;

public class Main {
	public static void main(String[] args) {
		/*
		UserMenuFSA menu = UserMenuFSA.START;
		while (true) {
			menu = menu.stepNext();
		}

		 */
		Model model = new Model();
		Controller controller = new Controller(model);
		controller.startView();
	}
}
