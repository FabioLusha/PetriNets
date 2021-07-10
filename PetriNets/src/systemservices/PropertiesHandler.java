package systemservices;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesHandler {
    public static final String PROPERTIES_DIR = "properties";
    public static final Path REPO_PROPERTIES_PATH = FileSystems.getDefault().getPath(PROPERTIES_DIR, INetRepository.class.getName() + ".properties");
    public static final Path DEFAULT_SAVING_DIR = FileSystems.getDefault().getPath("data","nets.xml");
    public static final Path DEFAULT_TEST_SAVING_DIR = FileSystems.getDefault().getPath("data","test_nets.xml");
    public static final Path DEFAULT_EXPORT_DIR = FileSystems.getDefault().getPath("exportedNet");
    public static final Path NET_EXPORT_PROPERTIES_PATH = FileSystems.getDefault().getPath(PROPERTIES_DIR, INetExporter.class.getName() + ".properties");


    public static final String NET_REPOSITORY_CLASS_NAME = "NetRepository.class.name";
    public static final String DIRECTORY_PROPERTY = "directory";
    public static final String EXPORT_DIR_PROPERTY = "export.directory";


    public static void initializeProperties() {
        Path propDir = FileSystems.getDefault().getPath(PropertiesHandler.PROPERTIES_DIR);
        propDir.toFile().mkdir();
        Path repoPropDir = propDir.resolveSibling(REPO_PROPERTIES_PATH);

        try (OutputStream out = new FileOutputStream(repoPropDir.toFile())) {

            Properties prop = new Properties();
            prop.setProperty(NET_REPOSITORY_CLASS_NAME, Archive.class.getName());
            prop.setProperty(DIRECTORY_PROPERTY, PropertiesHandler.DEFAULT_SAVING_DIR.toString());
            prop.store(out, null);

        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }

        Path netExportPropDir = propDir.resolveSibling(NET_EXPORT_PROPERTIES_PATH);
        try (OutputStream out = new FileOutputStream(netExportPropDir.toFile())) {
            Properties prop = new Properties();
            prop.setProperty(EXPORT_DIR_PROPERTY, DEFAULT_EXPORT_DIR.toString());
            prop.store(out, null);
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }

    }
}
