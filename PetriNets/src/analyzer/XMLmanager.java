package analyzer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import net.Net;
import net.Place;

public class XMLmanager {

	private String filename;

	public XMLmanager(String pfilename) {
		this.filename = pfilename;
	}

	public Map<String, Net> deserializeFromXML() throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		XMLDecoder decoder = new XMLDecoder(fis);
		Map<String, Net> savednets = (Map<String, Net>) decoder.readObject();
		decoder.close();
		fis.close();
		return savednets;
	}

	public void serializeToXML(Map<String, Net> netlist) throws IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		XMLEncoder encoder = new XMLEncoder(fos);
		encoder.setExceptionListener(e -> System.out.println("Exception! :" + e.toString()));

		encoder.writeObject(netlist);
		encoder.close();
		fos.close();
	}

	public boolean isEmpty() {
		File file = new File(filename);

		if (file.length() != 0) {
			return false;
		} else
			return true;
	}

}
