
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming; //Import naming classes to bind to rmiregistry
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import classes.InfectedLocations;
import classes.Transactions;

public class SafeEntryServer {
	private ArrayList<String> clientListener = new ArrayList<String>();
	private RMIClientIntf c;
	static int port = 1099;

	// SafeEntryServer constructor
	public SafeEntryServer() {

		// Construct a new CalculatorImpl object and bind it to the local rmiregistry
		// N.b. it is possible to host multiple objects on a server by repeating the
		// following method.

		try {
			// For SafeEntry Normal users

			SafeEntryUser SEU = new SafeEntryUserimpl();
			Naming.rebind("rmi://localhost:" + port + "/SafeEntryService", SEU);

			// For SafeEntryOfficer
			SafeEntryOfficer SEO = new SafeEntryOfficerimpl();
			Naming.rebind("rmi://localhost:" + port + "/OfficerService", SEO);

			System.out.println("Server started!");

		} catch (Exception e) {
			System.out.println("Server Error: " + e);
		}
	}

	// Use to notify listener
	private void addListener(String listenerNRIC) throws RemoteException {
		clientListener.add(listenerNRIC);
	}

	private void removeListener(String listenerNRIC) throws RemoteException {
		clientListener.remove(listenerNRIC);
	}

	private void notifyListener(RMIClientIntf client, String NRIC) throws RemoteException {
		// Get the list of infected location, checkin&checkout

		// read the infected location first

		for (String element : clientListener) {
			// Compare each element in arrayList which contains the listener NRIC
			// For each element in filename.txt,
			// If (filename.txt location == infectedlocation && checkin time >
			// infectedcheckin && checkout <infectedcheckout)
			// callback to clientListener to notify
		}

		// TODO Auto-generated method stub
		c = client;

		Thread thread = new Thread(new Runnable() {

			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				boolean inside = false;

				String transactionFilePath = "C:\\Users\\Bernie\\OneDrive\\Desktop\\cloud\\projectrmi\\SafeEntry\\filename.txt";
				String infectedFilePath = "C:\\Users\\Bernie\\OneDrive\\Desktop\\cloud\\projectrmi\\SafeEntry\\infected.txt";
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

				BufferedReader transactionFileBuffer = null;
				BufferedReader infectedFileBuffer = null;
				try {
					transactionFileBuffer = new BufferedReader(new FileReader(transactionFilePath));
					infectedFileBuffer = new BufferedReader(new FileReader(infectedFilePath));
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
//				ArrayList<String> transactionRecords = new ArrayList<>();
//				ArrayList<String> infectedLocationRecords = new ArrayList<>();
				String transactionLine = null, infectedLocationLine = null;
				try {
					transactionLine = transactionFileBuffer.readLine();
					infectedLocationLine = infectedFileBuffer.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				ArrayList<Transactions> transactionList = new ArrayList<Transactions>();
				ArrayList<InfectedLocations> infectedLocationList = new ArrayList<InfectedLocations>();

				while (transactionLine != null) {
//					transactionRecords.add(transactionLine);					
					String[] res = transactionLine.split("[;]", 0);
					Transactions transaction = new Transactions(res[0], res[1], res[2], res[3], res[4], res[5]);
					transactionList.add(transaction);

					try {
						transactionLine = transactionFileBuffer.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				while (infectedLocationLine != null) {

//					transactionRecords.add(transactionLine);					
					String[] res = infectedLocationLine.split("[;]", 0);
					InfectedLocations InfectedLocation = new InfectedLocations(res[0], res[1], res[2]);
					infectedLocationList.add(InfectedLocation);

					try {
						infectedLocationLine = transactionFileBuffer.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (int transCounter = 0; transCounter < infectedLocationList.size(); transCounter++) {

					for (int infectedCounter = 0; infectedCounter < transactionList.size(); infectedCounter++) {

						// check the location first based on the name

						if (transactionList.get(transCounter).getLocation()
								.equals(infectedLocationList.get(infectedCounter).getLocation())) {

							LocalTime locationCheckInTime = LocalDateTime
									.parse(infectedLocationList.get(infectedCounter).getCheckInTime(), fmt)
									.toLocalTime();
							LocalTime locationCheckOutTime = LocalDateTime
									.parse(infectedLocationList.get(infectedCounter).getCheckOutTime(), fmt)
									.toLocalTime();
							LocalTime transactionTime = LocalDateTime
									.parse(transactionList.get(transCounter).getCheckInTime(), fmt).toLocalTime();

							if (transactionTime.isAfter(locationCheckInTime)
									&& transactionTime.isBefore(locationCheckOutTime)) {
								// Craft the notification message for every user
								
								System.out.println("Found!");

							}

						}

					}

				}
			}
		});
		thread.start();
		return;
	}

	public static void main(String args[]) {
		// Create the new Calculator server
		if (args.length == 1)
			port = Integer.parseInt(args[0]);

		new SafeEntryServer();
	}
}
