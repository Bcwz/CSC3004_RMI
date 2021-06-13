/*
	Code: SafeEntry Interface	SafeEntry.java

	The SafeEntry interface provides a description of the
	 methods available as part of the service provided
	by the remote object SafeEntryimpl. Note carefully that the interface
	extends remote and each methods throws a remote exception.
*/

import java.util.ArrayList;
import com.mongodb.client.MongoCollection;

import classes.FamilyMembers;
import classes.InfectedLocations;
import classes.Transactions;
import classes.Users;

public interface SafeEntryUser extends java.rmi.Remote  {

	public boolean selfCheckIn(Transactions transaction, MongoCollection<Transactions> collection)
			throws java.rmi.RemoteException;

	public boolean selfCheckOut(Transactions transaction, MongoCollection<Transactions> collection)
			throws java.rmi.RemoteException;

	public boolean groupCheckIn(ArrayList<Transactions> transactionList, MongoCollection<Transactions> collection)
			throws java.rmi.RemoteException;

	public boolean groupCheckOut(ArrayList<Transactions> transactionList, MongoCollection<Transactions> collection)
			throws java.rmi.RemoteException;

	public ArrayList<Transactions> viewHistory(Users user, MongoCollection<Transactions> collection)
			throws java.rmi.RemoteException;

	public ArrayList<InfectedLocations> retrieveAllInfectedLocations(MongoCollection<InfectedLocations> collection)
			throws java.rmi.RemoteException;

	public boolean addFamilyMembers(FamilyMembers familyMember, MongoCollection<FamilyMembers> collection)
			throws java.rmi.RemoteException;

	public boolean checkExistingFamilyMember(FamilyMembers familyMember, MongoCollection<FamilyMembers> collection)
			throws java.rmi.RemoteException;

	public boolean removeFamilyMembers(FamilyMembers familyMember, MongoCollection<FamilyMembers> collection)
			throws java.rmi.RemoteException;

	public ArrayList<FamilyMembers> retrieveAllFamilyMembers(Users user, MongoCollection<FamilyMembers> collection)
			throws java.rmi.RemoteException;

	public boolean checkExistingUser(Users user, MongoCollection<Users> collection) throws java.rmi.RemoteException;

	public boolean createAccount(Users user, MongoCollection<Users> collection) throws java.rmi.RemoteException;

	public Users login(Users user, MongoCollection<Users> collection) throws java.rmi.RemoteException;

	public String notifyUsernotifyUser(Users user, MongoCollection<Transactions> transactionCollection,
			MongoCollection<InfectedLocations> infectedCollection) throws java.rmi.RemoteException;

}
