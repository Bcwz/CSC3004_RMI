/*
	Code: CalculatorImpl remote object	calculatorimpl.java
	Date: 10th October 2000

	Contains the arithmetic methods that can be remotley invoked
*/

// The implementation Class must implement the rmi interface (calculator)
// and be set as a Remote object on a server
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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
import java.util.Random;

import classes.Transactions;

public class SafeEntryUserimpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryUser {

	private RMIClientIntf c;

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

				String recordBuilder = "NRIC: " + checkInTransaction.getNric() + "; Name: "
						+ checkInTransaction.getName() + "; Check-in Type: " + checkInTransaction.getType()
						+ "; Location: " + checkInTransaction.getLocation() + "; Check-out time: "
						+ checkInTransaction.getCheckOutTime() + "; Check-in time: "
						+ checkInTransaction.getCheckInTime() + "\n";
				Path p = Paths.get("C:\\Users\\Bernie\\OneDrive\\Desktop\\cloud\\projectrmi\\SafeEntry\\filename.txt");

				try (BufferedWriter writer = Files.newBufferedWriter(p, StandardOpenOption.APPEND)) {
					writer.write(recordBuilder);

					c.callBack("Check-in SUCCESS. NRIC : " + checkInTransaction.getNric());

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
				Path p = Paths.get("C:\\Users\\Bernie\\OneDrive\\Desktop\\cloud\\projectrmi\\SafeEntry\\filename.txt");
				Charset charset = StandardCharsets.UTF_8;

				String checkinRecord = "NRIC: " + checkOutTransaction.getNric() + "; Name: "
						+ checkOutTransaction.getName() + "; Check-in Type: " + checkOutTransaction.getType()
						+ "; Location: " + checkOutTransaction.getLocation() + "; Check-out time: null";

				String content ="";
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

					String recordBuilder = "NRIC: " + checkOutTransaction.getNric() + "; Name: "
							+ checkOutTransaction.getName() + "; Check-in Type: " + checkOutTransaction.getType()
							+ "; Location: " + checkOutTransaction.getLocation() + "; Check-out time: "
							+ checkOutTransaction.getCheckOutTime();
					content = content.replaceAll(checkinRecord, recordBuilder);
					try {
						Files.write(p, content.getBytes(charset));
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
	public void groupCheckIn(RMIClientIntf client, String NRIC) throws RemoteException {
		c = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				try {
					// Thread.sleep(timer);

					c.callBack("Group Check-in SUCCESS. NRIC = " + NRIC);
				} catch (java.rmi.RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return;

	}

	@Override
	public void groupCheckOut(RMIClientIntf client, String NRIC) throws RemoteException {
		c = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				try {
					// Thread.sleep(timer);

					c.callBack("Group Check-out SUCCESS. NRIC = " + NRIC);
				} catch (java.rmi.RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return;

	}

}
