import java.rmi.RemoteException;

public class SafeEntryOfficerimpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryOfficer{

	protected SafeEntryOfficerimpl() throws RemoteException {
		super();
	}

	@Override
	public void addLocation(String location) throws RemoteException {
		System.out.println("NEW LOCATION :" + location);
		
	}
	

}
