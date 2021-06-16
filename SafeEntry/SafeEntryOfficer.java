import classes.InfectedLocations;


public interface SafeEntryOfficer extends java.rmi.Remote {

	public void retrieveAllInfectedLocations(RMIClientIntf client) throws java.rmi.RemoteException;

	public void addLocation(RMIClientIntf client, InfectedLocations location) throws java.rmi.RemoteException;

	public void notifyClient(RMIClientIntf client) throws java.rmi.RemoteException;

}
