/*
	Code: calculator server		CalculatorServer.java

	Server code for hosting the CalculatorImpl object
*/


import java.rmi.Naming;	//Import naming classes to bind to rmiregistry

import classes.FamilyMembers;

public class SafeEntryserver {
	static int port = 1099;
   
   public SafeEntryserver() {

		 try {
		 		//Construct a new CalculatorImpl object and bind it to the local rmiregistry
     		//N.b. it is possible to host multiple objects on a server
				FamilyMembers m = new FamilyMembers();
				m.setName("Gideon");
				System.out.println("name: " +m.getName() );
				SafeEntryUser SE = new SafeEntryUserimpl();
				Naming.rebind("rmi://localhost/SafeEntryService", SE);
     }
     catch (Exception e) {
       System.out.println("Server Error: " + e);
     }
   }

   public static void main(String args[]) {
     	//Create the new Calculator server
			if (args.length == 1)
				port = Integer.parseInt(args[0]);
				new SafeEntryserver();
   		}
}
