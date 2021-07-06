package systemservices;

import java.io.*;
import java.util.Properties;

public class RepositoryFactory {

    private INetRepository repo;
    private static RepositoryFactory instance;

    public static RepositoryFactory getInstance(){
        if(instance == null)
            instance = new RepositoryFactory();
        return instance;
    }

    private RepositoryFactory(){
    		//TODO Fare controllo se il file esiste gi‡ non sovrascriverlo
    		//Durante il testing modificare la propriet‡ e dopo il testing riportarla sempre a quella originale
            PropertiesHandler.initializeProperties();
    }

    public INetRepository getRepo() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if(repo == null ) {
            //repo = Archive.getInstance(); //TODO Modificare Archive affinch√© non sia pi√π un Singleton
            try (FileInputStream inputStream = new FileInputStream(PropertiesHandler.REPO_PROPERTIES_PATH.toFile())) {
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
