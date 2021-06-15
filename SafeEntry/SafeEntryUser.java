import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import classes.Transactions;

public interface SafeEntryUser extends java.rmi.Remote {

//    public void selfCheckIn(RMIClientIntf client, String NRIC) throws java.rmi.RemoteException;

	public void selfCheckIn(RMIClientIntf client, Transactions checkInTransaction)
			throws java.rmi.RemoteException, UnsupportedEncodingException, FileNotFoundException, IOException;

	public void selfCheckOut(RMIClientIntf client, Transactions checkOutTransaction) throws java.rmi.RemoteException;

	public void groupCheckIn(RMIClientIntf client, String NRIC) throws java.rmi.RemoteException;

	public void groupCheckOut(RMIClientIntf client, String NRIC) throws java.rmi.RemoteException;

}
