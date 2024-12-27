/*************************************************************************************
 * @filename: 		OperationsCPU.java
 * @description:	Mother class responsible for updating registers, flags and memory
 * @version:		1.0
 * @created:  ‎		12/31/2022, 11:09:26
 * @modified: ‎		01/05/‎2023, ‏‎11:49:02
 * @revision:  		none
 * @ide:			Eclipse IDE 2022-06
 * @compiler: 		javac 18.0.1.1
 * @author: 		Hugo S. C. Bessa, hugobessa@alu.ufc.br
 * @organization:	UFC - Quixada
 *************************************************************************************/

import java.util.Map;
import java.util.TreeMap;

public abstract class OperationsCPU {
	protected static int[] reg = new int[16];
	protected static int regCPSR;
	protected static Map<Integer, Integer> programMemory = new TreeMap<Integer, Integer>();
	protected static Map<Integer, Integer> dataMemory = new TreeMap<Integer, Integer>();
	protected static Map<Integer, Integer> stackMemory = new TreeMap<Integer, Integer>();
	protected static int contMemory = 0;
	protected static boolean bl = false;
	protected static boolean attPc = false;
	protected static boolean modeCPU = false;

	protected byte bits_15_12;
	protected byte bits_11_8;
	protected byte bits_7_4;
	protected byte bits_3_0;
	protected String formattedInstruction = "";
	protected enum signType{signed, unsigned};
	protected enum dataType{Byte, Short, Int};
	protected enum shiftType{LSL, LSR, ASR, ROR};

	
	/*****************************************************************************************
	 * Name: 		decodeCond
	 * Description:	Decode cond and returns String.
	 * 				See table B.2 of the book ARM System Developer’s Guide.
	 * Return:		String	
	 *****************************************************************************************/
	protected String decodeCond(int cond) {
		switch (cond) {
		case 0:
			return "EQ";
		case 1:
			return "NE";
		case 2:
			return "CS_HS";
		case 3:
			return "CC_LO";
		case 4:
			return "MI";
		case 5:
			return "PL";
		case 6:
			return "VS";
		case 7:
			return "VC";
		case 8:
			return "HI";
		case 9:
			return "LS";
		case 10:
			return "GE";
		case 11:
			return "LT";
		case 12:
			return "GT";
		case 13:
			return "LE";
		case 14:
			return "{AL}";
		default:
			return "";
		}
	}
	
	/*****************************************************************************************
	 * Name: 		checkCondition
	 * Description:	Checks the condition called and returns true if correct and false otherwise. 
	 * 				See table A.2 of the book ARM System Developer’s Guide.
	 * Return:		boolean	
	 *****************************************************************************************/
	protected boolean checkCondition(int cond) {
		/************************************************************************	
		 * Flag					Bit
		 * Negative		->  	31
		 * Zero 		->  	30
		 * Carry 		->  	29
		 * Overflow		->  	28 
		 * If the specific bit of the flag is 1, the flag is true. Else, is false.
		 ************************************************************************/
		boolean N = (regCPSR >>> 31) == 1 ? true : false, 
				Z = (regCPSR >>> 30) == 1 ? true : false, 
				C = (regCPSR >>> 29) == 1 ? true : false, 
				V = (regCPSR >>> 28) == 1 ? true : false;
		
		boolean result = false;
		switch(decodeCond(cond)) {
		/**********************************************************
		 * EQual (last result zero)
		 **********************************************************/
		case "EQ":		
			result = Z ? true : false;
			break;
		/**********************************************************
		 * Not Equal (last result nonzero) 
		 **********************************************************/
		case "NE":
			result = !Z ? true : false;
			break;
		/**********************************************************
		 * Carry Set, unsigned Higher or Same (following a compare)
		 **********************************************************/
		case "CS_HS":
			result = C ? true : false;
			break;
		/**********************************************************
		 * Carry Clear, unsigned LOwer (following a comparison)
		 **********************************************************/
		case "CC_LO":
			result = !C ? true : false;
			break;
		/**********************************************************
		 * MInus (last result negative)
		 **********************************************************/
		case "MI":
			result = N ? true : false;
			break;
		/**********************************************************
		 * PLus (last result greater than or equal to zero)
		 **********************************************************/
		case "PL":
			result = !N ? true : false;
			break;
		/**********************************************************
		 * V flag Set (signed overflow on last result)
		 **********************************************************/
		case "VS":
			result = V ? true : false;
			break;
		/**********************************************************
		 * V flag Clear (no signed overflow on last result)
		 **********************************************************/
		case "VC":
			result = !V ? true : false;
			break;
		/**********************************************************
		 * unsigned HIgher (following a comparison)
		 **********************************************************/
		case "HI":
			result = (C && !Z) ? true : false;
			break;
		/**********************************************************
		 * unsigned Lower or Same (following a comparison)
		 **********************************************************/
		case "LS":
			result = (!C || Z) ? true : false;
			break;
		/**********************************************************
		 * signed Greater than or Equal
		 **********************************************************/
		case "GE":
			result = (N == V) ? true : false;
			break;
		/**********************************************************
		 * signed Less Than
		 **********************************************************/
		case "LT":
			result = (N != V) ? true : false;
			break;
		/**********************************************************
		 * signed Greater Than
		 **********************************************************/
		case "GT":
			result = (N == V) && !Z ? true : false;
			break;
		/**********************************************************
		 * signed Less than or Equal
		 **********************************************************/
		case "LE":
			result = (N != V) || Z ? true : false;
			break;
		/**********************************************************
		 * ALways
		 **********************************************************/
		case "{AL}":
			result = true;
			break;
		case "":
			result = false;
			break;
		}
		return result;
	}
	
