/*************************************************************************************
 * @filename: 		App.java
 * @description:	Class that contains the application.
 * @version:		1.0
 * @created:  ‎		12/27/2022, ‏‎13:15:52
 * @modified: ‎		01/05/‎2023, ‏‎‏‎11:49:02
 * @revision:  		none
 * @ide:			Eclipse IDE 2022-06
 * @compiler: 		javac 18.0.1.1
 * @author: 		Hugo S. C. Bessa, hugobessa@alu.ufc.br
 * @organization:	UFC - Quixada
 *************************************************************************************/

public class App {

	public static void main(String[] args) {
		String file = "arquivos/input/file_5.txt";
		ThumbInstructions thumbInstructions = new ThumbInstructions(file);
		System.out.println(thumbInstructions.getStrOpcodes());
		System.out.println(thumbInstructions.showRegisters());
		System.out.println(thumbInstructions.showCPSR());
		System.out.println(thumbInstructions.showProgramMemory());
		System.out.println(thumbInstructions.showDataMemory());
		System.out.println(thumbInstructions.showStackMemory());
	}
	
}
