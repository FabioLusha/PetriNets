package systemservices;

import java.io.*;
import java.util.Properties;

public class RepositoryFactory {

    private INetRepository repo;
    private RepositoryFactory instance;

    public RepositoryFactory getInstance(){
        if(instance == null)
            instance = new RepositoryFactory();
        return instance;
    }

    private RepositoryFactory(){
        if(! new File(PropertiesHandler.PROPERTIES_DIR).exists())
            PropertiesHandler.initializeProperties();
        instance = this;
    }

    public INetRepository getRepo() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(repo == null ) {
            //repo = Archive.getInstance(); //TODO Modificare Archive affinché non sia più un Singleton
            try (FileInputStream inputStream = new FileInputStream(PropertiesHandler.REPO_PROPERTIES.toFile())) {
                Properties repoProp = new Properties();
                repoProp.load(inputStream);
                String className = repoProp.getProperty(PropertiesHandler.NET_REPOSITORY_CLASS_NAME);
                repo = (INetRepository) Class.forName(className).newInstance();
                repo.readFromFile(repoProp.getProperty(PropertiesHandler.DIRECTORY_PROPERTY));
            } catch (IOException e){
                throw e;
            }
        }

        return repo;
    }


}
