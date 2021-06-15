import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface SafeEntryOfficer extends java.rmi.Remote{

	
	
	public void addLocation(String location) throws java.rmi.RemoteException, UnsupportedEncodingException, FileNotFoundException, IOException;
}
