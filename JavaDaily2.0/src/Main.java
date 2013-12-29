import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class Main {

	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException{

		DataMain dm = new DataMain();
		CardRegression cr = new CardRegression(9);
		FileIO fio = new FileIO();
		Card[] cardArray = dm.findCards("http://www.wizards.com/Magic/Digital/MagicOnlineTourn.aspx?x=mtg/digital/magiconline/tourn/5294186");
		fio.writeCards(cardArray);
		Card[] cards = fio.readCards();
		cr.buildMatrix(cards);
		cr.Regress();
	}
}
