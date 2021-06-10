/*
	Code: Calculator Interface	calculator.java

	The calculator interface provides a description of the 5 remote
	arithmetic methods available as part of the service provided
	by the remote object calculatorimpl. Note carefully that the interface
	extends remote and each methods throws a remote exception.
*/


public interface SafeEntry
          extends java.rmi.Remote {


    public String checkin(String name)
        throws java.rmi.RemoteException;
    
    public String checkout(String name)
            throws java.rmi.RemoteException;
    
    public String groupcheckin(String name)
            throws java.rmi.RemoteException;
    
    public String groupcheckout(String name)
            throws java.rmi.RemoteException;
    
    public String addfamily(String name)
            throws java.rmi.RemoteException;
    
    public String removefamily(String name)
            throws java.rmi.RemoteException;


}
