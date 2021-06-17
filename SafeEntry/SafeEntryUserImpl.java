/*
	Code: SafeEntryUserImpl remote object	
	Contains the Users methods that can be remotely invoked by client.
	extends java.rmi.server.UnicastRemoteObject and implements SafeEntryUser (Interface)
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import classes.Transactions;
import classes.Users;

@SuppressWarnings("serial")
public class SafeEntryUserImpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryUser {

	private static RMIClientIntf clientCallBack;
	public static ArrayList<String> connectedClientList = new ArrayList<String>();
	public static HashMap<String, RMIClientIntf> connectClients = new HashMap<String, RMIClientIntf>();
	private static final Path TRANSACTIONFILEPATH = Paths.get("./TraceTogetherTransaction.csv");
	private static final String SELFCHECKINTYPE = "Self check-in";
	private static final String GROUPCHECKINTYPE = "Group check-in";
	private static final DateTimeFormatter DATETIMEFORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	private static final Charset CHARSET = StandardCharsets.UTF_8;

	// Explicit constructor to declare the RemoteException exception
	public SafeEntryUserImpl() throws java.rmi.RemoteException {
		super();
	}

	/**
	 * Returns the list of connected clients
	 * 
	 * @return connectedClientList.
	 */
	public static ArrayList<String> getConnectedClientList() {
		return connectedClientList;
	}

	/**
	 * Allows officers to perform callback to notify clients
	 * 
	 * @param message Message to be displayed
	 */
	public static void clientCallBack(String message) {
		try {
			clientCallBack.callBack(message);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Allow users to check in. Store records in TraceTogetherTransaction.csv file.
	 * 
	 * @param checkInTransaction Transactions object contain NRIC, Name, Location
	 * @param client             RMIClientIntf object for callback
	 */
	@Override
	public void selfCheckIn(RMIClientIntf client, Transactions checkInTransaction) {

		clientCallBack = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				// Set the Transactions object Type, CheckInTime and CheckOutTime to be null.
				checkInTransaction.setType(SELFCHECKINTYPE);
				checkInTransaction.setCheckInTime(LocalDateTime.now().format(DATETIMEFORMAT));
				checkInTransaction.setCheckOutTime(null);

				// Create a new String to be inserted into the CSV
				String transactionBuilder = checkInTransaction.getNric() + ";" + checkInTransaction.getName() + ";"
						+ checkInTransaction.getType() + ";" + checkInTransaction.getLocation() + ";"
						+ checkInTransaction.getCheckOutTime() + ";" + checkInTransaction.getCheckInTime() + ";\n";

				try (BufferedWriter writer = Files.newBufferedWriter(TRANSACTIONFILEPATH, StandardOpenOption.APPEND)) {
					// Append the String to the end of the file
					writer.write(transactionBuilder);

					// Add the connected user's NRIC to the list of connected clients
					connectedClientList.add(checkInTransaction.getNric());
					System.out.println("USER Self Checked in:" + checkInTransaction.getNric() + " at location : " + checkInTransaction.getLocation());
					// Associate the user's NRIC and RMIClientIntf object to the HashMap. Used for
					// notifying (callback) the connected user
					registerClient(checkInTransaction.getNric(), clientCallBack);

					// Perform a callback to inform the user of the result.
					clientCallBack.callBack("Check-in SUCCESS. NRIC: " + checkInTransaction.getNric());

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// Start a new Thread for concurrency and faster processing of tasks
		thread.start();
		return;
	}

	/**
	 * Allow users to check out. Store records in TraceTogetherTransaction.csv file.
	 * 
	 * @param checkOutTransaction Transactions object contain NRIC, Name, Location
	 * @param client              RMIClientIntf object for callback
	 */
	@Override
	public void selfCheckOut(RMIClientIntf client, Transactions checkOutTransaction) throws java.rmi.RemoteException {

		clientCallBack = client;

		Thread thread = new Thread(new Runnable() {

			public void run() {

				// Set the Transactions object Type.
				checkOutTransaction.setType(SELFCHECKINTYPE);

				// Create String check in record based on the transaction requirements
				String checkinRecord = checkOutTransaction.getNric() + ";" + checkOutTransaction.getName() + ";"
						+ checkOutTransaction.getType() + ";" + checkOutTransaction.getLocation() + ";null;";

				// Store the file content as a string
				String transactionContent = "";
				try {
					transactionContent = new String(Files.readAllBytes(TRANSACTIONFILEPATH), CHARSET);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Find the previous record by NRIC, Name, TransactionType, Location and update
				// the record
				if (transactionContent.contains(checkinRecord)) {

					// Set the Transactions object CheckOutTime
					checkOutTransaction.setCheckOutTime(LocalDateTime.now().format(DATETIMEFORMAT));

					// Create a new String to be updated into the CSV
					String transactionBuilder = checkOutTransaction.getNric() + ";" + checkOutTransaction.getName()
							+ ";" + checkOutTransaction.getType() + ";" + checkOutTransaction.getLocation() + ";"
							+ checkOutTransaction.getCheckOutTime() + ";";

					// Replace (update) the transaction to includes the checkout time
					transactionContent = transactionContent.replaceAll(checkinRecord, transactionBuilder);

					try {
						// Update the file
						Files.write(TRANSACTIONFILEPATH, transactionContent.getBytes(CHARSET));

						// Perform a callback to inform the user of the result.
						clientCallBack.callBack("Check-out SUCCESS. NRIC : " + checkOutTransaction.getNric());

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("USER Self Checked out:" + checkOutTransaction.getNric() + " at location : " + checkOutTransaction.getLocation());

				} else {
					try {
						// Perform a callback to inform the user of the result.
						clientCallBack.callBack(
								"Check-out FAILED. NRIC : " + checkOutTransaction.getNric() + ". Please try again.");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		// Start a new Thread for concurrency and faster processing of tasks
		thread.start();
		return;
	}

	/**
	 * Allow users to perform group check in. Store records in
	 * TraceTogetherTransaction.csv file.
	 * 
	 * @param checkInTransactionList list of Transactions object contain NRIC, Name,
	 *                               Location
	 * @param client                 RMIClientIntf object for callback
	 */
	@Override
	public void groupCheckIn(RMIClientIntf client, ArrayList<Transactions> checkInTransactionList)
			throws RemoteException {
		clientCallBack = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (int counter = 0; counter < checkInTransactionList.size(); counter++) {

						// Set the Transactions object Type, CheckInTime and CheckOutTime to be null.
						checkInTransactionList.get(counter).setType(GROUPCHECKINTYPE);
						checkInTransactionList.get(counter).setCheckInTime(LocalDateTime.now().format(DATETIMEFORMAT));
						checkInTransactionList.get(counter).setCheckOutTime(null);

						// Create a new String to be inserted into the CSV
						String recordBuilder = checkInTransactionList.get(counter).getNric() + ";"
								+ checkInTransactionList.get(counter).getName() + ";"
								+ checkInTransactionList.get(counter).getType() + ";"
								+ checkInTransactionList.get(counter).getLocation() + ";"
								+ checkInTransactionList.get(counter).getCheckOutTime() + ";"
								+ checkInTransactionList.get(counter).getCheckInTime() + ";\n";

						try (BufferedWriter writer = Files.newBufferedWriter(TRANSACTIONFILEPATH,
								StandardOpenOption.APPEND)) {

							// Append the String to the end of the file
							writer.write(recordBuilder);

							// Perform a callback to inform the user of the result.
							clientCallBack.callBack("Check-in SUCCESS for family member. NRIC : "
									+ checkInTransactionList.get(counter).getNric());

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					// Perform a callback to inform the user of the result.
					clientCallBack.callBack("Group Check-in for all members SUCCESS.");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return;

	}

	/**
	 * Allow users to perform group check out. Store records in
	 * TraceTogetherTransaction.csv file.
	 * 
	 * @param checkInTransactionList list of Transactions object contain NRIC, Name,
	 *                               Location
	 * @param client                 RMIClientIntf object for callback
	 */
	@Override
	public void groupCheckOut(RMIClientIntf client, ArrayList<Transactions> checkOutTransactionList)
			throws RemoteException {
		clientCallBack = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (int counter = 0; counter < checkOutTransactionList.size(); counter++) {

						// Set the Transactions object Type.
						checkOutTransactionList.get(counter).setType(GROUPCHECKINTYPE);

						// Create String check in record based on the transaction requirements
						String checkinRecord = checkOutTransactionList.get(counter).getNric() + ";"
								+ checkOutTransactionList.get(counter).getName() + ";"
								+ checkOutTransactionList.get(counter).getType() + ";"
								+ checkOutTransactionList.get(counter).getLocation() + ";null;";

						// Store the file content as a string
						String transactionContent = "";
						try {
							transactionContent = new String(Files.readAllBytes(TRANSACTIONFILEPATH), CHARSET);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// Find the previous record by NRIC, Name, TransactionType, Location and update
						// the record
						if (transactionContent.contains(checkinRecord)) {

							// Set the Transactions object CheckOutTime
							checkOutTransactionList.get(counter)
									.setCheckOutTime(LocalDateTime.now().format(DATETIMEFORMAT));

							// Create a new String to be updated into the CSV
							String recordBuilder = checkOutTransactionList.get(counter).getNric() + ";"
									+ checkOutTransactionList.get(counter).getName() + ";"
									+ checkOutTransactionList.get(counter).getType() + ";"
									+ checkOutTransactionList.get(counter).getLocation() + ";"
									+ checkOutTransactionList.get(counter).getCheckOutTime() + ";";

							// Replace (update) the transaction to includes the checkout time
							transactionContent = transactionContent.replaceAll(checkinRecord, recordBuilder);
							try {

								// Update the file
								Files.write(TRANSACTIONFILEPATH, transactionContent.getBytes(CHARSET));

								// Perform a callback to inform the user of the result.
								clientCallBack.callBack(
										"Check-out SUCCESS. NRIC : " + checkOutTransactionList.get(counter).getNric());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							try {

								// Perform a callback to inform the user of the result.
								clientCallBack.callBack("Check-out FAILED. NRIC : "
										+ checkOutTransactionList.get(counter).getNric() + ". Please try again.");

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

					// Perform a callback to inform the user of the result.
					clientCallBack.callBack("Group Check-out for all members SUCCESS.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return;

	}

	/**
	 * Allow users to view history. Reads records in TraceTogetherTransaction.csv
	 * file.
	 * 
	 * @param user   Users object contain NRIC, Name
	 * @param client RMIClientIntf object for callback
	 */
	@Override
	public void viewHistory(RMIClientIntf client, Users user) throws RemoteException {

		clientCallBack = client;

		Thread thread = new Thread(new Runnable() {

			public void run() {
				boolean found = false;

				// Create a buffer reader to store the file
				BufferedReader bufReader = null;
				try {
					bufReader = new BufferedReader(new FileReader(TRANSACTIONFILEPATH.toString()));
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				// Save each line of Transaction for reading and updating
				ArrayList<String> transactionStringList = new ArrayList<>();
				String line = null;
				try {
					line = bufReader.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while (line != null) {
					transactionStringList.add(line);
					try {
						line = bufReader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					bufReader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Iterate through the list to find existing records
				String userRecord = user.getNric() + ";" + user.getName() + ";";
				for (int i = 0; i < transactionStringList.size(); i++) {
					if (transactionStringList.get(i).contains(userRecord)) {
						found = true;

						// Split the transactionStringList with the delimiter ;
						String[] recordFound = transactionStringList.get(i).split("[;]", 0);

						try {

							String location = recordFound[3];
							String checkInTime = recordFound[5];
							String checkOutTime = recordFound[4];
							String userHistory = "\nLocation: " + location + " Check-in time: " + checkInTime
									+ " Check-out time: " + checkOutTime;

							// Returns the list of history through a callback.
							clientCallBack.callBack(userHistory);

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				if (!found) {
					try {

						// Perform a callback to inform the user of the result.
						clientCallBack.callBack("No records found. Please try again.");

					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
		thread.start();
		return;
	}

	/**
	 * Allow clients to registered as connected clients.
	 * 
	 * @param clientNRIC    Client's NRIC client
	 * @param RMIClientIntf object for callback
	 */
	@Override
	public void registerClient(String clientNRIC, RMIClientIntf client) throws RemoteException {
		connectClients.put(clientNRIC, client);
	}

}
