import flanagan.analysis.Regression;


public class CardRegression {

	private int variables;
	private double[][] xArray;
	private double[] yArray;
	private Card[] cardArray;

	public CardRegression(int numVars){
		this.variables = numVars+1;

	}
	public void buildMatrix(Card[] cardArray){
		double[][] mainMatrix = new double[variables][cardArray.length];
		double[] priceMatrix = new double[cardArray.length];
		this.cardArray = cardArray;

		for (int i = 0; i < cardArray.length; i++) {
			Card current = cardArray[i];
			mainMatrix[0][i] = Math.log(current.Count());
			mainMatrix[1][i] = current.Reprints();
			mainMatrix[2][i] = current.isRarity("M");
			mainMatrix[3][i] = current.isRarity("R");
			mainMatrix[4][i] = current.isRarity("U");
			mainMatrix[5][i] = current.isSet("Gatecrash");
			mainMatrix[6][i] = current.isSet("Return to Ravnica");
			mainMatrix[7][i] = current.isSet("Avacyn Restored");
			mainMatrix[8][i] = current.isSet("Dark Ascension");
			mainMatrix[9][i] = current.isSet("Innistrad");

			priceMatrix[i] = Math.log(current.Price());
		}
		xArray = mainMatrix;
		yArray = priceMatrix;
	}
	public void Regress(){
		Regression reg = new Regression(xArray, yArray);
		reg.linearPlot();
		double[] yCalc = reg.getYcalc();
		for (int i = 0; i < yCalc.length; i++) {
			
			double calcPrice = Math.round((float)100*Math.exp(yCalc[i]));
			
			System.out.println(cardArray[i].Name()+":");
			System.out.println("Current Price: "+cardArray[i].Price());
			System.out.println("Predicted Price: "+calcPrice/100);
			System.out.println();
		}
	}
}
