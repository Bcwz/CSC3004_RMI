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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import classes.InfectedLocations;

public class SafeEntryOfficerimpl extends java.rmi.server.UnicastRemoteObject implements SafeEntryOfficer {
	private RMIClientIntf c;

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
				Random rg = new Random();
				int timer = rg.nextInt(5000);
				boolean inside = false;

				String p = "C:\\Users\\Bernie\\OneDrive\\Desktop\\cloud\\projectrmi\\SafeEntry\\filename.txt";

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
				for (int i = 0; i < listOfLines.size(); i++) {

					try {
						c.callBack(listOfLines.get(i));
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

				String recordBuilder = "Location: " + location.getLocation() + "; Checkin time: "
						+ location.getCheckInTime() + "; Checkout time: " + location.getCheckOutTime() + ";\n";
				Path p = Paths.get("C:\\Users\\Bernie\\OneDrive\\Desktop\\cloud\\projectrmi\\SafeEntry\\infected.txt");

				try (BufferedWriter writer = Files.newBufferedWriter(p, StandardOpenOption.APPEND)) {
					writer.write(recordBuilder);
					c.callBack("Locateion added SUCCESS. Location: " + location.getLocation());
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

}
