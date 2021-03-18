package analyzer;

public class Main {
	public static void main(String[] args) {
		UserMenuFSA menu = UserMenuFSA.START;
		while (true) {
			menu = menu.stepNext();
		}
	}
}
