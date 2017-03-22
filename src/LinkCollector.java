/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkCollector {
	private ArrayList<String>	validLinks	= new ArrayList<String>();

	public LinkCollector( String data ) {
		try {
			Pattern p = Pattern.compile( "href=\"[^\"]*\"" );
			Matcher m = p.matcher( data );

			String link = null;

			while( m.find() ) {
				link = m.group( 0 );
				link = link.replace( "href=\"", "" );
				link = link.replace( "\"", "" );
				
				validLinks.add( link );
			}

		} catch( Exception e ) {
			System.out.println( "Error: " + e );
		}
	}

	public ArrayList<String> getLinks() {
		return validLinks;
	}

}