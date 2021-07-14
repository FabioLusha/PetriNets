package systemservices;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesHandler {
    public static final Path PROPERTIES_PATH = FileSystems.getDefault().getPath("properties");
    public static final Path REPO_PROPERTIES_PATH = PROPERTIES_PATH.resolve(INetRepository.class.getName() + ".properties");
    public static final Path DEFAULT_SAVING_PATH = FileSystems.getDefault().getPath("data","nets.xml");
    public static final Path DEFAULT_EXPORT_DIR = FileSystems.getDefault().getPath("exportedNet");
    public static final Path NET_EXPORT_PROPERTIES_PATH = PROPERTIES_PATH.resolve(INetExporter.class.getName() + ".properties");

    public static final Path TEST_PROPERTIES_PATH = FileSystems.getDefault().getPath("properties","test");
    public static final Path TEST_REPO_PROPERTIES_PATH = TEST_PROPERTIES_PATH.resolve(INetRepository.class.getName() + ".properties");
    public static final Path DEFAULT_TEST_SAVING_PATH = FileSystems.getDefault().getPath("test","data", "test_nets.xml");

    public static final String NET_REPOSITORY_CLASS_NAME_PROPERTY = "NetRepository.class.name";
    public static final String FILE_NAME_PROPERTY = "filename";
    public static final String EXPORT_DIR_PROPERTY = "export.directory";
    public static final String ERR_REPO_PROP = "Errore nell'inizializzazione del file delle proprietà per il sistema di persistenza";
    public static final String ERR_EXPORT_PROP = "Errore nell'inizializzazione del file delle proprietà per la gestione delle reti esportate";

    public static void initializeProperties() throws PropertiesInitializationException {
        initializeRepositoryProperties();
        initializeExportProperties();
    }

    public static void initializeRepositoryProperties() throws PropertiesInitializationException {
        PROPERTIES_PATH.toFile().mkdir();
        Path repoPropPath = PROPERTIES_PATH.resolveSibling(REPO_PROPERTIES_PATH);

        try (OutputStream out = new FileOutputStream(repoPropPath.toFile())) {

            Properties prop = new Properties();
            prop.setProperty(NET_REPOSITORY_CLASS_NAME_PROPERTY, Archive.class.getName());
            prop.setProperty(FILE_NAME_PROPERTY, PropertiesHandler.DEFAULT_SAVING_PATH.toString());
            prop.store(out, null);

        } catch (IOException e) {
            throw new PropertiesInitializationException(ERR_REPO_PROP);
        }
    }

    public static void initializeExportProperties() throws PropertiesInitializationException {
        PROPERTIES_PATH.toFile().mkdir();
        Path netExportPropDir = PROPERTIES_PATH.resolveSibling(NET_EXPORT_PROPERTIES_PATH);

        try (OutputStream out = new FileOutputStream(netExportPropDir.toFile())) {
            Properties prop = new Properties();
            prop.setProperty(EXPORT_DIR_PROPERTY, DEFAULT_EXPORT_DIR.toString());
            prop.store(out, null);
        } catch (IOException e) {
            throw new PropertiesInitializationException(ERR_EXPORT_PROP);
        }
    }

    public static void initializeTestProperties() throws PropertiesInitializationException {
        TEST_PROPERTIES_PATH.toFile().mkdirs();
        Path repoPropPath = TEST_REPO_PROPERTIES_PATH;

        try (OutputStream out = new FileOutputStream(repoPropPath.toFile())) {

            Properties prop = new Properties();
            prop.setProperty(NET_REPOSITORY_CLASS_NAME_PROPERTY, Archive.class.getName());
            prop.setProperty(FILE_NAME_PROPERTY, PropertiesHandler.DEFAULT_TEST_SAVING_PATH.toString());
            prop.store(out, null);

        } catch (IOException e) {
            throw new PropertiesInitializationException(ERR_REPO_PROP);
        }
    }

}
