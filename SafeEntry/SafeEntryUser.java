import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import classes.Transactions;
import classes.Users;

public interface SafeEntryUser extends java.rmi.Remote {

	public void selfCheckIn(RMIClientIntf client, Transactions checkInTransaction)
			throws java.rmi.RemoteException, UnsupportedEncodingException, FileNotFoundException, IOException;

	public void selfCheckOut(RMIClientIntf client, Transactions checkOutTransaction) throws java.rmi.RemoteException;

	public void groupCheckIn(RMIClientIntf client, ArrayList<Transactions> transactionList)
			throws java.rmi.RemoteException;

	public void groupCheckOut(RMIClientIntf client, ArrayList<Transactions> transactionList)
			throws java.rmi.RemoteException;

	public String viewHistory(RMIClientIntf client, Users userToFind) throws java.rmi.RemoteException;

}
