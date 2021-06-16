import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import classes.InfectedLocations;
import classes.Transactions;

public class SafeEntryOfficerimpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryOfficer {
	private RMIClientIntf c;
	private static final Path transactionFilePath =  Paths.get("./TraceTogetherTransaction.csv");
	private static final Path infectedLocationsFilePath =  Paths.get("./TraceTogetherInfectedLocations.csv");

	protected SafeEntryOfficerimpl() throws RemoteException {
		super();
	}

	@Override
	public void retrieveAllInfectedLocations(RMIClientIntf client)
			throws RemoteException, UnsupportedEncodingException, FileNotFoundException, IOException {

		// TODO Auto-generated method stub
		c = client;

		Thread thread = new Thread(new Runnable() {

			public void run() {


				BufferedReader bufReader = null;
				try {
					bufReader = new BufferedReader(new FileReader(infectedLocationsFilePath.toFile()));
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				ArrayList<String> listOfLines = new ArrayList<>();
				String line = null;
				try {
					line = bufReader.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				while (line != null) {
					listOfLines.add(line);
					try {
						line = bufReader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (int i = 0; i < listOfLines.size(); i++) {

					try {
						
						String[] res=listOfLines.get(i).split("[;]", 0);
						c.callBack("Location: "+res[0]+" Check-in time: "+res[2]+" Check-out time: "+res[1]);
						
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				if(listOfLines.isEmpty()) {
					try {
						c.callBack("No record found. Please try again.");
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

	@Override
	public void addLocation(RMIClientIntf client, InfectedLocations location)
			throws RemoteException, UnsupportedEncodingException, FileNotFoundException, IOException {
		c = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);

				String recordBuilder = location.getLocation() + ";" + location.getCheckOutTime() + ";"
						+ location.getCheckInTime() + ";\n";
//				Path p = Paths.get("./infected.txt");

				try (BufferedWriter writer = Files.newBufferedWriter(infectedLocationsFilePath, StandardOpenOption.APPEND)) {
					writer.write(recordBuilder);
					c.callBack("Location added SUCCESS. Location: " + location.getLocation());
				} catch (java.rmi.RemoteException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		thread.start();
		return;

	}

	@Override
	public void notifyClient(RMIClientIntf client)
			throws RemoteException, UnsupportedEncodingException, FileNotFoundException, IOException {
		// Get the list of infected location, checkin&checkout

		// read the infected location first

		// TODO Auto-generated method stub
		c = client;

		
		Thread thread = new Thread(new Runnable() {

			public void run() {
				boolean inside=false;
//				String transactionFilePath = "./filename.txt";
//				String infectedFilePath = "./infected.txt";
				DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

				BufferedReader transactionFileBuffer = null;
				BufferedReader infectedFileBuffer = null;
				try {
					transactionFileBuffer = new BufferedReader(new FileReader(transactionFilePath.toString()));
					infectedFileBuffer = new BufferedReader(new FileReader(infectedLocationsFilePath.toString()));
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
//						ArrayList<String> transactionRecords = new ArrayList<>();
//						ArrayList<String> infectedLocationRecords = new ArrayList<>();
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
//							transactionRecords.add(transactionLine);					
					String[] res = transactionLine.split("[;]", 0);
					Transactions transaction = new Transactions(res[0], res[1], res[2], res[3], res[4], res[5]);
//							System.out.println(res[0]+ res[1]+ res[2]+ res[3]+res[4]+res[5]);
					transactionList.add(transaction);

					try {
						transactionLine = transactionFileBuffer.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				while (infectedLocationLine != null) {

//							transactionRecords.add(transactionLine);					
					String[] res = infectedLocationLine.split("[;]", 0);
					InfectedLocations InfectedLocation = new InfectedLocations(res[0], res[2], res[1]);
//							System.out.println(res[0]+ res[1]+ res[2]);
					infectedLocationList.add(InfectedLocation);

					try {
						infectedLocationLine = transactionFileBuffer.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (int infectedCounter = 0; infectedCounter < infectedLocationList.size(); infectedCounter++) {

					for (int transCounter = 0; transCounter < transactionList.size(); transCounter++) {

						// check the location first based on the name

						if (transactionList.get(transCounter).getLocation()
								.equals(infectedLocationList.get(infectedCounter).getLocation())) {

//									System.out.println("Location 1: " + transactionList.get(transCounter).getLocation());
//									System.out.println("Location 2: " + infectedLocationList.get(infectedCounter).getLocation());

							LocalTime locationCheckInTime = LocalDateTime
									.parse(infectedLocationList.get(infectedCounter).getCheckInTime(), fmt)
									.toLocalTime();
							LocalTime locationCheckOutTime = LocalDateTime
									.parse(infectedLocationList.get(infectedCounter).getCheckOutTime(), fmt)
									.toLocalTime();
							LocalTime transactionTime = LocalDateTime
									.parse(transactionList.get(transCounter).getCheckInTime(), fmt).toLocalTime();

//									System.out.println("locationCheckInTime: " + locationCheckInTime);
//									System.out.println("locationCheckOutTime: " + locationCheckOutTime);
//									System.out.println("transactionTime: " + transactionTime);

							if (transactionTime.isAfter(locationCheckInTime)
									&& transactionTime.isBefore(locationCheckOutTime)) {
								// Craft the notification message for every user
								inside=true;
								System.out.println("User found: " + transactionList.get(transCounter).getName());
								
								try {
									c.callBack("Location found!!!! FOUND PERSON: "
											+ transactionList.get(transCounter).getName());
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								ArrayList<String> ListenerArray = SafeEntryUserimpl.getCheckinArray();
								System.out.println("CLIENT LISTENER ARRAY LIST : " + ListenerArray);

								for (int i = 0; i < ListenerArray.size(); i++) {
									if (ListenerArray.get(i).equals(transactionList.get(transCounter).getNric())) {
//										System.out.println("Found : " + ListenerArray.get(i));
//										SafeEntryUserimpl.officerCallBack("ALERT!! You might be exposed to Covid-19.");
										RMIClientIntf intsdadsa=SafeEntryUserimpl.clientMap.get(ListenerArray.get(i));
										try {
											intsdadsa.callBack("ALERT!! You might be exposed to Covid-19.");
										} catch (RemoteException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										//access the var
										
									}

								}

							} 

						}

					}

				}
				
		if(!inside) {

			try {
				c.callBack("All users are healthy. No notfication required.");
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

}
