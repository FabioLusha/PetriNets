package systemservices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

public class RepositoryFactory {
    private static final String PROPERTIES_DIR = "properties";
    private static final Path DEFAUTL_SAVING_DIR = FileSystems.getDefault().getPath("data","nets.xml");
    private static final Path DEFAUTL_TEST_SAVING_DIR = FileSystems.getDefault().getPath("data","test_nets.xml");

    private Archive repo;
    private RepositoryFactory instance;

    public RepositoryFactory getInstance(){
        if(instance == null)
            instance = new RepositoryFactory();
        return instance;
    }

    private RepositoryFactory(){
        if(! new File(PROPERTIES_DIR).exists())
            initializaProperties();
        instance = this;
    }

    public Archive getRepo() throws IOException {
        if(repo == null)
            repo = Archive.getInstance(); //TODO Modificare Archive affinché non sia più un Singleton

        try {
            Archive.class.getConstructor(void.class).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return repo;
    }

    public void initializaProperties() {
        Path propDir = FileSystems.getDefault().getPath(PROPERTIES_DIR);
        propDir.toFile().mkdir();
        Path repoPropDir = propDir.resolve(Archive.class.getName() + ".properties");

        try (OutputStream out = new FileOutputStream(repoPropDir.toFile())) {

            Properties prop = new Properties();
            prop.setProperty("directory", DEFAUTL_SAVING_DIR.toString());
            prop.store(out, null);

        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }
    }
}
