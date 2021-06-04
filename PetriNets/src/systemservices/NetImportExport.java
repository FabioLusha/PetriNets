package systemservices;


import java.io.File;
import java.io.IOException;

import petrinets.domain.net.INet;

public class NetImportExport {
    private static String exportedNetDirectory = "exportedNet";
	
	public static void exportINet(INet inet) throws IOException {
		String fileName = exportedNetDirectory + System.getProperty("file.separator") + inet.getName().replaceAll("[ /\\.,:;*#@]", "_");
		
		File theDir = new File(exportedNetDirectory);
		if (!theDir.exists()){
		    theDir.mkdirs();
		}
		
		XMLmanager<INet> xmlManager = new XMLmanager<>(fileName);
		xmlManager.serializeToXML(inet);
		
	}
	
	public static INet importNet(String fileName) throws IOException {
		XMLmanager<INet> xmlManager = new XMLmanager<>(fileName);
		
		return xmlManager.deserializeFromXML();
	}
}
