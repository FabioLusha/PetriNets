package systemservices;

import java.io.*;
import java.util.Properties;

public class RepositoryFactory {

    private INetRepository repo;
    private static RepositoryFactory instance;

    public static RepositoryFactory getInstance() throws PropertiesInitializationException{
        if(instance == null)
            instance = new RepositoryFactory();

        return instance;
    }

    private RepositoryFactory() throws PropertiesInitializationException {
    		//Durante il testing modificare la proprietà e dopo il testing riportarla sempre a quella originale
        if(! PropertiesHandler.REPO_PROPERTIES_PATH.toFile().exists())
            PropertiesHandler.initializeRepositoryProperties();
    }

    public INetRepository getRepo() throws IOException, ReflectiveOperationException {
        if(repo == null ) {
            try (FileInputStream inputStream = new FileInputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile())) {
                Properties repoProp = new Properties();
                repoProp.load(inputStream);

                String className = repoProp.getProperty(PropertiesHandler.NET_REPOSITORY_CLASS_NAME_PROPERTY);
                repo = (INetRepository) Class.forName(className).getDeclaredConstructor().newInstance();

                repo.readFromFile(repoProp.getProperty(PropertiesHandler.FILE_NAME_PROPERTY));
            } catch (IOException  e){
                throw e;
            }
        }

        return repo;
    }
    
    


}
