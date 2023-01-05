/*************************************************************************************
 * @filename: 		DecodeInstructions.java
 * @description:	Class responsible for decoding opcodes. 
 * 					Extends OperationsInstructions.
 * 					See table B.5 of the book ARM System Developer’s Guide.
 * @version:		1.0
 * @created:  ‎		12/27/2022, ‏‎14:55:35
 * @modified: ‎		01/05/‎2023, ‏‎‏‎11:49:02
 * @revision:  		none
 * @ide:			Eclipse IDE 2022-06
 * @compiler: 		javac 18.0.1.1
 * @author: 		Hugo S. C. Bessa, hugobessa@alu.ufc.br
 * @organization:	UFC - Quixada
 *************************************************************************************/


public class DecodeInstructions extends OperationsInstructions{
	
	/*****************************************************************************************
	 * Name: 		DecodeInstructions
	 * Description: Constructor method	
	 *****************************************************************************************/
	public DecodeInstructions(int instruction) {
		/******************************************************
		 * The instruction is separated into four nibbles.
		 * |15 14 13 12|11 10  9  8 |7  6  5  4 |3  2  1 0|
		 * |bits_15_12 |  bits_11_8 | bits_7_4  |bits_3_0 |
		 ******************************************************/
		bits_15_12 	=	(byte) ((instruction >>> 12) & 0xF); 	// (ABCD >>> 12)  ->    A & 0xF -> A 
		bits_11_8 	=	(byte) ((instruction >>>  8) & 0xF); 	// (ABCD >>>  8)  ->   AB & 0xF -> B
		bits_7_4 	= 	(byte) ((instruction >>>  4) & 0xF);	// (ABCD >>>  4)  ->  ABC & 0xF -> C
		bits_3_0 	= 	(byte) ((instruction >>>  0) & 0xF);    // (ABCD >>>  0)  -> ABCD & 0xF -> D 
		
		/*********************************************
		 * Calls the method that decodes the opcode 
		 *********************************************/
		decode();
		
		/*************************************************************************************
		 * If modeCPU == false, the decoding will only be to disassemble the opcodes and form 
		 * the program memory. Else, then all flag registers and memory will actually be used.
		 * The modeCPU is true when decodeOperationsCPU() method is called 
		 *************************************************************************************/
		if(!modeCPU)
			contMemory += 2;
		else {
			if(attPc)
				reg[15] += 2;
			else 
				attPc = true;
		}
	}
	
