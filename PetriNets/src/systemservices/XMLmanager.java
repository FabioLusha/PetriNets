package systemservices;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLmanager<T> implements Serializer<T>{

	private String filename;
	private File file;

	public XMLmanager(String pfilename) throws IOException {
		this.filename = pfilename;
		file = new File(filename);
		if(file.getParentFile() != null)
			file.getParentFile().mkdirs();
		
		file.createNewFile();
	}

	public T deserialize() throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		XMLDecoder decoder = new XMLDecoder(fis);
		T savednets = (T) decoder.readObject();
		decoder.close();
		fis.close();
		return savednets;
	}

	public void serialize(T netlist) throws IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		XMLEncoder encoder = new XMLEncoder(fos);
		encoder.setExceptionListener(e -> System.out.println("Exception! :" + e.toString()));

		encoder.writeObject(netlist);
		encoder.close();
		fos.close();
	}

	public boolean isEmpty() {
		
			if (file.length() != 0) {
				return false;
			} else 
				return true;
	}

}
