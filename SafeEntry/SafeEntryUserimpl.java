/*
	Code: CalculatorImpl remote object	calculatorimpl.java
	Date: 10th October 2000

	Contains the arithmetic methods that can be remotley invoked
*/

// The implementation Class must implement the rmi interface (calculator)
// and be set as a Remote object on a server
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import java.util.Random;
import java.util.Scanner;

import classes.Transactions;
import classes.Users;

public class SafeEntryUserimpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryUser {

	private RMIClientIntf c;
	public ArrayList<String> checkinListener = new ArrayList();

	// Implementations must have an explicit constructor
	// in order to declare the RemoteException exception

	public SafeEntryUserimpl() throws java.rmi.RemoteException {
		super();
	}

	public void selfCheckIn(RMIClientIntf client, Transactions checkInTransaction)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		c = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);

				checkInTransaction.setType("Self check-in");
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String formatDateTime = now.format(format);
				checkInTransaction.setCheckInTime(formatDateTime);
				checkInTransaction.setCheckOutTime(null);

				String recordBuilder = checkInTransaction.getNric() + ";"
						+ checkInTransaction.getName() + ";" + checkInTransaction.getType()
						+ ";" + checkInTransaction.getLocation() + ";"
						+ checkInTransaction.getCheckOutTime() + ";"
						+ checkInTransaction.getCheckInTime() + ";\n";
				
				Path p = Paths.get("./filename.txt");

				try (BufferedWriter writer = Files.newBufferedWriter(p, StandardOpenOption.APPEND)) {
					writer.write(recordBuilder);

					c.callBack("Check-in SUCCESS. NRIC : " + checkInTransaction.getNric());
					//Add NRIC to ArrayList, ArrayList shown on Server terminal
					checkinListener.add(checkInTransaction.getNric());
					System.out.println("USER CHECKED IN: " + checkinListener);

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

	public void selfCheckOut(RMIClientIntf client, Transactions checkOutTransaction) throws java.rmi.RemoteException {

		c = client;

		Thread thread = new Thread(new Runnable() {

			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);

				checkOutTransaction.setType("Self check-in");
				Path p = Paths.get("./filename.txt");
				Charset charset = StandardCharsets.UTF_8;

			
				
				
				String checkinRecord = checkOutTransaction.getNric() + ";"
						+ checkOutTransaction.getName() + ";" + checkOutTransaction.getType()
						+ ";" + checkOutTransaction.getLocation() + ";null;";

				String content = "";
				try {
					content = new String(Files.readAllBytes(p), charset);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				if (content.contains(checkinRecord)) {
					LocalDateTime now = LocalDateTime.now();
					DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
					String formatDateTime = now.format(format);
					checkOutTransaction.setCheckOutTime(formatDateTime);

					
					
					String recordBuilder = checkOutTransaction.getNric() + ";"
							+ checkOutTransaction.getName() + ";" + checkOutTransaction.getType()
							+ ";" + checkOutTransaction.getLocation() + ";"
							+ checkOutTransaction.getCheckOutTime() + ";";
					
					content = content.replaceAll(checkinRecord, recordBuilder);
					try {
						Files.write(p, content.getBytes(charset));
						//Add NRIC to ArrayList, ArrayList shown on Server terminal
						checkinListener.remove(checkOutTransaction.getNric());
						System.out.println("USER CHECK OUT: " + checkinListener);

						c.callBack("Check-out SUCCESS. NRIC : " + checkOutTransaction.getNric());
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

				} else {
					try {
						c.callBack("Check-out FAILED. NRIC : " + checkOutTransaction.getNric() + ". Please try again.");
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
	public void groupCheckIn(RMIClientIntf client, ArrayList<Transactions> checkInTransactionList)
			throws RemoteException {
		c = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				try {
					for (int counter = 0; counter < checkInTransactionList.size(); counter++) {

						// Set the Transactions Object Type and CheckInTime
						checkInTransactionList.get(counter).setType("Group check-in");
						LocalDateTime now = LocalDateTime.now();
						DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
						String formatDateTime = now.format(format);
						checkInTransactionList.get(counter).setCheckInTime(formatDateTime);
						checkInTransactionList.get(counter).setCheckOutTime(null);

	
						
						String recordBuilder = checkInTransactionList.get(counter).getNric() + ";"
								+ checkInTransactionList.get(counter).getName() + ";" + checkInTransactionList.get(counter).getType()
								+ ";" + checkInTransactionList.get(counter).getLocation() + ";"
								+ checkInTransactionList.get(counter).getCheckOutTime() + ";"
								+ checkInTransactionList.get(counter).getCheckInTime() + ";\n";
						Path p = Paths.get(
								"./filename.txt");

						try (BufferedWriter writer = Files.newBufferedWriter(p, StandardOpenOption.APPEND)) {
							writer.write(recordBuilder);

							c.callBack("Check-in SUCCESS for family member. NRIC : "
									+ checkInTransactionList.get(counter).getNric());

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					c.callBack("Group Check-in for all members SUCCESS.");
				} catch (java.rmi.RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return;

	}

	@Override
	public void groupCheckOut(RMIClientIntf client, ArrayList<Transactions> checkOutTransaction)
			throws RemoteException {
		c = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				try {
					for (int counter = 0; counter < checkOutTransaction.size(); counter++) {

						// Set the Transactions Object Type and CheckInTime
						checkOutTransaction.get(counter).setType("Group check-in");

						Path p = Paths.get(
								"./filename.txt");
						Charset charset = StandardCharsets.UTF_8;

						

						
						String checkinRecord = checkOutTransaction.get(counter).getNric() + ";"
								+ checkOutTransaction.get(counter).getName() + ";" + checkOutTransaction.get(counter).getType()
								+ ";" + checkOutTransaction.get(counter).getLocation() + ";null;";

						String content = "";
						try {
							content = new String(Files.readAllBytes(p), charset);
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						if (content.contains(checkinRecord)) {
							LocalDateTime now = LocalDateTime.now();
							DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
							String formatDateTime = now.format(format);
							checkOutTransaction.get(counter).setCheckOutTime(formatDateTime);

					
							
							
							
							String recordBuilder = checkOutTransaction.get(counter).getNric() + ";"
									+ checkOutTransaction.get(counter).getName() + ";" + checkOutTransaction.get(counter).getType()
									+ ";" + checkOutTransaction.get(counter).getLocation() + ";"
									+ checkOutTransaction.get(counter).getCheckOutTime() + ";";
							content = content.replaceAll(checkinRecord, recordBuilder);
							try {
								Files.write(p, content.getBytes(charset));
								c.callBack("Check-out SUCCESS. NRIC : " + checkOutTransaction.get(counter).getNric());
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

						} else {
							try {
								c.callBack("Check-out FAILED. NRIC : " + checkOutTransaction.get(counter).getNric()
										+ ". Please try again.");
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

					c.callBack("Group Check-out SUCCESS. NRIC = ");
				} catch (java.rmi.RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return;

	}

	@Override
	public String viewHistory(RMIClientIntf client, Users userToFind) throws RemoteException {
		// TODO Auto-generated method stub
		c = client;

		Thread thread = new Thread(new Runnable() {

			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				boolean inside = false;

				String p = "./filename.txt";

				BufferedReader bufReader = null;
				try {
					bufReader = new BufferedReader(new FileReader(p));
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
				try {
					bufReader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String recordsToFind = userToFind.getNric() + ";" + userToFind.getName() + ";";
				for (int i = 0; i < listOfLines.size(); i++) {
					if (listOfLines.get(i).contains(recordsToFind)) {
						inside = true;
						try {
							c.callBack(listOfLines.get(i));
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				if (!inside) {
					try {
						c.callBack("No records found. Please try again.");
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
		thread.start();
		return null;
	}

}
