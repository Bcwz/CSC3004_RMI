import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import classes.InfectedLocations;

public interface SafeEntryOfficer extends java.rmi.Remote {

	public void retrieveAllInfectedLocations(RMIClientIntf client)
			throws java.rmi.RemoteException, UnsupportedEncodingException, FileNotFoundException, IOException;

	public void addLocation(RMIClientIntf client, InfectedLocations location)
			throws java.rmi.RemoteException, UnsupportedEncodingException, FileNotFoundException, IOException;
}
