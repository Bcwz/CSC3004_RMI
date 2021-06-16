

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import classes.InfectedLocations;
import classes.Transactions;

public class SafeEntryOfficerImpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryOfficer {

	private RMIClientIntf clientCallBack;
	private static final Path transactionFilePath = Paths.get("./TraceTogetherTransaction.csv");
	private static final Path infectedLocationsFilePath = Paths.get("./TraceTogetherInfectedLocations.csv");
	private static final DateTimeFormatter DATETIMEFORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	// Explicit constructor to declare the RemoteException exception
	protected SafeEntryOfficerImpl() throws RemoteException {
		super();
	}

	/**
	 * Allows officers to retrieve all infected locations. Reads records in
	 * TraceTogetherInfectedLocations.csv
	 * 
	 * @param client RMIClientIntf object for callback
	 */
	@Override
	public void retrieveAllInfectedLocations(RMIClientIntf client) {

		clientCallBack = client;

		Thread thread = new Thread(new Runnable() {

			public void run() {

				// Create a buffer reader to store the file
				BufferedReader bufReader = null;
				try {
					bufReader = new BufferedReader(new FileReader(infectedLocationsFilePath.toFile()));
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				// Save each line of InfectedLocations for reading and updating
				ArrayList<String> infectedLocationStringList = new ArrayList<>();
				String line = null;
				try {
					line = bufReader.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while (line != null) {
					infectedLocationStringList.add(line);
					try {
						line = bufReader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// Iterate through the list to find all locations
				for (int i = 0; i < infectedLocationStringList.size(); i++) {
					try {

						// Split the transactionStringList with the delimiter ;
						String[] recordFound = infectedLocationStringList.get(i).split("[;]", 0);

						String location = recordFound[0];
						String checkInTime = recordFound[1];
						String checkOutTime = recordFound[2];
						String locationHistory = "\nLocation: " + location + " Check-in time: " + checkInTime
								+ " Check-out time: " + checkOutTime;

						// Returns the list of history through a callback.
						clientCallBack.callBack(locationHistory);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if (infectedLocationStringList.isEmpty()) {
					try {

						// Returns the list of history through a callback.
						clientCallBack.callBack("No record found. Please try again.");

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
	 * Allow officers to add an infected location. Store records in
	 * TraceTogetherInfectedLocations.csv file.
	 * 
	 * @param InfectedLocations location object contain Location, check-in time,
	 *                          check-out time
	 * @param client            RMIClientIntf object for callback
	 */
	@Override
	public void addLocation(RMIClientIntf client, InfectedLocations location) {
		clientCallBack = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				// Create a new String to be inserted into the CSV
				String locationBuilder = location.getLocation() + ";" + location.getCheckOutTime() + ";"
						+ location.getCheckInTime() + ";\n";

				try (BufferedWriter writer = Files.newBufferedWriter(infectedLocationsFilePath,
						StandardOpenOption.APPEND)) {
					// Append the String to the end of the file
					writer.write(locationBuilder);

					// Perform a callback to inform the user of the result.
					clientCallBack.callBack("Location added SUCCESS. Location: " + location.getLocation());
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
	 * Allow officers to notify connected clients of a possible exposure through a
	 * callback of connected clients.
	 * 
	 * @param client RMIClientIntf object for callback
	 */
	@Override
	public void notifyClient(RMIClientIntf client) {

		clientCallBack = client;

		Thread thread = new Thread(new Runnable() {

			public void run() {
				boolean found = false;

				// Create buffer readers to store the files
				BufferedReader transactionFileBuffer = null;
				BufferedReader infectedFileBuffer = null;
				try {
					transactionFileBuffer = new BufferedReader(new FileReader(transactionFilePath.toString()));
					infectedFileBuffer = new BufferedReader(new FileReader(infectedLocationsFilePath.toString()));
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				String transactionLine = null, infectedLocationLine = null;
				try {
					transactionLine = transactionFileBuffer.readLine();
					infectedLocationLine = infectedFileBuffer.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Save each line of Transaction and Infected Location for reading
				ArrayList<Transactions> transactionList = new ArrayList<Transactions>();
				ArrayList<InfectedLocations> infectedLocationList = new ArrayList<InfectedLocations>();

				while (transactionLine != null) {

					// Split the transactionLine with the delimiter ;
					String[] transactionRecord = transactionLine.split("[;]", 0);

					// Creation and addition of transaction object based on the NRIC, name, type,
					// location, checkOutTime, checkInTime
					Transactions transaction = new Transactions(transactionRecord[0], transactionRecord[1],
							transactionRecord[2], transactionRecord[3], transactionRecord[4], transactionRecord[5]);
					transactionList.add(transaction);

					try {
						transactionLine = transactionFileBuffer.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				while (infectedLocationLine != null) {

					// Split the transactionLine with the delimiter ;
					String[] res = infectedLocationLine.split("[;]", 0);

					// Creation and addition of InfectedLocation object based on the location,
					// checkOutTime, checkInTime
					InfectedLocations InfectedLocation = new InfectedLocations(res[0], res[2], res[1]);
					infectedLocationList.add(InfectedLocation);

					try {
						infectedLocationLine = transactionFileBuffer.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// Iterate through both the infectedLocationList and transactionList to find for
				// possible exposures
				for (int infectedCounter = 0; infectedCounter < infectedLocationList.size(); infectedCounter++) {

					for (int transCounter = 0; transCounter < transactionList.size(); transCounter++) {

						// Comparison of transactionList & infectedLocationList locations
						if (transactionList.get(transCounter).getLocation()
								.equals(infectedLocationList.get(infectedCounter).getLocation())) {

							// Creation of transactionList & infectedLocationList LocalTime object
							LocalTime locationCheckInTime = LocalDateTime
									.parse(infectedLocationList.get(infectedCounter).getCheckInTime(), DATETIMEFORMAT)
									.toLocalTime();
							LocalTime locationCheckOutTime = LocalDateTime
									.parse(infectedLocationList.get(infectedCounter).getCheckOutTime(), DATETIMEFORMAT)
									.toLocalTime();
							LocalTime transactionTime = LocalDateTime
									.parse(transactionList.get(transCounter).getCheckInTime(), DATETIMEFORMAT)
									.toLocalTime();

							// Comparison of transactionList & infectedLocationList LocalTime object
							if (transactionTime.isAfter(locationCheckInTime)
									&& transactionTime.isBefore(locationCheckOutTime)) {

								found = true;
								try {

									// Perform a callback to inform the user of the result.
									clientCallBack.callBack("Possible exposure user found: "
											+ transactionList.get(transCounter).getName() + ". Sending notification.");

								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								ArrayList<String> connectedClientList = SafeEntryUserImpl.getConnectedClientList();
//								System.out.println("CLIENT LISTENER ARRAY LIST : " + connectedClientList);

								// Iterated through the ConnectedClientList and invoke the client's notification
								// callback
								for (int i = 0; i < connectedClientList.size(); i++) {
									if (connectedClientList.get(i)
											.equals(transactionList.get(transCounter).getNric())) {
//										System.out.println("Found : " + ListenerArray.get(i));
//										SafeEntryUserimpl.officerCallBack("ALERT!! You might be exposed to Covid-19.");

										RMIClientIntf clientCallBack = SafeEntryUserImpl.connectClients
												.get(connectedClientList.get(i));
										try {

											// Returns the possible exposure notification
											clientCallBack
													.callBack("ALERT!! You have a possible exposure to Covid-19.");
										} catch (RemoteException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
							}
						}
					}
				}

				if (!found) {
					try {

						// Perform a callback to inform the user of the result.
						clientCallBack.callBack("No possible exposure found. No notfication sent.");
						
					} catch (RemoteException e) {
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

}
