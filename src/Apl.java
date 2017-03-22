public class Apl {
	public static void main( String[] args ) {
		Crawler crawler = new Crawler();
		try
		{
			crawler.start("http://www.example.com");
		}
		catch( Exception e )
		{
			System.out.println( "URL not found" );
		}
	}
}