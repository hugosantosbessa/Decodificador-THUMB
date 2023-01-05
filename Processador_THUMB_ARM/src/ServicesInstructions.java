/*************************************************************************************
 * @filename: 		ServicesInstructions.java
 * @description:	Class that contains the methods to be called in the application.
 * 					Extends OperationsInstructions.
 * @version:		1.0
 * @created:  ‎		12/29/2022, 10:25:42
 * @modified: ‎		01/05/‎2023, ‏‎‏‎11:49:02
 * @revision:  		none
 * @ide:			Eclipse IDE 2022-06
 * @compiler: 		javac 18.0.1.1
 * @author: 		Hugo S. C. Bessa, hugobessa@alu.ufc.br
 * @organization:	UFC - Quixada
 *************************************************************************************/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ServicesInstructions extends OperationsInstructions {
	
	/*****************************************************************************************
	 * Name: 		decodeOperationsCPU
	 * Description: Function that acts as CPU. With the opcodes saved in the programMemory, 
	 * 				the CPU starts decoding the opcode at the current address of the 
	 * 				PC register.
	 * Return:		void	
	 *****************************************************************************************/
	public static void decodeOperationsCPU() {
		modeCPU = true;
		int rep = 0;
		while(rep < 10000) {
			try {
				new DecodeInstructions(programMemory.get(reg[15]));
				rep++;
			} 
			catch (Exception e) {
				System.out.println(
						"At pc="
						+ String.format("0x%08x", reg[15])
						+ " Instruction fetched from a location outside of a code section (.text or .exceptions).");
				return;
			}
		}
	}
	
	/*****************************************************************************************
	 * Name: 		decodeFile
	 * Description: Function that decodes the text file into an arraylist of integers. 
	 * 				The programMemory is also formed in this function.
	 * Return:		ArrayList<Integer>	
	 *****************************************************************************************/
	private static ArrayList<Integer> decodeFile(String arq) {
		try (
                FileReader inputFile = new FileReader(arq);
                BufferedReader inputStream = new BufferedReader(inputFile);
            )
        {
			String aux = inputStream.readLine();					// Read line
            String str[] = aux != null ? aux.split(": ") : null;	// Separate the String from the token ": "
            
            /******************************************************************
             * str[0] = addr; 					-> note used
             * str[1] =	instruction of 32 bits; -> split into 2 substrings
             * In Thumb mode the instructions are 16 bits long. 
             * Access is done in little-endian mode.
             ******************************************************************/
            String strL = str[1].substring(4),		// 16 least significant bits		
            	   strH = str[1].substring(0, 4);	// 16 most significant bits
            int cont = 0;							// programMemory counter
            
            /*************************************************************
             * listInstructions stores all opcodes in integer format. 
             * Its return is used to show the disassembly of the opcodes.
             **************************************************************/
            ArrayList<Integer> listInstructions = new ArrayList<Integer>();
            
            while(str != null) {
            	strL = str[1].substring(4); 					// 16 least significant bits
               	strH = str[1].substring(0, 4);					// 16 most significant bits
            	programMemory.put(cont, decodeString(strL));	// Add opcode in programMemory	
            	cont += 2;										// Increment cont  
            	programMemory.put(cont, decodeString(strH));	// Add opcode in programMemory
            	cont += 2;										// Increment cont 
            	listInstructions.add(decodeString(strL));		// Add opcode in listInstructions
            	listInstructions.add(decodeString(strH));		// Add opcode in listInstructions
            	aux = inputStream.readLine();					// Reaf next line
            	str = aux != null ? aux.split(": ") : null;		// Separate the String from the token ": "
            }
            return listInstructions;
        }
        catch(FileNotFoundException e) {
            System.out.println("\nerror: No file was read");
        }
        catch(IOException e) {
            System.out.println("\nerror: There was a problem reading the file");
        }
		return null;
	}
	
	/*****************************************************************************************
	 * Name: 		decodeString
	 * Description: Decodes a String in hexadecimal format to a hexadecimal Int
	 * Return:		Int	
	 *****************************************************************************************/
	private static int decodeString(String str) {
		return Integer.decode("0x"+str);
	}
		
	/*****************************************************************************************
	 * Name: 		showDecodeOpcodes
	 * Description: Show disassembly opcodes
	 * Return:		String	
	 *****************************************************************************************/
	public static String showDecodeOpcodes(String arq){
		StringBuilder str = new StringBuilder();
		str.append(".thumb\n");
		ArrayList<Integer> listInstructions = ServicesInstructions.decodeFile(arq);
		for (Integer integer : listInstructions) 
			str.append("\t" + new DecodeInstructions(integer) + "\n");
		return str.toString(); 
	}
	
	/*****************************************************************************************
	 * Name: 		showRegisters
	 * Description: Show the registers
	 * Return:		String	
	 *****************************************************************************************/
	public static String showRegisters() {
		String str  = "\n---------------------------------------------------------------------\n";
			   str +=   "|                        Registers R0-R15                           |";
			   str += "\n---------------------------------------------------------------------\n";
		for(int i = 0; i < reg.length; i++) {
			if(i <= 12)
				str += String.format("R%02d: 0x%08x\n", i,reg[i]);
			else if(i == 13)
				str += String.format(" SP: 0x%08x\n", reg[i]);
			else if(i == 14)
				str += String.format(" LR: 0x%08x\n", reg[i]);
			else
				str += String.format(" PC: 0x%08x", reg[i]);
		}
		return str;
	}
	
	/*****************************************************************************************
	 * Name: 		showCPSR
	 * Description: Show CPSR register
	 * Return:		String	
	 *****************************************************************************************/
	public static String showCPSR(){
		String 
		str  = "\n-------------------------------------------------------------------------------------------------\n";
		str +=   "|                                         Register CPSR                                         |";
		str += "\n-------------------------------------------------------------------------------------------------\n";
		str +=   "|31 30 29 28 27|26 25|24|23 22 21 20|19 18 17 16|15 14 13 12 11 10| 9  8 |7  6  5 |4  3  2  1  0|\n";
		str +=   "| N  Z  C  V  Q| Res |J |    Res    |  GE[3:0]  |       Res       | E  A |I  F  T |    mode     |\n";
		str += "|";
		for(int i = 31; i >= 0 ; i--) 
			str += (i != 0)	? String.format("%2d ", (regCPSR >> i) & 0x1): 
							  String.format("%2d|", (regCPSR >> i) & 0x1);
		str += String.format("\n|%45s%08x%43s", "0x",regCPSR, "|");
		str += "\n-------------------------------------------------------------------------------------------------";
		return str;
	}
	
	
	/*****************************************************************************************
	 * Name: 		showProgramMemory
	 * Description: Shows the region of memory used by the program
	 * Return:		String	
	 *****************************************************************************************/
	public static String showProgramMemory() {
		String 
		str  = "\n---------------------------------------------------------------------\n";
		str +=   "|                          Program Memory                           |";
		str += "\n---------------------------------------------------------------------\n";
		str += String.format("  %s    %8s\n","Adress", "Opcode");
		Iterator<Integer> addr = programMemory.keySet().iterator();
		Iterator<Integer> opcode = programMemory.values().iterator();
		while(addr.hasNext() && opcode.hasNext()) 
				str += String.format("0x%08x    0x%04x\n", addr.next(), opcode.next());
		return str;
	}
	
	/*****************************************************************************************
	 * Name: 		showDataMemory
	 * Description: Shows the region of memory used by the data
	 * Return:		String	
	 *****************************************************************************************/
	public static String showDataMemory() {
		String 
		str  = "\n---------------------------------------------------------------------\n";
		str +=   "|                           Data Memory                             |";
		str += "\n---------------------------------------------------------------------\n";
		str += String.format("  %s     %8s\n","Adress", "Data");
		Iterator<Integer> addr = dataMemory.keySet().iterator();
		Iterator<Integer> data = dataMemory.values().iterator();
		while(addr.hasNext() && data.hasNext()) 
			str += String.format("0x%08x    0x%08x\n", addr.next(), data.next());
		return str;
	}
	
	/*****************************************************************************************
	 * Name: 		showStackMemory
	 * Description: Shows the region of memory used by the stack
	 * Return:		String	
	 *****************************************************************************************/
	public static String showStackMemory() {
		String 
		str  = "\n---------------------------------------------------------------------\n";
		str +=   "|                           Stack Memory                            |";
		str += "\n---------------------------------------------------------------------\n";
		str += String.format("  %s     %8s\n","Adress", "Data");
		Iterator<Integer> addr = stackMemory.keySet().iterator();
		Iterator<Integer> data = stackMemory.values().iterator();
		while(addr.hasNext() && data.hasNext()) 
				str += String.format("0x%08x    0x%08x\n", addr.next(), data.next());
		return str;
	}
}