	/*****************************************************************************************
	 * Name: 		loadMemory
	 * Description:	Loads a value from memory according to size and sign and returns 
	 * Return:		int 	
	 *****************************************************************************************/
	protected int loadMemory(int address, dataType sizeData, signType signal) {
		/******************************************************
		 * In the cpsr E=1 so data accesses will be big-endian
		 ******************************************************/
		boolean E = ((regCPSR >>> 9) & 0x1) == 1 ? true : false;
		
		/***********************************************
		 * if dataMemory has already loaded the address, 
		 * then the data is loaded, if not loaded 0
		 ***********************************************/
		int data;
		if(dataMemory.containsKey(address))
			data = dataMemory.get(address);
		else {
			dataMemory.put(address, 0);
			data = 0;
			/******************************************
			 * If data = 0, treatment is not necessary
			 ******************************************/
			return data;
		}
		
		if(!E) {
			/****************************
			 * access little-endian mode
			 ****************************/
			switch (sizeData) {
			case Byte: 
				data &= 0x000000FF;
				if((signal == signType.signed) && (dataMemory.get(address) < 0)) 
					data |= 0xFFFFFF00;
				break;
			case Short:
				data &= 0x0000FFFF;
				if((signal == signType.signed) && (dataMemory.get(address) < 0)) 
					data |= 0xFFFF0000;
				break;
			case Int:
				break;
			}
		} 
		else { 
			/****************************
			 * access big-endian mode
			 ****************************/
			switch (sizeData) {
			case Byte: 
				data = (data & 0xFF000000) >>> 24;
				if(signal == signType.signed && (dataMemory.get(address) & 0x1) == 1) 
					data |= 0xFFFFFF00;
				break;
			case Short:
				data = ((data & 0xFF000000) >>> 24) | ((data & 0x00FF0000) >>> 8);
				if(signal == signType.signed && (dataMemory.get(address) & 0x1) == 1) 
					data |= 0xFFFF0000;
				break;
			case Int:
				data = 	((data & 0xFF000000) >>> 24) | ((data & 0x00FF0000) >>> 8) | 
						((data & 0x0000FF00) <<   8) | ((data & 0x000000FF) << 24); 
				break;
			}
		}
		return data;
	}
	
