package petrinets.domain;

import java.io.IOException;

import petrinets.domain.net.Net;

public class NetLogic extends AbstractINetLogic{
	private Net controlNet;
	
	
	public NetLogic() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean saveCurrentNet() {
		if(netArchive.containsValue(controlNet)) {
	           return false;
	       }
	        netArchive.add(controlNet.getName(), controlNet);
	        //pulisco la variabile di comodo per evidenziare eventuali errori
	        controlNet = null;

	        return true;
	}
	
	 public boolean createNet(String name){
	        if(netArchive.contains(name))
	            return false;
	        else{
	            controlNet = new Net(name);
	        }
	        return true;
	    }

}
