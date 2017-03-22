import java.io.*;

public class FileWrite {
	public static void writetoFile( String filename, String filecontent ) {
		try {
			// Create file
			FileWriter fstream = new FileWriter( filename );
			BufferedWriter out = new BufferedWriter( fstream );
			out.write( filecontent );
			// Close the output stream
			out.close();
		} catch( Exception e ) {// Catch exception if any
			System.err.println( "Error: " + e.getMessage() );
		}
	}
}