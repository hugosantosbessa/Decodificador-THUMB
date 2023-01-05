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
		System.out.println(ServicesInstructions.showDecodeOpcodes("input3.txt"));
		ServicesInstructions.decodeOperationsCPU();
		System.out.println(ServicesInstructions.showRegisters());
		System.out.println(ServicesInstructions.showCPSR());
		System.out.println(ServicesInstructions.showProgramMemory());
		System.out.println(ServicesInstructions.showDataMemory());
		System.out.println(ServicesInstructions.showStackMemory());
	}
	
}
