package authentication;


import java.util.HashMap;
import java.util.Map;

public class Systems {
	//TODO Look for all systems and return the one where id=system identifier
	public static  Map getSystem(String id) {	
	      		
		Map<String,String> system = null;

		system = new HashMap<String,String>();
		system.put("identifier", "SKSYSTEM2");
		system.put("keybase64", "7vjTsO0IhSZsNA6ze37Dk/xXw2nphFM9ZAMUkwXgaAA=");
		if (id.equals(system.get("identifier").toString())) {
		return system;
		}
		return null;
		
	}

}
