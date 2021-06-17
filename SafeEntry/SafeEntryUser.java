import java.util.ArrayList;

import classes.Transactions;
import classes.Users;

public interface SafeEntryUser extends java.rmi.Remote {

	public void selfCheckIn(RMIClientIntf client, Transactions checkInTransaction) throws java.rmi.RemoteException;

	public void selfCheckOut(RMIClientIntf client, Transactions checkOutTransaction) throws java.rmi.RemoteException;

	public void groupCheckIn(RMIClientIntf client, ArrayList<Transactions> transactionList)
			throws java.rmi.RemoteException;

	public void groupCheckOut(RMIClientIntf client, ArrayList<Transactions> transactionList)
			throws java.rmi.RemoteException;

	public void viewHistory(RMIClientIntf client, Users userToFind) throws java.rmi.RemoteException;

	public void registerClient(String clientNRIC, RMIClientIntf client) throws java.rmi.RemoteException;

}
