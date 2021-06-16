Step 1: Open SafeEntry Directory.
CLI Command: cd SafeEntry

Step 2: Compile Java JAR files.
CLI Command: javac *.java --release 8

Step 3: Start RMIRegistry
CLI Command: start rmiregistry

Step 4: Run SafeEntryServer class
CLI Command: java SafeEntryServer 

Step 5: Run SafeEntryUserClient class in another CLI terminal
CLI Command: java SafeEntryUserClient

Step 6: Run java SafeEntryOfficerClientclass in another CLI terminal
CLI Command: java SafeEntryOfficerClient