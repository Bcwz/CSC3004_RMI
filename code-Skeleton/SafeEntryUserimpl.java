/*
 * SafeEntryimpl - put the the methods for public users here (NOT SafeEntryOfficer)
 *  
 */
public class SafeEntryUserimpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryUser
{

    // Implementations must have an explicit constructor
    // in order to declare the RemoteException exception

    public SafeEntryUserimpl()
        throws java.rmi.RemoteException {
        super();
    }
    
    //SafeEntry checkin method
    public synchronized String checkin(String name)
        throws java.rmi.RemoteException {
    	
		/* Put methods to connect to MongoDB,
		 * save checkin info to MongoDB.
		 * should be able to just copy paste existing code here
		*/
    	
    	return name;
    	
    }
    
    public synchronized String checkout(String name)
            throws java.rmi.RemoteException {
        	return name;
        }
    
    public synchronized String groupcheckin(String name)
            throws java.rmi.RemoteException {
        	return name;
        }
    
    public synchronized String groupcheckout(String name)
            throws java.rmi.RemoteException {
        	return name;
        }
    
    //For adding/removing family members
    public synchronized String addfamily(String name)
            throws java.rmi.RemoteException {
        	return name;
        }
    public synchronized String removefamily(String name)
            throws java.rmi.RemoteException {
        	return name;
        }

}
