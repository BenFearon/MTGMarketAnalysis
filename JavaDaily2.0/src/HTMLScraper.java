import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class HTMLScraper {
	
	private String inputLine;
	private String bigHTMLstring;
	private String fullHTML;

	public String getHTML(String address) throws ParserConfigurationException, SAXException{
		
		try {
			fullHTML = "";
			bigHTMLstring = "";
			inputLine = "";
			URL newURL = new URL(address);
			URLConnection newURLConnection = newURL.openConnection();
			newURLConnection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(newURLConnection.getInputStream()));
			while((inputLine = in.readLine()) != null)
				bigHTMLstring = bigHTMLstring + inputLine;
			in.close();
			fullHTML =  bigHTMLstring;
		}  
		catch (MalformedURLException e) { 
			// new URL() failed
			// ...
		} 
		catch (IOException e) {   
			// openConnection() failed
			// ...
		}
		return fullHTML;
	}
}
