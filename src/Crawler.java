import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Crawler {
	public ArrayList<String>	list	= new ArrayList<String>();
	public String				strSC;

	public void start( String url ) {
		strSC = ripUrl( url );
		getPage( "http://" + strSC );
	}

	public void getPage( String pageUrl ) {
		String source = getSourceCode( pageUrl );
		if( source != null ) {
			String page = urlToPath( pageUrl );
			LinkCollector collector = new LinkCollector( source );

			if( isFileNameInUrl( page ) ) {
				makeFolder( removeFileNameUrl( page ));
			}

			makeFile( page, source );

			System.out.println("page: " + page);
			
			list.add( pageUrl );

			for( String link : collector.getLinks() ) {
				if( !link.isEmpty() ) {
					String validUrl = makeValid( link, pageUrl );
					String url = removeUplevel( validUrl );
					if( !list.contains( url ) && isValidURL( link ) ) {
						getPage( url );
					}
				}
			}
		}
	}

	public String removeFileNameUrl( String aFileName ) {
		return aFileName.replaceAll( "\\\\([^\\\\]*).(css|js|htm|html)$", "" );
	}

	public boolean isFileNameInUrl( String aUrl ) {
		return aUrl.matches( "(.*?)\\.(css|js|htm|html)$" );
	}

	public String urlToPath( String aUrl ) {
		aUrl = aUrl.replace( "/", "\\" );
		aUrl = aUrl.replace( "www.", "" );
		aUrl = aUrl.replace( "http:\\\\", "C:\\" );
		return aUrl;
	}

	public void makeFile( String aFileName, String aSource ) {
		if( aFileName.matches( "(.*?)\\.(css|js|htm|html)$" ) ) {
			FileWrite.writetoFile( aFileName, aSource );
		} else {
			FileWrite.writetoFile( aFileName + "\\index.htm", aSource );
		}

	}

	public void makeFolder( String path ) {
		File f = new File( path );
		try {
			if( !f.mkdirs() )
				System.out.println( "Directory is not created" );
		} catch( Exception e ) {
			System.out.println( "Error: " + e );
		}
	}

	public boolean isValidURL( String str ) {
		return ( str.matches( "(.*)" + ripUrl( strSC ) + "(.*)" ) || !str.matches( "^(http://)(.*)" ) ) && !str.matches( "^(#)(.*)" ) && !str.matches( "^(mailto:)(.*)" ) && !str.matches( "^(javascript:)(.*)" );
	}

	public String makeValid( String link, String url ) {
		if( !link.matches( "^(http://)(.*)" ) ) {
			if( !link.matches( "^(/)(.*)" ) ) {
				if( !url.matches( "(.*)(/)$" ) ) {
					link = url + "/" + link;
				} else {
					link = url + link;
				}
			} else {
				link = url + link;
			}
		}
		return link;
	}

	public String removeUplevel( String string ) {
		while( string.matches( ".*/[^/]*/(\\.\\./).*" ) ) {
			string = string.replaceAll( "/[^/]*/(\\.\\./)", "/" );
		}
		return string;
	}

	public String ripUrl( String url ) {
		return url.replaceAll( "^(http://www\\.|http://|www\\.)", "" );
	}

	public String getSourceCode( String pageURL ) {
		try {
			URL url = new URL( pageURL );
			URLConnection urlc = url.openConnection();

			BufferedInputStream buffer = new BufferedInputStream( urlc.getInputStream() );

			StringBuilder builder = new StringBuilder();
			int byteRead;
			while( ( byteRead = buffer.read() ) != -1 )
				builder.append( ( char ) byteRead );

			buffer.close();

			return builder.toString();
		} catch( MalformedURLException ex ) {
			ex.printStackTrace();
		} catch( IOException ex ) {
			ex.printStackTrace();
		}
		return "null";
	}
}
