import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class Card {

	private String name;
	private String rarity;
	private String set;
	private double price;
	private double count;
	private double reprints;
	private boolean stock = true;


	public Card(String name, double count){
		this.name = name;
		this.count = count;
	}
//getters and setters
	public String Name(){
		return name;
	}
	public String Rarity(){
		return rarity;
	}
	public double Price(){
		return price;
	}
	public double Count(){
		return count;
	}
	public double Reprints(){
		return reprints;
	}
	public String Set(){
		return set;
	}
	public boolean Stock(){
		return stock;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setRarity(String rarity){
		this.rarity = rarity;
	}
	public void setPrice(Double price){
		this.price = price;
	}
	public void setCount(Double count){
		this.count = count;
	}
	public void setReprints(Double reprints){
		this.reprints = reprints;
	}
	public void setSet(String set){
		this.set = set;
	}
	public void setStock(boolean stock){
		this.stock = stock;
	}
	public void getCardData() throws ParserConfigurationException, SAXException{
//create patterns
		HTMLScraper scraper = new HTMLScraper();
		String urlName = name.replace(" ", "+");
		String mainPage = scraper.getHTML("http://store.channelfireball.com/products/search?query="+urlName);

		Pattern p1 = Pattern.compile("(<article id=\"product_)(\\d+)");
//productID
		Pattern p2 = Pattern.compile("(<td>NM-Mint</td>).{0,90}(<td>English</td>).{0,200}(\\$\\d+.\\d+)");
//priceUnit
		Pattern p3 = Pattern.compile("(\\$\\d+.\\d+)");
//price
		Pattern p4 = Pattern.compile("(<h2 class=\"product-list-title).{0,200}(</h2>)");
//nameUnit
		Pattern p5 = Pattern.compile("(<b>Rarity</b>).+?>(C|U|R|M)");
//rarity
		Pattern p6 = Pattern.compile("(<b>Set Name</b>).+?>.+?<");
		
//separate foil cards from nonfoil cards and cards belonging to older sets
		Matcher m1 = p1.matcher(mainPage);
		Matcher m4 = p4.matcher(mainPage);
		int foilFinder = 1;
		boolean done = false;
		while(m4.find() && !done){
			if(m4.group().contains("Foil") || !(containsRecentSets(m4.group()))){
				foilFinder++;
			}
			else{
				done = true;
			}
		}
		for (int i = 0; i < foilFinder-1; i++) {
			m1.find();
		}
//get infoPage
		Integer catNum = 0;
		if(m1.find()){
			catNum = Integer.valueOf(m1.group().substring(21));
		}
		String infoPage = scraper.getHTML("http://store.channelfireball.com/catalog/magic_singles/"+urlName+"/"+catNum);
//get price
		Matcher m2 = p2.matcher(infoPage);
		if(m2.find()){
			Matcher m3 = p3.matcher(m2.group());
			m3.find();
			price = Double.valueOf(m3.group().substring(1));
			System.out.println("Price: $"+price);
		}
		else{
			System.out.println("Out of Stock");
			price = 0.0;
			stock = false;
		}
//get rarity
		Matcher m5 = p5.matcher(infoPage);
		if(m5.find()){
			String uncutRarity = m5.group();
			rarity = uncutRarity.substring(uncutRarity.length()-1);
		}
		System.out.println("Rarity: "+rarity);
//get set
		Matcher m6 = p6.matcher(infoPage);
		if(m6.find()){
			String uncutSet = m6.group();
			set = uncutSet.substring(uncutSet.indexOf(">", 20)+1, uncutSet.length()-1);
		}
		System.out.println("Set: "+set);
//get reprints
		int printCounter = 0;
		Matcher m4_2 = p4.matcher(infoPage);
		while(m4_2.find()){
			printCounter++;
		}
		int intprints = printCounter/2;
		System.out.println("Reprints: "+intprints);
		reprints = intprints;
	}
	public void backupPriceScrape() throws ParserConfigurationException, SAXException{
		HTMLScraper scraper = new HTMLScraper();
		String urlName = name.replace(" ", "+");
		String mainPage = scraper.getHTML("http://www.cardkingdom.com/catalog/view?search=basic&filter%5Bname%5D="+urlName);

		Pattern p1 = Pattern.compile("(<a href=\"http://www.cardkingdom.com/catalog/item/)(.{0,400})(\\d+.\\d+)");
		Pattern p2 = Pattern.compile("(<td>\\d+.\\d+)");
		Matcher m1 = p1.matcher(mainPage);

		if(m1.find()){
			String selection = m1.group();
			Matcher m2 = p2.matcher(selection);
			m2.find();
			Double tempPrice = Double.valueOf(m2.group().substring(4));
			this.price = tempPrice;		
		}
		else{
			this.price = 0.0;
		}
	}
//helper functions
	public boolean containsRecentSets(String input){
		if(!(input.contains("Gatecrash")
				||input.contains("Return to Ravnica")
				||input.contains("Avacyn Restored")
				||input.contains("Dark Ascension")
				||input.contains("Innistrad")
				||input.contains("Magic 2013")
				||input.contains("Magic 2014")
				||input.contains("Dragon's Maze")
				||input.contains("Theros"))){
			return false;
		}
		else{
			return true;
		}
	}
	public double isRarity(String checkAgainst){
		if(rarity.equals(checkAgainst)){
			return 1;
		}
		else{
			return 0;
		}
	}

	public double isSet(String checkAgainst){
		if(set.equals(checkAgainst)){
			return 1;
		}
		else{
			return 0;
		}
	}
}
