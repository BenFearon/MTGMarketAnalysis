import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class DataMain {

	Map<String, Integer> countMap = new HashMap<String, Integer>();

	public Card[] findCards(String url) throws ParserConfigurationException, SAXException, IOException{

		HTMLScraper scraper = new HTMLScraper();
		String html = scraper.getHTML(url);
		Pattern p1 = Pattern.compile(".{5}(<a class=\"nodec\")(.{1,200})(</a>)");
		Pattern p2 = Pattern.compile("(javascript:void())(.{1,40})(</a>)");
		Matcher m = p1.matcher(html);

		while(m.find()){
			Matcher m2 = p2.matcher(m.group());
			m2.find();
			String cardName = m2.group().substring(19, m2.group().length()-4);
			Integer cardCount = Integer.valueOf(m.group().substring(0,1));

			Integer oldCount = countMap.get(cardName);
			if(oldCount == null){
				oldCount = 0;
			}
			countMap.put(cardName, cardCount+oldCount);
		}
		int counter = 0;
		Card[] cardArray = new Card[countMap.size()-5];
		for(String cardname: countMap.keySet()){
			if(!(cardname.equals("Swamp") || cardname.equals("Mountain") || cardname.equals("Forest") || cardname.equals("Plains") || cardname.equals("Island"))){
				cardArray[counter] = new Card(cardname, countMap.get(cardname));
				System.out.println(cardname);
				cardArray[counter].getCardData();
				if(cardArray[counter].Stock() == false || cardArray[counter].Price() == 0.0 || cardArray[counter].Price()>1000){
					cardArray[counter].backupPriceScrape();
				}
				System.out.println("Count: "+countMap.get(cardname));
				System.out.println();
				counter++;
			}
		}
		return cardArray;
	}
}
