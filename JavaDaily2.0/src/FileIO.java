import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileIO {

	public void writeCards(Card[] cardArray) throws IOException{
		for (int i = 0; i < cardArray.length; i++) {
			
			FileWriter fstream = new FileWriter("cardStorage.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			Card card = cardArray[i];
			
			if(i!=0){
				out.write("="+card.Name()
						+"_"+card.Price()
						+"_"+card.Rarity()
						+"_"+card.Count()
						+"_"+card.Reprints()
						+"_"+card.Set()
						+"_"+card.Stock());
				out.close();
			}
			else{
				out.write(card.Name()
						+"_"+card.Price()
						+"_"+card.Rarity()
						+"_"+card.Count()
						+"_"+card.Reprints()
						+"_"+card.Set()
						+"_"+card.Stock());
				out.close();
			}
		}
	}
	public Card[] readCards() throws IOException{

		FileReader reader = new FileReader("cardStorage.txt");
		BufferedReader textReader = new BufferedReader(reader);
		String filecontents = textReader.readLine();
		String[] stringCards = filecontents.split("=");
		Card[] cardArray = new Card[stringCards.length];
		for (int i = 0; i < stringCards.length; i++) {

			String[] cutItems = stringCards[i].split("_");
			Card current = new Card(cutItems[0], Double.valueOf(cutItems[3]));
			current.setPrice(Double.valueOf(cutItems[1]));
			current.setRarity(cutItems[2]);
			current.setReprints(Double.valueOf(cutItems[4]));
			current.setSet(cutItems[5]);
			current.setStock(Boolean.getBoolean(cutItems[6]));
			cardArray[i] = current;
			System.out.println(current.Name());
			System.out.println(current.Rarity());
			System.out.println(current.Set());
			System.out.println(current.Count());
			System.out.println(current.Price());
			System.out.println();
		}
		return cardArray;
	}

}

