package systemservices;


import petrinets.domain.net.INet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

public class INetExporter {

	public static void exportINet(INet inet) throws IOException, PropertiesInitializationException {

		if (!PropertiesHandler.NET_EXPORT_PROPERTIES_PATH.toFile().exists())
			PropertiesHandler.initializeExportProperties();

		String exportDirectory;
		try (FileInputStream inputStream = new FileInputStream(PropertiesHandler.NET_EXPORT_PROPERTIES_PATH.toFile())) {
			Properties exportNetProp = new Properties();
			exportNetProp.load(inputStream);

			exportDirectory = exportNetProp.getProperty(PropertiesHandler.EXPORT_DIR_PROPERTY);
		} catch (IOException  e){
			throw e;
		}

		Path fileName = FileSystems.getDefault().getPath(exportDirectory, inet.getName().replaceAll("[ /\\.,:;*#@]", "_"));
		File theDir = fileName.getParent().toFile();

		if (!theDir.exists()){
		    theDir.mkdirs();
		}


		Serializer<INet> xmlManager = new XMLmanager<>(fileName.toString());
		xmlManager.serialize(inet);
		
	}

}
