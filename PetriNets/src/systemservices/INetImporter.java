package systemservices;


import java.io.File;
import java.io.IOException;

import petrinets.domain.net.INet;

public class INetImporter {
    private static final String FILE_NOT_PRESENT_MSG = "File inesistente";
	private static final String FILE_EMPTY_MSG = "File vuoto";

	public static INet importINet(String fileName) throws IOException {
		if(new File(fileName).exists()) {
			Serializer<INet> xmlManager = new XMLmanager<>(fileName);
			if (xmlManager.isEmpty()) {
				throw new IOException(FILE_EMPTY_MSG);
			}
			return xmlManager.deserialize();
		} else
			throw new IOException(FILE_NOT_PRESENT_MSG);
	}

}
