package systemservices;

import petrinets.domain.net.INet;

public class INetExportFormat  implements ExportFormat<INet, INet>{
    private INet inet;

    public INetExportFormat(INet pinet){
        inet = pinet;
    }

    @Override
    public INet format() {
        //Vuoto, non facciamo alcuna particolare formatazzione per la versione attuale
        return inet;
    }

    @Override
    public INet reverseFormat() {
        return inet;
    }
}