	/*****************************************************************************************
	 * Name: 		storeMemory
	 * Description:	Stores a value in memory according to size and sign
	 * Return:		void 	
	 *****************************************************************************************/
	protected void storeMemory(int address, int data, dataType sizeData) {
		/******************************************************
		 * In the cpsr E=1 so data accesses will be big-endian
		 ******************************************************/
		boolean E = ((regCPSR >>> 9) & 0x1) == 1 ? true : false;
		
		/****************************
		 * access little-endian mode
		 ****************************/
		if(!E)
			dataMemory.put(address, data);
		else {
			/****************************
			 * access big-endian mode
			 ****************************/
			switch (sizeData) {
			case Byte: 
				data = (data & 0xFF000000) >>> 24;
				dataMemory.put(address, data);
				break;
			case Short:
				data = ((data & 0xFF000000) >>> 24) | ((data & 0x00FF0000) >>> 8); 
				dataMemory.put(address, data);
				break;
			case Int:
				data =  ((data & 0xFF000000) >>> 24) | ((data & 0x00FF0000) >>> 8) | 
						((data & 0x0000FF00) <<   8) | ((data & 0x000000FF) << 24); 
				dataMemory.put(address, data);
				break;
			}
		}
	}
	
	/*****************************************************************************************
	 * Name: 		pushStack
	 * Description:	Store values on the memory stack
	 * Return:		void 	
	 *****************************************************************************************/
	protected void pushStack(byte regs, boolean lr) {
		for(int i = 0; i < 8; i++) { 
			if(((regs >> i) & 0x1) == 1) {
				reg[13] -= 4;
				stackMemory.put(reg[13], reg[i]);
			}
		}
		if(lr) {
			reg[13] -= 4;
			stackMemory.put(reg[13], reg[14]);
		}
	}
	
	/*****************************************************************************************
	 * Name: 		popStack
	 * Description:	Load values from memory stack
	 * Return:		void 	
	 *****************************************************************************************/
	protected void popStack(byte regs, boolean pc) {
		for(int i = 0; i < 8; i++) { 
			if(((regs >> i) & 0x1) == 1) {
				/***********************************************
				 * if stackMemory has already loaded the address, 
				 * then the data is loaded, if not loaded 0
				 ***********************************************/
				if(stackMemory.containsKey(reg[13])) {
					reg[i] =  stackMemory.get(reg[13]) ;
					reg[13] += 4;
				}
				else {
					stackMemory.put(reg[13], 0);
					reg[i] =  0;
					reg[13] += 4;
				}
			}
		}
		if(pc) {
			/***********************************************
			 * if stackMemory has already loaded the address, 
			 * then the data is loaded, if not loaded 0
			 ***********************************************/
			if(stackMemory.containsKey(reg[13])) {
				reg[15] = stackMemory.get(reg[13]);
				reg[13] += 4;
			}
			else {
				stackMemory.put(reg[13], 0);
				reg[15] =  0;
				reg[13] += 4;
			}
		}
	}
	
	/*****************************************************************************************
	 * Name: 		updateCPSR_N_Z
	 * Description:	Updates the negative and zero flags
	 * Return:		void 	
	 *****************************************************************************************/
	protected void updateCPSR_N_Z(int number) {
		/******************************************
		 * Negative flag, records bit 31 of the 
		 * result of flag-setting operations.
		 * Bit -> 31
		 * ****************************************/
		if((number >>> 31) == 1)
			regCPSR |= 1 << 31;
		else
			regCPSR &= ~(1 << 31);
		
		/******************************************
		 * Zero flag, records if the result of a 
		 * flag-setting operation is zero.
		 * Bit -> 30
		 ******************************************/
		if(number == 0)
			regCPSR |= 1 << 30;
		else
			regCPSR &= ~(1 << 30);
			
	}
	
