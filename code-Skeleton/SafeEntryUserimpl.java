/*
 * SafeEntryimpl - put the the methods for public users here (NOT SafeEntryOfficer)
 *  
 */

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;

import classes.FamilyMembers;
import classes.InfectedLocations;
import classes.Transactions;
import classes.Users;


public class SafeEntryUserimpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryUser
{

    // Implementations must have an explicit constructor
    // in order to declare the RemoteException exception

    public SafeEntryUserimpl()
        throws java.rmi.RemoteException {
        super();
    }
    
    public synchronized boolean selfCheckIn(Transactions transaction, MongoCollection<Transactions> collection)
			throws RemoteException {
		try {

			// Set the Transactions Object Type and CheckInTime
			transaction.setType("Self check-in");
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String formatDateTime = now.format(format);
			transaction.setCheckInTime(formatDateTime);
			transaction.setCheckOutTime(null);

			// Insert the object into the dB
			collection.insertOne(transaction);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public synchronized boolean selfCheckOut(Transactions transaction, MongoCollection<Transactions> collection)
			throws RemoteException {
		try {
			// Set the Transactions Object Type and CheckInTime
			transaction.setType("Self check-in");

			// Create 2 new filters based on the requirements
			Bson nameFilter = Filters.eq("name", transaction.getName());
			Bson locationFilter = Filters.eq("location", transaction.getLocation());
			Bson selfCheckInFilter = Filters.eq("type", transaction.getType());
			Bson checkOutTimeFilter = Filters.eq("checkOutTime", null);

			// Find the Transactions based on these filters
			Transactions userfound = collection
					.find(Filters.and(nameFilter, locationFilter, selfCheckInFilter, checkOutTimeFilter)).first();
			LocalDateTime now = LocalDateTime.now();
			// Set the checkout time
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String formatDateTime = now.format(format);
			userfound.setCheckOutTime(formatDateTime);

			// Update the database
			Document filterByGradeId = new Document("_id", userfound.getId());
			FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions()
					.returnDocument(ReturnDocument.AFTER);
			collection.findOneAndReplace(filterByGradeId, userfound, returnDocAfterReplace);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public synchronized boolean groupCheckIn(ArrayList<Transactions> transactionList,
			MongoCollection<Transactions> collection) throws RemoteException {
		try {
			for (int counter = 0; counter < transactionList.size(); counter++) {

				// Set the Transactions Object Type and CheckInTime
				transactionList.get(counter).setType("Group check-in");
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String formatDateTime = now.format(format);
				transactionList.get(counter).setCheckInTime(formatDateTime);
				transactionList.get(counter).setCheckOutTime(null);

				// Insert the object into the dB
				collection.insertOne(transactionList.get(counter));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public synchronized boolean groupCheckOut(ArrayList<Transactions> transactionList,
			MongoCollection<Transactions> collection) throws RemoteException {
		try {
			for (int counter = 0; counter < transactionList.size(); counter++) {
				// Set the Transactions Object Type
				transactionList.get(counter).setType("Group check-in");
				// Create BSON filters based on the requirements
				Bson nameFilter = Filters.eq("name", transactionList.get(counter).getName());
				Bson locationFilter = Filters.eq("location", transactionList.get(counter).getLocation());
				Bson selfCheckInFilter = Filters.eq("type", transactionList.get(counter).getType());
				Bson checkOutTimeFilter = Filters.eq("checkOutTime", null);

				// Find the user based on these filters
				Transactions userfound = collection
						.find(Filters.and(nameFilter, locationFilter, selfCheckInFilter, checkOutTimeFilter)).first();

				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String formatDateTime = now.format(format);
				userfound.setCheckOutTime(formatDateTime);

				// Find the previous record by ID and update the record
				Document filterByGradeId = new Document("_id", userfound.getId());
				FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions()
						.returnDocument(ReturnDocument.AFTER);
				collection.findOneAndReplace(filterByGradeId, userfound, returnDocAfterReplace);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public synchronized ArrayList<Transactions> viewHistory(Users user, MongoCollection<Transactions> collection)
			throws RemoteException {
		try {
			// Create BSON filters based on the requirements
			Bson nameFilter = Filters.eq("name", user.getName());
			Bson locationFilter = Filters.eq("nric", user.getNric());

			// Find the list of Transactions based on these filters
			MongoCursor<Transactions> cursor = collection.find(Filters.and(nameFilter, locationFilter)).iterator();

			// Stores list of Transactions objects
			ArrayList<Transactions> transactionsList = new ArrayList<Transactions>();
			try {
				while (cursor.hasNext()) {
					transactionsList.add(cursor.next());
				}
			} finally {
				cursor.close();
			}

			return transactionsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public synchronized ArrayList<InfectedLocations> retrieveAllInfectedLocations(
			MongoCollection<InfectedLocations> collection) throws RemoteException {
		try {
			// Find all InfectedLocations database records
			MongoCursor<InfectedLocations> cursor = collection.find().iterator();

			// Stores list of InfectedLocations objects
			ArrayList<InfectedLocations> infectedList = new ArrayList<InfectedLocations>();
			try {
				while (cursor.hasNext()) {
					infectedList.add(cursor.next());
				}
			} finally {
				cursor.close();
			}

			return infectedList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized boolean addFamilyMembers(FamilyMembers familyMember, MongoCollection<FamilyMembers> collection)
			throws RemoteException {
		try {
			// Insert the object into dB
			collection.insertOne(familyMember);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public synchronized boolean checkExistingFamilyMember(FamilyMembers familyMember,
			MongoCollection<FamilyMembers> collection) throws RemoteException {
		// Create BSON filters based on the requirements
		Bson nameFilter = Filters.eq("name", familyMember.getName());
		Bson nricFilter = Filters.eq("nric", familyMember.getNric());
		Bson relatedToFilter = Filters.eq("relatedTo", familyMember.getRelatedTo());

		// Find FamilyMember based on these filters
		FamilyMembers famMember = collection.find(Filters.and(nameFilter, nricFilter, relatedToFilter)).first();

		if (famMember == null) {
			return false;
		} else {
			return true;
		}

	}

	public synchronized boolean removeFamilyMembers(FamilyMembers familyMember,
			MongoCollection<FamilyMembers> collection) throws RemoteException {
		try {
			// Create BSON filters based on the requirements
			Bson nameFilter = Filters.eq("name", familyMember.getName());
			Bson nricFilter = Filters.eq("nric", familyMember.getNric());

			// Delete record based on filters
			collection.deleteOne(Filters.and(nameFilter, nricFilter));

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized ArrayList<FamilyMembers> retrieveAllFamilyMembers(Users user,
			MongoCollection<FamilyMembers> collection) throws RemoteException {
		try {
			// Create BSON filters based on the requirements
			Bson nameFilter = Filters.eq("relatedTo", user.getName());

			// Find the list of Family Members based on these filters
			MongoCursor<FamilyMembers> cursor = collection.find(Filters.and(nameFilter)).iterator();

			// Stores list of Family Members objects
			ArrayList<FamilyMembers> famList = new ArrayList<FamilyMembers>();

			try {
				while (cursor.hasNext()) {
					famList.add(cursor.next());
				}
			} finally {
				cursor.close();
			}

			return famList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized boolean checkExistingUser(Users user, MongoCollection<Users> collection)
			throws RemoteException {

		// Create BSON filters based on the requirements
		Bson nricFilter = Filters.eq("nric", user.getNric());

		// Find the user based on these filters
		Users userFound = collection.find(Filters.and(nricFilter)).first();
		if (userFound == null) {
			return false;
		} else {
			return true;
		}
	}

	public synchronized boolean createAccount(Users user, MongoCollection<Users> collection) throws RemoteException {
		try {

			// Insert the object into the dB
			collection.insertOne(user);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public synchronized Users login(Users user, MongoCollection<Users> collection) throws RemoteException {
		try {
			// Create BSON filters based on the requirements
			Bson nricFilter = Filters.eq("nric", user.getNric());
			Bson passwordFilter = Filters.eq("password", user.getPassword());

			// Find User based on these filters
			Users userfound = collection.find(Filters.and(nricFilter, passwordFilter)).first();

			if (userfound != null) {
				return userfound;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public String notifyUsernotifyUser(Users user, MongoCollection<Transactions> transactionCollection,
			MongoCollection<InfectedLocations> infectedCollection) throws RemoteException {
		try {
			/// Create BSON filters based on the requirements
			Bson nameFilter = Filters.eq("name", user.getName());
			Bson locationFilter = Filters.eq("nric", user.getNric());

			// Find the list of Transactions based on these filters
			MongoCursor<Transactions> transCursor = transactionCollection.find(Filters.and(nameFilter, locationFilter))
					.iterator();

			// Stores list of Transactions objects
			ArrayList<Transactions> transactionsList = new ArrayList<Transactions>();
			try {
				while (transCursor.hasNext()) {
					transactionsList.add(transCursor.next());
				}
			} finally {
				transCursor.close();
			}

			// Find the full list of InfectedLocations
			MongoCursor<InfectedLocations> infectedCursor = infectedCollection.find().iterator();

			// Stores list of InfectedLocations objects
			ArrayList<InfectedLocations> infectedList = new ArrayList<InfectedLocations>();
			try {
				while (infectedCursor.hasNext()) {
					infectedList.add(infectedCursor.next());
				}
			} finally {
				infectedCursor.close();
			}

			String message = "";
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

			// Checks if the Transaction's check in time is within the Infected Location's
			// Check-in and Check-out time
			for (int infectedCounter = 0; infectedCounter < infectedList.size(); infectedCounter++) {

				for (int transCounter = 0; transCounter < transactionsList.size(); transCounter++) {

					if (transactionsList.get(transCounter).getLocation()
							.equals(infectedList.get(infectedCounter).getLocation())) {

						LocalTime locationCheckInTime = LocalDateTime
								.parse(infectedList.get(infectedCounter).getCheckInTime(), fmt).toLocalTime();
						LocalTime locationCheckOutTime = LocalDateTime
								.parse(infectedList.get(infectedCounter).getCheckOutTime(), fmt).toLocalTime();
						LocalTime transactionTime = LocalDateTime
								.parse(transactionsList.get(transCounter).getCheckInTime(), fmt).toLocalTime();

						if (transactionTime.isAfter(locationCheckInTime)
								&& transactionTime.isBefore(locationCheckOutTime)) {
							// Craft the notification message
							message += "\n\nLocation: " + infectedList.get(infectedCounter).getLocation();
							message += "\nCheck-in Time: " + infectedList.get(infectedCounter).getCheckInTime();
							message += "\nCheck-out Time: " + infectedList.get(infectedCounter).getCheckOutTime();
						}
					}
				}
			}
			return message;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
