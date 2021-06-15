/*
	Code: CalculatorImpl remote object	calculatorimpl.java
	Date: 10th October 2000

	Contains the arithmetic methods that can be remotley invoked
*/

// The implementation Class must implement the rmi interface (calculator)
// and be set as a Remote object on a server
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SafeEntryUserimpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryUser{

	private RMIClientIntf c;

	// Implementations must have an explicit constructor
	// in order to declare the RemoteException exception

	public SafeEntryUserimpl() throws java.rmi.RemoteException {
		super();
	}

	
	public void selfCheckIn(RMIClientIntf client, String NRIC, String Name, String location) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		c = client;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String formatDateTime = now.format(format);

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				String s = "NAME:aaa";
				Path p = Paths.get("C:\\Users\\Bernie\\OneDrive\\Desktop\\cloud\\projectrmi\\SafeEntry\\filename.txt");
				try (
						BufferedWriter writer = Files.newBufferedWriter(p, StandardOpenOption.APPEND)) {
					writer.write(s);
			
					c.callBack("Check-in SUCCESS. NRIC : " + NRIC);

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
	
	
	// Subtract the second parameter from the first and return the result
	public void selfCheckOut(RMIClientIntf client, String NRIC) throws java.rmi.RemoteException {

		c = client;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				try {
					// Thread.sleep(timer);

					c.callBack("Check-out SUCCESS. NRIC = " + NRIC);
				} catch (java.rmi.RemoteException e) {
					e.printStackTrace();
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
