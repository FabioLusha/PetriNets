package petrinets.UI.controller;

import petrinets.UI.View;

import java.io.IOException;

public abstract class AbstractConfigurationController {
    protected View view;

    public abstract void permanentSave() throws IOException;
    public void saveAndExit() {
        try {
            permanentSave();
        } catch(IOException e) {
            view.printToDisplay(e.getMessage());
        }
        view.loginMenu();
    }


}