	/*****************************************************************************************
	 * Name: 		updateCPSR_C_Shift
	 * Description:	Updates the flag carry used by the shifting circuit
	 * 				See table A.3 of the book ARM System Developer’s Guide.
	 * Return:		void 	
	 *****************************************************************************************/
	protected void updateCPSR_C_shift(int number, int k, shiftType shift) {
		boolean C;
		switch (shift) {
		case LSL:
			if(k == 0)
				return;
			else if((k >= 1) && (k <= 31)) 
				C = ((number >>> (k-1)) & 0x1) == 1 ? true : false;
			else if(k == 32)
				C = (number >>> 31) == 1 ? true : false;
			else
				C = false;
			break;
		case LSR:
			if(k == 0)
				return;
			else if((k >= 1) && (k <= 31)) 
				C = ((number >> (k-1)) & 0x1) == 1 ? true : false;
			else if(k == 32)
				C = (number >> 31) == 1 ? true : false;
			else
				C = false;
			break;
		case ASR:
			if(k == 0)
				return;
			else if((k >= 1) && (k <= 31)) 
				C = ((number >> (k-1)) & 0x1) == 1 ? true : false;
			else if(k >= 32)
				C = ((number >> 31) & 0x1) == 1 ? true : false;
			else
				return;	
			break;
		case ROR:
			if(k == 0)
				return;
			else if((k >= 1) && (k <= 31)) 
				C = ((number >> (k-1)) & 0x1) == 1 ? true : false;
			else if(k >= 32)
				C = ((number >> ((k-1)&31)) & 0x1)  == 1 ? true : false;
			else
				return;
			break;
		default:
			return;
		}
		
		/*********************
		 * Carry flag
		 * Bit -> 29
		 ********************/
		if(C)
			regCPSR |= 1 << 29;
		else 
			regCPSR &= ~(1 << 29);
			
	}
	
	/*****************************************************************************************
	 * Name: 		updateCPSR_ADD_signed
	 * Description:	Updates the negative, zero, carry and overflow flags when a 
	 *				signed sum occurs
	 * Return:		void 	
	 *****************************************************************************************/
	protected void updateCPSR_ADD_signed(int n1, int n2) {		
		/*************************************************
		 * sum result and updates negative and zero flags
		 *************************************************/
		int r = n1 + n2; 
		updateCPSR_N_Z(r);
		
		/*********************************************
		 * Bitmasks of the Carry and Overflow flags
		 * Carry 	-> 	29
		 * Overlfow	->	28
		 *********************************************/
		int C = (1 << 29), c = ~(1 << 29), V = (1 << 28), v = ~(1 << 28);
		
		/*********************************************
		 * Most significant bit of operands n1, n2, r
		 *********************************************/
		int msbN1 = n1 >>> 31, msbN2 = n2 >>> 31, msbR = r >>> 31;
		switch(msbN1+msbN2) {
		/*************************************************************************************
		 * sum of two positive numbers. Overflow if msbR == 1 and carry is not possible
		 ************************************************************************************/
		case 0:
			regCPSR = (msbR == 1) ? regCPSR | V : regCPSR & v;
			regCPSR &= c;
			break;
		/************************************************************************************
		 * sum of two numbers with different signs. No overflow and carry occurs if msbR == 0
		 ************************************************************************************/
		case 1:
			regCPSR = (msbR == 0) ? regCPSR | C : regCPSR & c;
			regCPSR &= v;
			break;
		/************************************************************************************
		 *  sum of two negative numbers. Overflow if msbR == 0 and carry always occurs
		 ************************************************************************************/
		case 2: 
			regCPSR = (msbR == 0) ? regCPSR | V : regCPSR & v;
			regCPSR |= C;
			break;
		}
	}	

	/*****************************************************************************************
	 * Name: 		updateCPSR_ADD_signed
	 * Description:	Updates the negative, zero, carry and overflow flags when a 
	 * 				subtraction occurs
	 * Return:		void 	
	 *****************************************************************************************/
	protected void updateCPSR_SUB_signed(int n1, int n2) {	
		/**************************************************************
		 *  The subtraction takes place with two's complement, 
		 *  n1 + ~n2 + 1, i.e. sum. o it is possible to use the 
		 *  updateCPSR_ADD_signed(int n1, int n2) method.
		 *  n1 - n2 = n1 + ~n2 + 1 
		 **************************************************************/
		n2 = ~n2 + 1; 
		updateCPSR_ADD_signed(n1, n2);
	}
	
	/*****************************************************************************************
	 * Name: 		updatePc
	 * Description:	Update the PC register and make sure it won't auto-increment
	 * Return:		void 	
	 *****************************************************************************************/
	protected void updatePc(int address) {
		reg[15] = address;
		attPc = false;
	}
}
