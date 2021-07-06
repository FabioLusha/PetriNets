package systemservices;

import petrinets.domain.net.INet;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface INetRepository {
    void readFromFile(String filePath) throws IOException;
    void add(String name, INet net);
    boolean contains(String name);
    boolean containsValue(INet inet);
    void permanentSave() throws IOException;
    void removeFromArchive(String key);
    INet get(String netName);
    Collection<INet> getAllElements();


}