	/*****************************************************************************************
	 * Name: 		decode
	 * Description: Decode opcode and call method referring to opcode instruction
	 * 				See table B.5 of the book ARM System Developer’s Guide.
	 * Return: 		void
	 *****************************************************************************************/
	private void decode() {
		switch(bits_15_12){
		// Instrucoes LSL | LSR  
		case 0: 
			if((bits_11_8 >>> 3) == 0) 
				LSL_Ld_Lm_immed5();
			else 
				LSR_Ld_Lm_immed5();
			break;
		// Instrucoes ASR | ADD | SUB 
		case 1:
			// Instrucao ASR
			if((bits_11_8 >>> 3) == 0)
				ASR_Ld_Lm_immed5();
			// Instrucoes ADD | SUB
			else if((bits_11_8 >>> 2) == 2){
				if(((bits_11_8 >>> 1) & 0x1) == 0)
					ADD_Ld_Ln_Lm();
				else
					SUB_Ld_Ln_Lm();
			}
			// Instrucoes ADD_immed3 | SUB_immed3 ->  ADD Ld, Ln, #<immed3> 
			else {
				if(((bits_11_8 >>> 1) & 0x1) == 0)
					ADD_Ld_Ln_immed3();
				else
					SUB_Ld_Ln_immed3();
			}
			break;
		// Instrucoes MOV_immed8 | CMP_immed8 -> 3. MOV Ld, #<immed8>
		case 2:
			if((bits_11_8 >>> 3) == 0)
				MOV_Ld_immed8();
			else
				CMP_Ld_immed8();
			break;
		// Instrucoes ADD_immed8 | SUB_immed8 -> ADD Ld, #<immed8> 
		case 3:
			if((bits_11_8 >>> 3) == 0)
				ADD_Ld_immed8();
			else
				SUB_Ld_immed8();	
			break;
		case 4:
			// Instrucoes AND | EOR | LSL | LSR -> AND Ld, Lm 
			if(bits_11_8 == 0){
				if((bits_7_4 >>> 2) == 0)
					AND_Ld_Lm();
				else if((bits_7_4 >>> 2) == 1)
					EOR_Ld_Lm();
				else if((bits_7_4 >>> 2) == 2)
					LSL_Ld_Ls();
				else
					LSR_Ld_Ls();
			}
			// Instrucoes 	ASR | ADC | SBC | ROR -> ASR Ld, Lm 	
			else if(bits_11_8 == 1) {
				if((bits_7_4 >>> 2) == 0)
					ASR_Ld_Ls();
				else if((bits_7_4 >>> 2) == 1)
					ADC_Ld_Lm();
				else if((bits_7_4 >>> 2) == 2)
					SBC_Ld_Lm();
				else
					ROR_Ld_Ls();
			}
			// Instrucoes TST | NEG | CMP | CMN 
			else if(bits_11_8 == 2) {
				if((bits_7_4 >>> 2) == 0)
					TST_Ln_Lm();
				else if((bits_7_4 >>> 2) == 1)
					NEG_Ld_Lm();
				else if((bits_7_4 >>> 2) == 2)
					CMP_Ln_Lm();
				else
					CMN_Ln_Lm();
			}
			// Instrucoes ORR | MUL | BIC | MVN
			else if(bits_11_8 == 3){
				if((bits_7_4 >>> 2) == 0)
					ORR_Ld_Lm();
				else if((bits_7_4 >>> 2) == 1)
					MUL_Ld_Lm();
				else if((bits_7_4 >>> 2) == 2)
					BIC_Ld_Lm();
				else
					MVN_Ld_Lm();
			}
			// Instrucao CPY
			else if(bits_11_8 == 6 && bits_7_4 >>> 2 == 0)
				CPY_Ld_Lm();
			// Instrucao ADD | MOV
			else if(bits_11_8 >>> 2 == 1 && (bits_11_8 & 0x1) == 0 && bits_7_4 >>> 2 != 0){
				if(bits_7_4 >>> 2 == 1) {
					if(((bits_11_8 >>> 1) & 0x1) == 0)
						ADD_Ld_Hm();
					else
						MOV_Ld_Hm();
				}
				else if(bits_7_4 >>> 2 == 2) {
					if(((bits_11_8 >>> 1) & 0x1) == 0)
						ADD_Hd_Lm();
					else
						MOV_Hd_Lm();
				}
				else {
					if(((bits_11_8 >>> 1) & 0x1) == 0)
						ADD_Hd_Hm();
					else
						MOV_Hd_Hm();
				}
			}
			// Instrucao CMP
			else if(bits_11_8 == 5) {
				if(bits_7_4 >>> 2 == 1)
					CMP_Ln_Hm();
				else if(bits_7_4 >>> 2 == 2)
					CMP_Hn_Lm();
				else if(bits_7_4 >>> 2 == 3)
					CMP_Hn_Hm();
			}
			// Instrucao BL | BLX -> BX Rm
			else if(bits_11_8 == 7){
				if(bits_7_4 >>> 3 == 0)
					BX_Rm();
				else
					BLX_Rm();
			}
			// Instrucao LDR 
			else if((bits_11_8 >>> 3) == 1)
				LDR_Ld_immed8();
			break;
		case 5:
			// Instrucoes STR | STRH | STRB | LDRSB
			if((bits_11_8 >>> 3) == 0){
				if(((bits_11_8 >>> 1) & 0x3) == 0)
					STR_Ld_Ln_Lm();
				else if(((bits_11_8 >>> 1) & 0x3) == 1)
					STRH_Ld_Ln_Lm();
				else if(((bits_11_8 >>> 1) & 0x3) == 2)
					STRB_Ld_Ln_Lm();
				else 
					LDRSB_Ld_Ln_Lm();
			}
			// Instrucoes LDR | LDRH | LDRB | LDRSH pre -> LDRSB Ld, [Ln, Lm]
			else{
				if(((bits_11_8 >>> 1) & 0x3) == 0)
					LDR_Ld_Ln_Lm();
				else if(((bits_11_8 >>> 1) & 0x3) == 1)
					LDRH_Ld_Ln_Lm();
				else if(((bits_11_8 >>> 1) & 0x3) == 2)
					LDRB_Ld_Ln_Lm();
				else 
					LDRSH_Ld_Ln_Lm();
			}
			break;
		// Instrucoes STR | LDR
		case 6:
			if((bits_11_8 >>> 3) == 0)
				STR_Ld_Ln_immed5();
			else
				LDR_Ld_Ln_immed5();
			break;
		// Instrucoes STRB | LDRB
		case 7:
			if((bits_11_8 >>> 3) == 0)
				STRB_Ld_Ln_immed5();
			else
				LDRB_Ld_Ln_immed5();
			break;
		// Instrucoes STRH | LDRH
		case 8:
			if((bits_11_8 >>> 3) == 0)
				STRH_Ld_Ln_immed5();
			else
				LDRH_Ld_Ln_immed5();
			break;
		// Instrucoes STR | LDR	
		case 9:
			if((bits_11_8 >>> 3) == 0)
				STR_Ld_Sp_immed8();
			else
				LDR_Ld_Sp_immed8();
			break;
		// Instrucoes ADD Ld, pc, #<immed8>*4 | ADD Ld, sp, #<immed8>*4	
		case 10:
			if((bits_11_8 >>> 3) == 0)
				ADD_Ld_pc_immed8();
			else
				ADD_Ld_sp_immed8();
			break;
		case 11:
			// Instrucoes ADD | SUB ->  ADD sp, #<immed7>*4
			if(bits_11_8 == 0) {
				if((bits_7_4 >>> 3) == 0)
					ADD_sp_immed7();
				else
					SUB_sp_immed7();
			}
			// Instrucoes SXTH | SXTB | UXTH | UXTB
			else if(bits_11_8 == 2) {
				if((bits_7_4 >>> 2) == 0)
					SXTH_Ld_Lm();
				else if((bits_7_4 >>> 2) == 1)
					SXTB_Ld_Lm();
				else if((bits_7_4 >>> 2) == 2)
					UXTH_Ld_Lm();
				else
					UXTB_Ld_Lm();
			}
			// Instrucoes REV | REV16 | | REVSH
			else if(bits_11_8 == 10) {
				if((bits_7_4 >>> 2) == 0)
					REV_Ld_Lm();
				else if((bits_7_4 >>> 2) == 1)
					REV16_Ld_Lm();
				else if((bits_7_4 >>> 2) == 2) 
					UNDEFINED();
				else
					REVSH_Ld_Lm();
			}
			// Instrucoes PUSH | POP
			else if(((bits_11_8 >>> 1) & 0x3) == 2) {
				if((bits_11_8 >>> 3) == 0)
					PUSH_R_register_list();
				else
					POP_R_register_list();
			}
			// Instrucoes SETEND LE | SETEND BE
			else if(bits_11_8 == 6 && bits_7_4 == 5) {
				if((bits_3_0 >>> 3) == 0)
					SETEND_LE();
				else
					SETEND_BE();
			}
			// Instrucoes CPSIE | CPSID
			else if(bits_11_8 == 6 && bits_7_4 >= 6) {
				if((bits_7_4 & 0x1) == 0)
					CPSIE();
				else
					CPSID();
			}
			// Instrucoes BKPT
			else if(bits_11_8 == 14) {
				BKPT_immed8();
			}
			break;
		// Instrucoes STMIA | LDMIA -> STMIA Ln!, {register-list} 
		case 12:
			if((bits_11_8 >>> 3) == 0)
				STMIA();
			else
				LDMIA();
			break;
		// Instruction B<cond>
		case 13:
			if(bits_11_8 < 0xE)
				B_cond_offset8();
			else if(bits_11_8 == 0xE) 
				UNDEFINED();
			else
				SWI();
			break;
		case 14:
			if((bits_11_8 >>> 3) == 0)
				B_offset11();
			else 
				BLX_offset10();
			break;
		case 15:
			if((bits_11_8 >>> 3) == 0) 
				BLorBLX();
			else 
				BL_offset11();
			break;
		default:
			UNDEFINED();
			break;
		}
	}
	
	/*****************************************************************************************
	 * Name: 		toString
	 * Description: returns a String with disassembled opcode
	 * Return: 		String
	 *****************************************************************************************/
	@Override
	public String toString() {
		return formattedInstruction;
	}
	
}
