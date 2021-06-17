(CLI Command used for Windows OS, not sure about MacOS)

Step 1: Open SafeEntry Directory.
CLI Command: cd SafeEntry

Step 2: Compile all Java files and classes.
CLI Command: javac classes/*.java *.java --release 8

Step 3: Start RMIRegistry.
CLI Command: start rmiregistry

Step 4: Run SafeEntryServer class in first CLI terminal.
CLI Command: java SafeEntryServer 

Step 5: Run SafeEntryUserClient class in another (second terminal) CLI terminal.
CLI Command: java SafeEntryUserClient

Step 6: Run java SafeEntryOfficerClient class in another (third terminal) CLI terminal.
CLI Command: java SafeEntryOfficerClient