/*************************************************************************************
 * @filename: 		OperationsInstructions.java
 * @description:	Class responsible for performing instruction operations. 
 * 					Extends the OperationsCPU class. 
 * 					See appendix A of the book ARM System Developer’s Guide. 
 * @version:		1.0
 * @created:  ‎		12/27/2022, ‏‎14:55:35
 * @modified: ‎		01/05/‎2023, ‏‎‏‎11:49:02
 * @revision:  		none
 * @ide:			Eclipse IDE 2022-06
 * @compiler: 		javac 18.0.1.1
 * @author: 		Hugo S. C. Bessa, hugobessa@alu.ufc.br
 * @organization:	UFC - Quixada
 *************************************************************************************/


public abstract class OperationsInstructions extends OperationsCPU {

	/*****************************************************************************************
	 * Name: 		LSL_Ld_Lm_immed5
	 * Tag:			LSL
	 * Description:	Logical shift left for Thumb 
	 * Opcode: 		0000 0 <immed-5b> <Lm-3b> <Ld-3b>
	 * Disassembly: LSL Ld, Lm, #immed
	 * Action:		Ld = Lm LSL #<immed>
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <shifter_C>
	 *****************************************************************************************/
	protected void LSL_Ld_Lm_immed5() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed5 = ((bits_11_8 & 0x7) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LSL R" + Ld + ", R" + Lm + ", #" + immed5;
		if(modeCPU) {
			reg[Ld] = reg[Lm] << immed5;
			updateCPSR_C_shift(reg[Lm], immed5, shiftType.LSL);
			updateCPSR_N_Z(reg[Ld]);
		}
	}
	
	/*****************************************************************************************
	 * Name: 		LSR_Ld_Lm_immed5
	 * Tag:			LSR
	 * Description: Logical shift right for Thumb 
	 * Opcode: 		0000 0 <immed-5b> <Lm-3b> <Ld-3b>
	 * Disassembly: LSR LD, LM, #immed
	 * Action:		Ld = Lm LSR #<immed>
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <shifter_C>
	 *****************************************************************************************/
	protected void LSR_Ld_Lm_immed5() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed5 = ((bits_11_8 & 0x7) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LSR R" + Ld + ", R" + Lm + ", #" + immed5;
		if(modeCPU) {
			reg[Ld] = reg[Lm] >>> immed5;	
			updateCPSR_C_shift(reg[Lm], immed5, shiftType.LSR);
			updateCPSR_N_Z(reg[Ld]);
		}
	}
	
	/**************************************************
	 * Name: 		ASR_Ld_Lm_immed5
	 * Tag:			ASR
	 * Description: Arithmetic shift right for Thumb 
	 * Opcode: 		0001 0 <immed-5b> <Lm-3b> <Ld-3b>
	 * Disassembly: ASR LD, LM, #immed
	 * Action:		Ld = Lm ASR #<immed>
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <shifter_C>
	 **************************************************/
	protected void ASR_Ld_Lm_immed5() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed5 = ((bits_11_8 & 0x7) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "ASR R" + Ld + ", R" + Lm + ", #" + immed5;
		if(modeCPU) {
			reg[Ld] = reg[Lm] >> immed5;	
			updateCPSR_C_shift(reg[Lm], immed5, shiftType.ASR);
			updateCPSR_N_Z(reg[Ld]);
		}
	}
	
	/*****************************************************************************************
	 * Name: 		ADD_Ld_Ln_Lm
	 * Tag:			ADD
	 * Description: Add two 32-bit values 
	 * Opcode: 		0001 100 <Lm-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: ADD LD, LN, LM
	 * Action:		Ld = Ln + Lm
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <UnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/
	protected void ADD_Ld_Ln_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int Lm = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "ADD R" + Ld + ", R" + Ln + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] = reg[Ln] + reg[Lm];
			updateCPSR_ADD_signed(reg[Ln], reg[Lm]);
		}
	}
	
	/******************************************************************************************
	 * Name: 		SUB_Ld_Ln_Lm
	 * Tag:			SUB
	 * Description: Subtract two 32-bit values 
	 * Opcode: 		0001 101 <Lm-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: SUB LD, LN, LM
	 * Action:		Ld = Ln - Lm
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <NoUnsignedOverflow>, V = <SignedOverflow>
	 ******************************************************************************************/
	protected void SUB_Ld_Ln_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int Lm = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "SUB R" + Ld + ", R" + Ln + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] = reg[Ln] - reg[Lm];
			updateCPSR_SUB_signed(reg[Ln], reg[Lm]);
		}
	}
	
	/*****************************************************************************************
	 * Name:		ADD_Ld_Ln_immed3
	 * Tag:			ADD
	 * Description: Add two 32-bit values 
	 * Opcode: 		0001 110 <immed-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: ADD LD, LN, #immed
	 * Action:		Ld = Ln + #immed
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <UnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/	
	protected void ADD_Ld_Ln_immed3() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed3 = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "ADD R" + Ld + ", R" + Ln + ", #" + immed3;
		if(modeCPU) {
			reg[Ld] = reg[Ln] + immed3;
			updateCPSR_ADD_signed(reg[Ln], immed3);
		}
	}
	
	/******************************************************************************************
	 * Name:		SUB_Ld_Ln_immed3	
	 * Tag:			SUB
	 * Description: Subtract two 32-bit values 
	 * Opcode: 		0001 111 <immed-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: SUB LD, LN, #immed
	 * Action:		Ld = Ln - #immed
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <NoUnsignedOverflow>, V = <SignedOverflow>
	 ******************************************************************************************/	
	protected void SUB_Ld_Ln_immed3() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed3 = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "ADD R" + Ld + ", R" + Ln + ", #" + immed3;
		if(modeCPU) {
			reg[Ld] = reg[Ln] + immed3;
			updateCPSR_SUB_signed(reg[Ln], immed3);
		}
	}

	/******************************************************************************************
	 * Name: 		MOV_Ld_immed8
	 * Tag:			MOV
	 * Description: Move a 32-bit value into a register 
	 * Opcode: 		0010 0 <Ld-3b> <immed-8b>
	 * Disassembly: MOV LD, #immed
	 * Action:		Ld = #immed
	 * Effect CPSR: N = <Negative>, Z = <Zero>
	 ******************************************************************************************/	
	protected void MOV_Ld_immed8() {
		int Ld =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + (bits_3_0);
		formattedInstruction = "MOV R" + Ld + ", #" + immed8;
		if(modeCPU) {
			reg[Ld] = immed8;
			updateCPSR_N_Z(reg[Ld]);
		}
	}
	
	/******************************************************************************************
	 * Name:		CMP_Ld_immed8
	 * Tag:			CMP
	 * Description: Compare two 32-bit integers 
	 * Opcode: 		0010 1 <Ln-3b> <immed-8b>
	 * Disassembly: CMP LD, #immed
	 * Action:		cpsr flags set on the result of (Ln - <immed8>)
	 * Effect CPSR: N = <Negative>, Z = <Zero>
	 ******************************************************************************************/
	protected void CMP_Ld_immed8() {
		int Ln =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + (bits_3_0);
		formattedInstruction = "CMP R" + Ln + ", #" + immed8;
		if(modeCPU) 
			updateCPSR_SUB_signed(reg[Ln], immed8);
	}

	/*****************************************************************************************
	 * Name:		ADD_Ld_immed8
	 * Tag:			ADD
	 * Description: Add two 32-bit values 
	 * Opcode: 		0011 0 <Ld-3b> <immed-8b>
	 * Disassembly: ADD LD, #immed
	 * Action:		Ld = Ld + #immed
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <UnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/
	protected void ADD_Ld_immed8() {
		int Ld =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + (bits_3_0);
		formattedInstruction = "MOV R" + Ld + ", #" + immed8;
		if(modeCPU) {
			reg[Ld] = reg[Ld] + immed8;
			updateCPSR_ADD_signed(reg[Ld], immed8);
		}
	}

	/*****************************************************************************************
	 * Name:		SUB_Ld_immed8
	 * Tag:			SUB
	 * Description: Subtract two 32-bit values 
	 * Opcode: 		0011 1 <Ld-3b> <immed-8b>
	 * Disassembly: SUB LD, #immed
	 * Action:		Ld = Ld - #immed
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <NoUnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/
	protected void SUB_Ld_immed8() {
		int Ld =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + (bits_3_0);
		formattedInstruction = "SUB R" + Ld + ", #" + immed8;
		if(modeCPU) {
			reg[Ld] = reg[Ld] - immed8;
			updateCPSR_SUB_signed(reg[Ld], immed8);
		}
	}

	/*****************************************************************************************
	 * Name:		AND_Ld_Lm
	 * Tag:			AND
	 * Description: Logical bitwise AND of two 32-bit values  
	 * Opcode: 		0100 0000 00 <Lm-3b> <Ld-3b>
	 * Disassembly: AND LD, LM
	 * Action:		Ld = Ld & LM
	 * Effect CPSR: N = <Negative>, Z = <Zero>
	 *****************************************************************************************/	
	protected void AND_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "AND R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] = reg[Ld] & reg[Lm];
			updateCPSR_N_Z(reg[Ld]);
		}
	}
	
	/*****************************************************************************************
	 * Name:		EOR_Ld_Lm
	 * Tag:			EOR
	 * Description: Logical exclusive OR of two 32-bit values  
	 * Opcode: 		0100 0000 01 <Lm-3b> <Ld-3b>
	 * Disassembly: EOR LD, LM
	 * Action:		Ld = Ld ^ LM
	 * Effect CPSR: N = <Negative>, Z = <Zero>
	 *****************************************************************************************/		
	protected void EOR_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "EOR R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] = reg[Ld] ^ reg[Lm];
			updateCPSR_N_Z(reg[Ld]);
		}
	}
	
	/*****************************************************************************************
	 * Name:		LSL_Ld_Ls
	 * Tag:			LSL
	 * Description: Logical shift left for Thumb 
	 * Opcode: 		0100 0000 10 <Ls-3b> <Ld-3b>
	 * Disassembly: LSL LD, LS
	 * Action:		Ld = Ld LSL Ls[7:0]
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <shifter_C>
	 *****************************************************************************************/	
	protected void LSL_Ld_Ls() {
		int Ld =  bits_3_0 & 0x7;
		int Ls = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "LSL R" + Ld + ", R" + Ls;
		if(modeCPU) {
			int k = (reg[Ls] & 0xFF);
			if(k > 31)
				k &= 31;
			reg[Ld] = reg[Ld] << k;
			updateCPSR_N_Z(reg[Ld]);
		}
	}
	
	/*****************************************************************************************
	 * Name:		LSR_Ld_Ls
	 * Tag:			LSR
	 * Description: Logical shift right for Thumb 
	 * Opcode: 		0100 0000 11 <Ls-3b> <Ld-3b>
	 * Disassembly: LSR LD, LS
	 * Action:		Ld = Ld LSR Ls[7:0]
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <shifter_C>
	 *****************************************************************************************/ 
	protected void LSR_Ld_Ls() {
		int Ld =  bits_3_0 & 0x7;
		int Ls = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "LSR R" + Ld + ", R" + Ls;
		if(modeCPU) {
			int k = (reg[Ls] & 0xFF);
			if(k > 31)
				k &= 31;
			reg[Ld] = reg[Ld] >>> k;
			updateCPSR_N_Z(reg[Ld]);
		}
	}

	/*****************************************************************************************
	 * Name:		ASR_Ld_Ls
	 * Tag:			ASR
	 * Description: Arithmetic shift right for Thumb 
	 * Opcode: 		0100 0001 00 <Ls-3b> <Ld-3b>
	 * Disassembly: ASR LD, LS
	 * Action:		Ld = Ld ASR Ls[7:0]
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <shifter_C>
	 *****************************************************************************************/	
	protected void ASR_Ld_Ls() {
		int Ld =  bits_3_0 & 0x7;
		int Ls = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "ASR R" + Ld + ", R" + Ls;
		if(modeCPU) {
			int k = reg[Ls] & 0xFF;
			if(k >= 0 && k <= 31)
				reg[Ld] = reg[Ld] >> k;
			else
				reg[Ld] = -(reg[Ls] >>> 31);
			updateCPSR_N_Z(reg[Ld]);
		}
	}

	/*****************************************************************************************
	 * Name:		ADC_Ld_Lm
	 * Tag:			ADC
	 * Description: Add two 32-bit values and carry 
	 * Opcode: 		0100 0001 01 <Lm-3b> <Ld-3b>
	 * Disassembly: ADC LD, LM
	 * Action:		Ld = Ld + Lm + C 
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <UnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/		
	protected void ADC_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int C = ((regCPSR >>> 29) & 0x1);
		formattedInstruction = "ADC R" + Ld + ", R" + Lm;
		if(modeCPU) {
			updateCPSR_ADD_signed(reg[Ld], (reg[Lm]+C));
			reg[Ld] = reg[Ld] + reg[Lm] + C;
		}
	}
	
	/*****************************************************************************************
	 * Name:		SBC_Ld_Lm
	 * Tag:			SBC
	 * Description: Subtract with carry 
	 * Opcode: 		0100 0001 10 <Lm-3b> <Ld-3b>
	 * Disassembly: SBC LD, LM
	 * Action:		Ld = Ld - Lm - (~C) 
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <NoUnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/	
	protected void SBC_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int C = ((regCPSR >>> 29) & 0x1);
		formattedInstruction = "SBC R" + Ld + ", R" + Lm;
		if(modeCPU) {
			updateCPSR_SUB_signed(reg[Ld], (reg[Lm] - (~C)));
			reg[Ld] = reg[Ld] - reg[Lm] - (~C);
		}
	}

	/*****************************************************************************************
	 * Name:		ROR_Ld_Ls
	 * Tag:			ROR
	 * Description: Rotate right for Thumb 
	 * Opcode: 		0100 0001 11 <Lm-3b> <Ld-3b>
	 * Disassembly: ROR LD, LS
	 * Action:		Ld = Ld ROR Ls[7:0] 
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <shifter_C>
	 *****************************************************************************************/		
	protected void ROR_Ld_Ls() {
		int Ld =  bits_3_0 & 0x7;
		int Ls = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "ROR R" + Ld + ", R" + Ls;
		if(modeCPU) {
			int k = reg[Ls] & 0xFF; // k range
			if(k >= 32) 
				k &= 31;
			// Operacao de rotation -> (number >>> k) | (number << (INT_BITS - k))
			reg[Ld] = (reg[Ld] >>> k | (reg[Ld] << (32-k)));
			updateCPSR_C_shift(reg[Ls], k, shiftType.ROR);
			updateCPSR_N_Z(reg[Ld]);
		}
	}

	/*****************************************************************************************
	 * Name:		TST_Ln_Lm
	 * Tag:			TST
	 * Description: Test bits of a 32-bit value 
	 * Opcode: 		0100 0010 00 <Lm-3b> <Ln-3b>
	 * Disassembly: TST LN, LM
	 * Action:		Set the cpsr on the result of (Ln & Lm) 
	 * Effect CPSR: N = <Negative>, Z = <Zero>
	 *****************************************************************************************/	  
	protected void TST_Ln_Lm() {
		int Ln =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		if(modeCPU) {
			formattedInstruction = "TST R" + Ln + ", R" + Lm;
			int result = reg[Ln] & reg[Lm];
			updateCPSR_N_Z(result);
		}
	}

	/*****************************************************************************************
	 * Name:		NEG_Ld_Lm
	 * Tag:			NEG
	 * Description: Negate value in Thumb 
	 * Opcode: 		0100 0010 01 <Lm-3b> <Ld-3b>
	 * Disassembly: NEG LD, LM
	 * Action:		Ld = -Lm 
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <NoUnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/		
	protected void NEG_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "NEG R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] = -reg[Lm];
			updateCPSR_SUB_signed(0, reg[Lm]);
		}
	}
	
	/*****************************************************************************************
	 * Name:		CMP_Ln_Lm
	 * Tag:			CMP
	 * Description: Compare two 32-bit integers  
	 * Opcode: 		0100 0010 10 <Lm-3b> <Ln-3b>
	 * Disassembly: TST LN, LM
	 * Action:		cpsr flags set on the result of (Ln - Lm) 
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <NoUnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/	  
	protected void CMP_Ln_Lm() {
		int Ln =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "CMP R" + Ln + ", R" + Lm;
		if(modeCPU)
			updateCPSR_SUB_signed(reg[Ln], reg[Lm]);
	}

	/*****************************************************************************************
	 * Name:		CMN_Ln_Lm
	 * Tag:			CMN
	 * Description: Compare negative 
	 * Opcode: 		0100 0010 11 <Lm-3b> <Ln-3b>
	 * Disassembly: TST LN, LM
	 * Action:		cpsr flags set on the result of (Ln + Lm) 
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <UnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/	
	protected void CMN_Ln_Lm() {
		int Ln =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "CMN R" + Ln + ", R" + Lm;
		if(modeCPU)
			updateCPSR_ADD_signed(reg[Ln], reg[Lm]);
	}

	/*****************************************************************************************
	 * Name:		ORR_Ld_Lm
	 * Tag:			ORR
	 * Description: Logical bitwise OR of two 32-bit values
	 * Opcode: 		0100 0011 00 <Lm-3b> <Ld-3b>
	 * Disassembly: ORR Ld, Lm
	 * Action:		Ld = Ld | Lm 
	 * Effect CPSR: N = <Negative>, Z = <Zero>
	 *****************************************************************************************/	
	protected void ORR_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "ORR R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] = reg[Ld] | reg[Lm];
			updateCPSR_N_Z(reg[Ld]);
		}
	}

	/*****************************************************************************************
	 * Name:		MUL_Ld_Lm
	 * Tag:			MUL
	 * Description: Multiply
	 * Opcode: 		0100 0011 01 <Lm-3b> <Ld-3b>
	 * Disassembly: MUL Ld, Lm
	 * Action:		Ld = Ld * Lm 
	 * Effect CPSR: N = <Negative>, Z = <Zero> 
	 *****************************************************************************************/	
	protected void MUL_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "MUL R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] = reg[Ld] * reg[Lm];
			updateCPSR_N_Z(reg[Ld]);
		}
	}
	
	/*****************************************************************************************
	 * Name:		BIC_Ld_Lm
	 * Tag:			BIC
	 * Description: Logical bit clear (AND NOT) of two 32-bit values
	 * Opcode: 		0100 0011 10 <Lm-3b> <Ld-3b>
	 * Disassembly: BIC Ld, Lm
	 * Action:		Ld = Ld & ~Lm
	 * Effect CPSR: N = <Negative>, Z = <Zero> 
	 *****************************************************************************************/
	protected void BIC_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "BIC R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] = reg[Ld] & (~reg[Lm]);
			updateCPSR_N_Z(reg[Ld]);
		}
	}
	
	/*****************************************************************************************
	 * Name:		MVN_Ld_Lm
	 * Tag:			MVN
	 * Description: Move the logical not of a 32-bit value into a register
	 * Opcode: 		0100 0011 11 <Lm-3b> <Ld-3b>
	 * Disassembly: MVN Ld, Lm
	 * Action:		Ld = ~Lm 
	 * Effect CPSR: N = <Negative>, Z = <Zero> 
	 *****************************************************************************************/	   
	protected void MVN_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "MVN R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] = ~reg[Lm];
			updateCPSR_N_Z(reg[Ld]);
		}
	}

	/*****************************************************************************************
	 * Name:		CPY_Ld_Lm
	 * Tag:			CPY
	 * Description: Copy one ARM register to another without affecting the cpsr
	 * Opcode: 		0100 0110 00 <Lm-3b> <Ld-3b>
	 * Disassembly: CPY Ld, Lm
	 * Action:		Ld = Lm 
	 * Effect CPSR: preserved 
	 *****************************************************************************************/		 
	protected void CPY_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "CPY R" + Ld + ", R" + Lm;
		if(modeCPU)
			reg[Ld] = reg[Lm];
	}
	
	
	/*****************************************************************************************
	 * Name:		ADD_Ld_Hm
	 * Tag:			ADD
	 * Description: Add two 32-bit values 
	 * Opcode: 		0100 0100 01 <Hm-3b> <Ld-3b>
	 * Disassembly: ADD Ld, Hm
	 * Action:		Ld = Ld + Hm
	 * Effect CPSR: preserved
	 *****************************************************************************************/	
	protected void ADD_Ld_Hm() {
		int Ld =  (bits_3_0 & 0x7);
		int Hm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3) + 8;
		formattedInstruction = "ADD R" + Ld + ", R" + Hm;
		if(modeCPU) 
			reg[Ld] = reg[Ld] + reg[Hm];
	}

	/*****************************************************************************************
	 * Name:		MOV_Ld_Hm
	 * Tag:			MOV
	 * Description: Move a 32-bit value into a register
	 * Opcode: 		0100 0110 01 <Hm-3b> <Ld-3b>
	 * Disassembly: MOV Ld, Hm
	 * Action:		Ld = Hm
	 * Effect CPSR: preserved
	 *****************************************************************************************/	
	protected void MOV_Ld_Hm() {
		int Ld =  (bits_3_0 & 0x7);
		int Hm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3) + 8;
		formattedInstruction = "MOV R" + Ld + ", R" + Hm;
		if(modeCPU)
			reg[Ld] = reg[Hm];
	}

	/*****************************************************************************************
	 * Name:		ADD_Hd_Lm
	 * Tag:			ADD
	 * Description: Add two 32-bit values 
	 * Opcode: 		0100 0100 10 <Lm-3b> <Hd-3b>
	 * Disassembly: ADD Hd, Lm
	 * Action:		Hd = Hd + Lm
	 * Effect CPSR: preserved
	 *****************************************************************************************/	
	protected void ADD_Hd_Lm() {
		int Hd =  (bits_3_0 & 0x7) + 8;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "ADD R" + Hd + ", R" + Lm;
		if(modeCPU)
			reg[Hd] = reg[Hd] + reg[Lm];
	}

	/*****************************************************************************************
	 * Name:		MOV_Hd_Lm
	 * Tag:			MOV
	 * Description: Move a 32-bit value into a register
	 * Opcode: 		0100 0110 10 <Lm-3b> <Hd-3b>
	 * Disassembly: MOV Hd, Lm
	 * Action:		Hd = Lm
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void MOV_Hd_Lm() {
		int Hd =  (bits_3_0 & 0x7) + 8;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "MOV R" + Hd + ", R" + Lm;
		if(modeCPU)
			reg[Hd] = reg[Lm];
	}
	
	/*****************************************************************************************
	 * Name:		ADD_Hd_Hm
	 * Tag:			ADD
	 * Description: Add two 32-bit values 
	 * Opcode: 		0100 0100 11 <Hm-3b> <Hd-3b>
	 * Disassembly: ADD Hd, Hm
	 * Action:		Hd = Hd + Hm
	 * Effect CPSR: preserved
	 *****************************************************************************************/	
	protected void ADD_Hd_Hm() {
		int Hd =  (bits_3_0 & 0x7) + 8;
		int Hm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3) + 8;
		formattedInstruction = "ADD R" + Hd + ", R" + Hm;
		if(modeCPU)
			reg[Hd] = reg[Hd] + reg[Hm];
	}
	
	/*****************************************************************************************
	 * Name:		MOV_Hd_Hm
	 * Tag:			MOV
	 * Description: Move a 32-bit value into a register
	 * Opcode: 		0100 0110 11 <Hm-3b> <Hd-3b>
	 * Disassembly: MOV Hd, Hm
	 * Action:		Hd = Hm
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void MOV_Hd_Hm() {
		int Hd =  (bits_3_0 & 0x7) + 8;
		int Hm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3) + 8;
		formattedInstruction = "MOV R" + Hd + ", R" + Hm;
		if(modeCPU)
			reg[Hd] = reg[Hm];
	}
	
	/*****************************************************************************************
	 * Name:		CMP_Ln_Hm
	 * Tag:			CMP
	 * Description: Compare two 32-bit integers  
	 * Opcode: 		0100 0101 01 <Hm-3b> <Ln-3b>
	 * Disassembly: CMP Ln, Hm
	 * Action:		cpsr flags set on the result of (Ln - Hm) 
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <NoUnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/	
	protected void CMP_Ln_Hm() {
		int Ln =  (bits_3_0 & 0x7);
		int Hm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3) + 8;
		formattedInstruction = "CMP R" + Ln + ", R" + Hm;
		if(modeCPU)
			updateCPSR_N_Z(reg[Ln] - reg[Hm]);
	}
	
	/*****************************************************************************************
	 * Name:		CMP_Hn_Lm
	 * Tag:			CMP
	 * Description: Compare two 32-bit integers  
	 * Opcode: 		0100 0101 10 <Lm-3b> <Hn-3b>
	 * Disassembly: CMP Hn, Lm
	 * Action:		cpsr flags set on the result of (Hn - Lm) 
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <NoUnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/
	protected void CMP_Hn_Lm() {
		int Hn =  (bits_3_0 & 0x7) + 8;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "CMP R" + Hn + ", R" + Lm;
		if(modeCPU)
			updateCPSR_N_Z(reg[Hn] - reg[Lm]);
	}
	
	/*****************************************************************************************
	 * Name:		CMP_Hn_Hm
	 * Tag:			CMP
	 * Description: Compare two 32-bit integers  
	 * Opcode: 		0100 0101 01 <Hm-3b> <Hn-3b>
	 * Disassembly: CMP Hn, Hm
	 * Action:		cpsr flags set on the result of (Hn - Hm) 
	 * Effect CPSR: N = <Negative>, Z = <Zero>, C = <NoUnsignedOverflow>, V = <SignedOverflow>
	 *****************************************************************************************/
	protected void CMP_Hn_Hm() {
		int Hn =  (bits_3_0 & 0x7) + 8;
		int Hm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3) + 8;
		formattedInstruction = "CMP R" + Hn + ", R" + Hm;
		if(modeCPU)
			updateCPSR_N_Z(reg[Hn] - reg[Hm]);
	}

	/*****************************************************************************************
	 * Name:		BX_Rm
	 * Tag:			BX
	 * Description: Branch with exchange (branch with possible state switch)  
	 * Opcode: 		0100 0111 0 <Rm-4b> 000
	 * Disassembly: BX Rm
	 * Action:		pc = Rm & 0xfffffffe;  
	 * Effect CPSR: T = Rm & 1
	 *****************************************************************************************/	
	protected void BX_Rm() {
		int Rm =  ((bits_7_4 & 0x7) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "BL R" + Rm;
		if(modeCPU) {
			int address = reg[Rm] & 0xfffffffe;
			updatePc(address);
			regCPSR = (Rm & 0x1) << 5;
		}
	}

	/*****************************************************************************************
	 * Name:		BLX_Rm
	 * Tag:			BLX
	 * Description: Branch with link and exchange (subroutine call with possible state switch)  
	 * Opcode: 		0100 0111 1 <Rm-4b> 000
	 * Disassembly: BLX Rm
	 * Action:		lr = ret+1; pc = Rm & 0xfffffffe 
	 * Effect CPSR: T = Rm & 1
	 *****************************************************************************************/		
	protected void BLX_Rm() {
		int Rm =  ((bits_7_4 & 0x7) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "BLX R" + Rm;
		if(modeCPU) {
			reg[14] = reg[15] + 2;
			int address = reg[Rm] & 0xfffffffe;
			updatePc(address);
			regCPSR = (Rm & 0x1) << 5; // BIT 5 -> T
		}
	}
	
	/*****************************************************************************************
	 * Name:		LDR_Ld_immed8
	 * Tag:			LDR
	 * Description: Load a single value from a virtual address in memory  
	 * Opcode: 		0100 1<Ld-3b> <imeed-8b>
	 * Disassembly: LDR Ld, [pc, #immed]
	 * Action:		Ld = (int*)(Ln + immed5*4) 
	 * Effect CPSR: 
	 *****************************************************************************************/
	protected void LDR_Ld_immed8() {
		int Ld =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + (bits_3_0);
		formattedInstruction = "LDR R" + Ld + ", [pc, #" + immed8 + "]";
		if(modeCPU)
			reg[Ld] = loadMemory(reg[15] + immed8*4, dataType.Int, signType.unsigned);
	}

	/*****************************************************************************************
	 * Name:		STR_Ld_Ln_Lm
	 * Tag:			STR
	 * Description: Store a single value to a virtual address in memory  
	 * Opcode: 		0101 000 <Lm-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: STR Ld, [Ln, Lm]
	 * Action:		*(int*)(Ln + Lm) = Ld 
	 * Effect CPSR: 
	 *****************************************************************************************/	
	protected void STR_Ld_Ln_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int Lm = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "STR R" + Ld + ", [R" + Ln + ", R" + Lm + "]";
		if(modeCPU)
			storeMemory(reg[Ln] + reg[Lm], reg[Ld], dataType.Int);
	}

	/*****************************************************************************************
	 * Name:		STRH_Ld_Ln_Lm
	 * Tag:			STRH
	 * Description: Store a single value to a virtual address in memory  
	 * Opcode: 		0101 001 <Lm-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: STRH Ld, [Ln, Lm]
	 * Action:		*(short*)(Ln + Lm) = Ld 
	 * Effect CPSR: 
	 *****************************************************************************************/	 
	protected void STRH_Ld_Ln_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int Lm = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "STRH R" + Ld + ", R" + Ln + ", R" + Lm + "]";
		if(modeCPU)	
			storeMemory(reg[Ln] + reg[Lm], reg[Ld], dataType.Short);
	}

	/*****************************************************************************************
	 * Name:		STRB_Ld_Ln_Lm
	 * Tag:			STRB
	 * Description: Store a single value to a virtual address in memory  
	 * Opcode: 		0101 010 <Lm-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: STRB Ld, [Ln, Lm]
	 * Action:		*(byte*)(Ln + Lm) = Ld 
	 * Effect CPSR: 
	 *****************************************************************************************/
	protected void STRB_Ld_Ln_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int Lm = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "STRB R" + Ld + ", R" + Ln + ", R" + Lm + "]";
		if(modeCPU)
			storeMemory(reg[Ln] + reg[Lm], reg[Ld], dataType.Byte);
	}
	
	/*****************************************************************************************
	 * Name:		LDRSB_Ld_Ln_Lm
	 * Tag:			LDRSB
	 * Description: Load a single value from a virtual address in memory  
	 * Opcode: 		0101 011 <Lm-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: LDRSB Ld, [Ln, Lm]
	 * Action:		Ld = (sign-extend)memory((Ln + Lm), 1)
	 * Effect CPSR: 
	 *****************************************************************************************/
	protected void LDRSB_Ld_Ln_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int Lm = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LDRSB R" + Ld + ", [R" + Ln + ", R" + Lm + "]";
		if(modeCPU)
			reg[Ld] = loadMemory(reg[Ln] + reg[Lm], dataType.Byte, signType.signed);
	}

	/*****************************************************************************************
	 * Name:		LDR_Ld_Ln_Lm
	 * Tag:			LDR
	 * Description: Load a single value from a virtual address in memory  
	 * Opcode: 		0101 100 <Lm-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: LDR Ld, [Ln, Lm]
	 * Action:		Ld = memory((Ln + Lm), 4)
	 * Effect CPSR: 
	 *****************************************************************************************/ 
	protected void LDR_Ld_Ln_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int Lm = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LDR R" + Ld + ", [R" + Ln + ", R" + Lm + "]";
		if(modeCPU)
			reg[Ld] = loadMemory(reg[Ln] + reg[Lm], dataType.Int, signType.unsigned); 
	}

	/*****************************************************************************************
	 * Name:		LDRH_Ld_Ln_Lm
	 * Tag:			LDRH
	 * Description: Load a single value from a virtual address in memory  
	 * Opcode: 		0101 101 <Lm-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: LDRH Ld, [Ln, Lm]
	 * Action:		Ld = (zero-extend)memory((Ln + Lm), 2)
	 * Effect CPSR: 
	 *****************************************************************************************/ 
	protected void LDRH_Ld_Ln_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int Lm = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LDRH R" + Ld + ", [R" + Ln + ", R" + Lm + "]";
		if(modeCPU)
			reg[Ld] = loadMemory(reg[Ln] + reg[Lm], dataType.Short, signType.unsigned);
	}

	/*****************************************************************************************
	 * Name:		LDRB_Ld_Ln_Lm
	 * Tag:			LDRB
	 * Description: Load a single value from a virtual address in memory  
	 * Opcode: 		0101 110 <Lm-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: LDRB Ld, [Ln, Lm]
	 * Action:		Ld = (zero-extend)memory((Ln + Lm), 1)
	 * Effect CPSR: 
	 *****************************************************************************************/
	protected void LDRB_Ld_Ln_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int Lm = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LDRB R" + Ld + ", [R" + Ln + ", R" + Lm + "]";
		if(modeCPU)
			reg[Ld] = loadMemory(reg[Ln] + reg[Lm], dataType.Byte, signType.unsigned);
	}
	
	/*****************************************************************************************
	 * Name:		LDRSH_Ld_Ln_Lm
	 * Tag:			LDRSH
	 * Description: Load a single value from a virtual address in memory  
	 * Opcode: 		0101 111 <Lm-3b> <Ln-3b> <Ld-3b>
	 * Disassembly: LDRSH Ld, [Ln, Lm]
	 * Action:		Ld = (sign-extend)memory((Ln + Lm), 2)
	 * Effect CPSR: 
	 *****************************************************************************************/
	protected void LDRSH_Ld_Ln_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int Lm = ((bits_11_8 & 0x1) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LDRSH R" + Ld + ", [R" + Ln + ", R" + Lm + "]";
		if(modeCPU)
			reg[Ld] = loadMemory(reg[Ln] + reg[Lm], dataType.Short, signType.signed);
	}
	
	/*****************************************************************************************
	 * Name:		STR_Ld_Ln_immed5
	 * Tag:			STR
	 * Description: Store a single value to a virtual address in memory  
	 * Opcode: 		0110 0 <immed-5b> <Ln-3b> <Ld-3b>
	 * Disassembly: STR Ld, [Ln, #immed]
	 * Action:		memory((Ln + Lm), 4) = (int)Ld
	 * Effect CPSR: 
	 *****************************************************************************************/ 
	protected void STR_Ld_Ln_immed5() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed5 = ((bits_11_8 & 0x7) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LSL R" + Ld + ", [R" + Ln + ", #" + immed5 + "]";
		if(modeCPU)
			storeMemory(reg[Ln] + immed5, reg[Ld], dataType.Int);
	}
	
	/*****************************************************************************************
	 * Name:		LDR_Ld_Ln_immed5
	 * Tag:			LDR
	 * Description: Load a single value from a virtual address in memory  
	 * Opcode: 		0110 1 <immed-5b> <Ln-3b> <Ld-3b>
	 * Disassembly: LDR Ld, [Ln, #immed]
	 * Action:		Ld = memory((Ln + Lm), 4)
	 * Effect CPSR: 
	 *****************************************************************************************/ 
	protected void LDR_Ld_Ln_immed5() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed5 = ((bits_11_8 & 0x7) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LSL R" + Ld + ", [R" + Ln + ", #" + immed5 + "]";
		if(modeCPU)
			reg[Ld] = loadMemory(reg[Ln] + immed5, dataType.Int, signType.unsigned);
	}
	
	/*****************************************************************************************
	 * Name:		STRB_Ld_Ln_immed5
	 * Tag:			STRB
	 * Description: Store a single value to a virtual address in memory  
	 * Opcode: 		0111 0 <immed-5b> <Ln-3b> <Ld-3b>
	 * Disassembly: STRB Ld, [Ln, #immed]
	 * Action:		memory((Ln + Lm), 1) = (char)Ld
	 * Effect CPSR: 
	 *****************************************************************************************/  
	protected void STRB_Ld_Ln_immed5() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed5 = ((bits_11_8 & 0x7) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LSL R" + Ld + ", [R" + Ln + ", #" + immed5 + "]";
		if(modeCPU)
			storeMemory(reg[Ln] + immed5, reg[Ld], dataType.Byte);
	}

	/*****************************************************************************************
	 * Name:		LDRB_Ld_Ln_immed5
	 * Tag:			LDRB
	 * Description: Load a single value from a virtual address in memory  
	 * Opcode: 		0111 1 <immed-5b> <Ln-3b> <Ld-3b>
	 * Disassembly: LDRB Ld, [Ln, #immed]
	 * Action:		Ld = (zero-extend)memory((Ln + Lm), 1)
	 * Effect CPSR: 
	 *****************************************************************************************/ 
	protected void LDRB_Ld_Ln_immed5() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed5 = ((bits_11_8 & 0x7) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LSL R" + Ld + ", [R" + Lm + ", #" + immed5 + "]";
		if(modeCPU)
			reg[Ld] = loadMemory(reg[Lm] + immed5, dataType.Byte, signType.unsigned);
	}
	
	/*****************************************************************************************
	 * Name:		STRH_Ld_Ln_immed5
	 * Tag:			STRH
	 * Description: Store a single value to a virtual address in memory  
	 * Opcode: 		1000 0 <immed-5b> <Ln-3b> <Ld-3b>
	 * Disassembly: STRH Ld, [Ln, #immed]
	 * Action:		memory((Ln + Lm), 2) = (short)Ld
	 * Effect CPSR: 
	 *****************************************************************************************/
	protected void STRH_Ld_Ln_immed5() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed5 = ((bits_11_8 & 0x7) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LSL R" + Ld + ", [R" + Ln + ", #" + immed5 + "]";
		if(modeCPU)
			storeMemory(reg[Ln] + immed5, reg[Ld], dataType.Short);
	}

	/*****************************************************************************************
	 * Name:		LDRH_Ld_Ln_immed5
	 * Tag:			LDRH
	 * Description: Load a single value from a virtual address in memory  
	 * Opcode: 		1000 1 <immed-5b> <Ln-3b> <Ld-3b>
	 * Disassembly: LDRH Ld, [Ln, #immed]
	 * Action:		Ld = (zero-extend)memory((Ln + Lm), 2)
	 * Effect CPSR: 
	 *****************************************************************************************/  
	protected void LDRH_Ld_Ln_immed5() {
		int Ld =  bits_3_0 & 0x7;
		int Ln = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		int immed5 = ((bits_11_8 & 0x7) << 2) + ((bits_7_4 & 0xC) >>> 2);
		formattedInstruction = "LSL R" + Ld + ", [R" + Ln + ", #" + immed5 + "]";
		if(modeCPU)
			reg[Ld] = loadMemory(reg[Ln] + immed5, dataType.Short, signType.unsigned);
	}	
	
	/*****************************************************************************************
	 * Name:		STR_Ld_Sp_immed8
	 * Tag:			STR
	 * Description: Store a single value to a virtual address in memory  
	 * Opcode: 		1001 0 <Ld-3b> <immed-8b>
	 * Disassembly: STR Ld, [Sp, #immed]
	 * Action:		memory((Sp + immed), 2) = (int)Ld
	 * Effect CPSR: 
	 *****************************************************************************************/
	protected void STR_Ld_Sp_immed8() {
		int Ld =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + (bits_3_0);
		formattedInstruction = "MOV R" + Ld + ", [SP, #" + immed8 + "]";
		if(modeCPU)
			storeMemory(reg[13] + immed8, reg[Ld], dataType.Int);
	}

	/*****************************************************************************************
	 * Name:		LDR_Ld_Sp_immed8
	 * Tag:			LDR
	 * Description: Load a single value from a virtual address in memory  
	 * Opcode: 		1001 1 <Ld-3b> <immed-8b>
	 * Disassembly: LDRH Ld, [Sp, #immed]
	 * Action:		Ld = memory((Sp + immed), 4)
	 * Effect CPSR: 
	 *****************************************************************************************/ 
	protected void LDR_Ld_Sp_immed8() {
		int Ld =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + (bits_3_0);
		formattedInstruction = "MOV R" + Ld + ", [SP, #" + immed8 + "]";
		if(modeCPU)
			reg[Ld] = loadMemory(reg[13] + immed8*4, dataType.Int, signType.unsigned);
	}	

	/*****************************************************************************************
	 * Name: 		ADD_Ld_pc_immed8
	 * Tag:			ADD
	 * Description: Add two 32-bit values 
	 * Opcode: 		1010 0 <Ld-3b> <immed-8b>
	 * Disassembly: ADD Ld, pc, #immed
	 * Action:		Ld = pc + immed
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void ADD_Ld_pc_immed8() {
		int Ld =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + (bits_3_0);
		formattedInstruction = "MOV R" + Ld + ", SP, #" + immed8;
		if(modeCPU)
			reg[Ld] = reg[15] + immed8;
	}
	
	/*****************************************************************************************
	 * Name: 		ADD_Ld_sp_immed8
	 * Tag:			ADD
	 * Description: Add two 32-bit values 
	 * Opcode: 		1010 1 <Ld-3b> <immed-8b>
	 * Disassembly: ADD Ld, sp, #immed
	 * Action:		Ld = Pc + immed
	 * Effect CPSR: preserved
	 *****************************************************************************************/	
	protected void ADD_Ld_sp_immed8() {
		int Ld =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + (bits_3_0);
		formattedInstruction = "MOV R" + Ld + ", SP, #" + immed8;
		if(modeCPU)
			reg[Ld] = reg[13] + immed8;
	}

	/*****************************************************************************************
	 * Name: 		ADD_sp_immed7
	 * Tag:			ADD
	 * Description: Add two 32-bit values 
	 * Opcode: 		1011 0000 0 <immed-7b>
	 * Disassembly: ADD Sp, #immed
	 * Action:		Sp = Sp + immed
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void ADD_sp_immed7() {
		int immed7 = ((bits_7_4 & 0x7) << 4) + bits_3_0;
		formattedInstruction = "ADD SP, #" + immed7;
		if(modeCPU)
			reg[13] = reg[13] + immed7;
	}

	/*****************************************************************************************
	 * Name: 		SUB_sp_immed7
	 * Tag:			SUB
	 * Description: Subtract two 32-bit values 
	 * Opcode: 		1011 0000 1 <immed-7b>
	 * Disassembly: SUB Sp, #immed
	 * Action:		Sp = Sp - immed
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void SUB_sp_immed7() {
		int immed7 = ((bits_7_4 & 0x7) << 4) + bits_3_0;
		formattedInstruction = "SUB SP, #" + immed7;
		if(modeCPU)
			reg[13] = reg[13] - immed7;
	}
	
	/*****************************************************************************************
	 * Name: 		SXTH_Ld_Lm
	 * Tag:			SXTH
	 * Description: Halfword extract
	 * Opcode: 		1011 0010 00 <Lm-3b> <Ld-3b>
	 * Disassembly: SXTH Ld, Lm
	 * Action:		Ld = (sign-extend)(Lm[15:00])
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void SXTH_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "SXTH R" + Ld + ", R" + Lm;
		if(modeCPU) {
		reg[Ld] = (reg[Lm] & 0xFFFF);
			if(reg[Lm] < 0)	// Se reg[Lm] for negativo o sinal eh extendido
				reg[Ld] |= 0xFFFF0000;
		}
	}	
	
	/*****************************************************************************************
	 * Name: 		SXTB_Ld_Lm
	 * Tag:			SXTB
	 * Description: Byte extract
	 * Opcode: 		1011 0010 01 <Lm-3b> <Ld-3b>
	 * Disassembly: SXTB Ld, Lm
	 * Action:		Ld = (sign-extend)(Lm[07:00])
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void SXTB_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "SXTB R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] = (reg[Lm] & 0xFF);
			if(reg[Lm] < 0)	// Se reg[Lm] for negativo o sinal eh extendido
				reg[Ld] |= 0xFFFFFF00;
		}
	}
	
	/*****************************************************************************************
	 * Name: 		UXTH_Ld_Lm
	 * Tag:			UXTH
	 * Description: Halfword extract
	 * Opcode: 		1011 0010 10 <Lm-3b> <Ld-3b>
	 * Disassembly: UXTH Ld, Lm
	 * Action:		Ld = (zero-extend)(Lm[15:00])
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void UXTH_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "UXTH R" + Ld + ", R" + Lm;
		if(modeCPU)
			reg[Ld] = reg[Lm] & 0xFFFF;
	}	
	
	/*****************************************************************************************
	 * Name: 		UXTB_Ld_Lm
	 * Tag:			UXTB
	 * Description: Byte extract
	 * Opcode: 		1011 0010 11 <Lm-3b> <Ld-3b>
	 * Disassembly: UXTB Ld, Lm
	 * Action:		Ld = (zero-extend)(Lm[07:00])
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void UXTB_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "UXTB R" + Ld + ", R" + Lm;
		if(modeCPU)
			reg[Ld] = reg[Lm] & 0xFF;
	}
	
	/*****************************************************************************************
	 * Name: 		REV_Ld_Lm
	 * Tag:			REV
	 * Description: Reverse bytes within a word
	 * Opcode: 		1011 1010 00 <Lm-3b> <Ld-3b>
	 * Disassembly: REV Ld, Lm
	 * Action:		Ld[31:24] = Lm[07:00]; Ld[23:16] = Lm[15:08]; 
	 * 				Ld[15:08] = Lm[23:16]; Ld[07:00] = Lm[31:24]
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void REV_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "REV R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] =  (reg[Lm] <<  24) & 0xFF000000; 	// Ld[31:24] = Lm[07:00]
			reg[Ld] |= (reg[Lm] <<   8) & 0x00FF0000;	// Ld[23:16] = Lm[15:08]
			reg[Ld] |= (reg[Lm] >>>  8) & 0x0000FF00;	// Ld[15:08] = Lm[23:16]
			reg[Ld] |= (reg[Lm] >>> 24) & 0x000000FF;	// Ld[07:00] = Lm[31:24]
		}
	}

	/*****************************************************************************************
	 * Name: 		REV16_Ld_Lm
	 * Tag:			REV16
	 * Description: Reverse bytes within a halfword
	 * Opcode: 		1011 1010 01 <Lm-3b> <Ld-3b>
	 * Disassembly: REV16 Ld, Lm
	 * Action:		Ld[31:24] = Lm[23:16]; Ld[23:16] = Lm[31:24]; 
	 * 				Ld[15:08] = Lm[07:00]; Ld[07:00] = Lm[15:08]
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void REV16_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "REV16 R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] =  (reg[Lm] <<  8) & 0xFF000000; 	// Ld[31:24] = Lm[23:16]
			reg[Ld] |= (reg[Lm] >>> 8) & 0x00FF0000;	// Ld[23:16] = Lm[31:24]
			reg[Ld] |= (reg[Lm] <<  8) & 0x0000FF00;	// Ld[15:08] = Lm[07:00]
			reg[Ld] |= (reg[Lm] >>> 8) & 0x000000FF;	// Ld[07:00] = Lm[15:08]
		}
	}
	
	/*****************************************************************************************
	 * Name: 		REVSH_Ld_Lm
	 * Tag:			REVSH
	 * Description: Reverse bytes within a halfword
	 * Opcode: 		1011 1010 11 <Lm-3b> <Ld-3b>
	 * Disassembly: REVSH Ld, Lm
	 * Action:		Ld[31:08] = sign-extend(Lm[07:00]); Ld[07:00] = Lm[15:08]
	 * Effect CPSR: preserved
	 *****************************************************************************************/	
	protected void REVSH_Ld_Lm() {
		int Ld =  bits_3_0 & 0x7;
		int Lm = ((bits_7_4 & 0x3) << 1) + (bits_3_0 >>> 3);
		formattedInstruction = "REVSH R" + Ld + ", R" + Lm;
		if(modeCPU) {
			reg[Ld] =  (reg[Lm] <<  8) & 0x0000FF00;	// Ld[15:08] = (Lm[07:00])
			reg[Ld] |= (reg[Lm] >>> 8) & 0x000000FF;	// Ld[07:00] = Lm[15:08]
			if(reg[Lm] < 0)								// Se reg[Lm] eh negativo, sign-extend 
				reg[Ld] |= 0xFFFF0000;
		}
	}
	
	/*****************************************************************************************
	 * Name: 		PUSH_R_register_list
	 * Tag:			PUSH
	 * Description: Pushes multiple registers to the stack in Thumb state
	 * Opcode: 		1011 010 <R> <register_list-8b>
	 * Disassembly: PUSH {register_list, R}
	 * Action:		
	 * Effect CPSR: preserved
	 *****************************************************************************************/	
	protected void PUSH_R_register_list() {
		byte immed8 = (byte) ((bits_7_4 << 4) + bits_3_0);
		boolean lr = (bits_11_8 & 0x1) == 1 ? true : false;
		formattedInstruction = "PUSH";
		boolean aux = true;		
		for(int i = 0; i < 8; i++) {
			if(aux) {
				if(((immed8 >>> i) & 0x1) == 1) {
					formattedInstruction += " {R" + i;
					aux = false;
				}
			}
			else {
				if(((immed8 >>> i) & 0x1) == 1)
					formattedInstruction += ", R" + i;
			}
		}
		formattedInstruction += lr ? ", LR}" : "}";
		if(modeCPU)
			pushStack(immed8, lr);
	}
	
	/*****************************************************************************************
	 * Name: 		POP_R_register_list
	 * Tag:			PUSH
	 * Description: Pops multiple registers to the stack in Thumb state
	 * Opcode: 		1011 110 <R> <register_list-8b>
	 * Disassembly: PUSH {register_list, R}
	 * Action:		
	 * Effect CPSR: preserved
	 *****************************************************************************************/	
	protected void POP_R_register_list() {
		byte immed8 = (byte) ((bits_7_4 << 4) + bits_3_0);
		boolean pc = (bits_11_8 & 0x1) == 1 ? true : false;
		formattedInstruction = "POP";
		boolean aux = true;		
		for(int i = 0; i < 8; i++) {
			if(aux) {
				if(((immed8 >>> i) & 0x1) == 1) {
					formattedInstruction += " {R" + i;
					aux = false;
				}
			} 
			else {
				if(((immed8 >>> i) & 0x1) == 1)
					formattedInstruction += ", R" + i;
			}
		}
		formattedInstruction += pc ? ", PC}" : "}";
		if(modeCPU)
			popStack(immed8, pc);
	}
	
	/*****************************************************************************************
	 * Name: 		SETEND_LE
	 * Tag:			SETEND LE
	 * Description: Set the endianness for data accesses
	 * Opcode: 		1011 0110 0101 0000
	 * Disassembly: SETEND LE
	 * Action:		In the cpsr E=0 so data accesses will be little-endian
	 * Effect CPSR: E = 0
	 *****************************************************************************************/	
	protected void SETEND_LE() {
		formattedInstruction = "SETEND LE";
		if(modeCPU)        
			regCPSR &= ~(1 << 9); // BIT 9 -> E
	}

	/*****************************************************************************************
	 * Name: 		SETEND_BE
	 * Tag:			SETEND BE
	 * Description: Set the endianness for data accesses
	 * Opcode: 		1011 0110 0101 1000
	 * Disassembly: SETEND BE
	 * Action:		In the cpsr E=1 so data accesses will be big-endian 
	 * Effect CPSR: E = 1
	 *****************************************************************************************/
	protected void SETEND_BE() {
		formattedInstruction = "SETEND BE";
		if(modeCPU)  
			regCPSR |= 1 << 9; // BIT 9 -> E
	}

	/*****************************************************************************************
	 * Name: 		CPSIE
	 * Tag:			CPSIE
	 * Description: Change processor state; modifies selected bits in the cpsr
	 * Opcode: 		1011 0110 0110 0 <a> <i> <f>
	 * Disassembly: CPSIE <flags>	
	 * Action:		cpsr = cpsr & ~mask  
	 * Effect CPSR: cpsr = cpsr & ~mask 
	 *****************************************************************************************/
	protected void CPSIE() {
		int mask = bits_3_0 & 0x7;
		formattedInstruction = "CPSIE";
		boolean aux = true;
		for(int i = 0; i < 3; i++) {
			if(aux) {
				if(((mask >>> i) & 0x1) == 1 && i == 0)
					formattedInstruction += " f";
				else if(((mask >>> i) & 0x1) == 1 && i == 1)
					formattedInstruction += " i"; 
				else if(((mask >>> i) & 0x1) == 1 && i == 2)
					formattedInstruction += " a"; 
			} else {
				if(((mask >>> i) & 0x1) == 1 && i == 1)
					formattedInstruction += ", i"; 
				else if(((mask >>> i) & 0x1) == 1 && i == 2)
					formattedInstruction += ", a"; 
			}
		}
		if(modeCPU)
			regCPSR &= ~(mask << 6); // BIT 8 7 6 -> a, i, f
	}

	/*****************************************************************************************
	 * Name: 		CPSID
	 * Tag:			CPSID
	 * Description: Change processor state; modifies selected bits in the cpsr
	 * Opcode: 		1011 0110 0111 0 <a> <i> <f>
	 * Disassembly: CPSID <flags>	
	 * Action:		cpsr = cpsr | mask  
	 * Effect CPSR: cpsr = cpsr | mask 
	 *****************************************************************************************/
	protected void CPSID() {
		int mask = bits_3_0 & 0x7;
		formattedInstruction = "CPSID";
		boolean aux = true;
		for(int i = 0; i < 3; i++) {
			if(aux) {
				if(((mask >>> i) & 0x1) == 1 && i == 0)
					formattedInstruction += " f";
				else if(((mask >>> i) & 0x1) == 1 && i == 1)
					formattedInstruction += " i"; 
				else if(((mask >>> i) & 0x1) == 1 && i == 2)
					formattedInstruction += " a"; 
			} else {
				if(((mask >>> i) & 0x1) == 1 && i == 1)
					formattedInstruction += ", i"; 
				else if(((mask >>> i) & 0x1) == 1 && i == 2)
					formattedInstruction += ", a"; 
			}
		}
		if(modeCPU)		
			regCPSR |= mask << 6; // BIT 8 7 6 -> a, i, f 
	}
	
	/*****************************************************************************************
	 * Name: 		BKPT_immed8
	 * Tag:			BKPT
	 * Description: Breakpoint instruction
	 * Opcode: 		1011 1110 <immed-8b>
	 * Disassembly: BKPT #immed	
	 * Action:		cpsr = cpsr | mask  
	 * Effect CPSR: cpsr = cpsr | mask 
	 *****************************************************************************************/
	protected void BKPT_immed8() {
		int immed8 = (bits_7_4 << 4) + (bits_3_0);
		formattedInstruction = "BKPT #" + immed8;
	}
	
	/*****************************************************************************************
	 * Name: 		STMIA_Ln_register_list
	 * Tag:			STMIA
	 * Description: Store multiple 32-bit registers to memory
	 * Opcode: 		1100 0 <Ln-3b> <register_list-8b>
	 * Disassembly: STMIA Ln!, <register_list>	
	 * Action:		Store multiple 32-bit registers to memory
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void STMIA() {
		int Ln =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + bits_3_0;
		formattedInstruction = "STMIA R" + Ln + "!";
		boolean aux = true;
		int address = reg[Ln];
		for(int i = 0; i < 8; i++) {
			if(aux) {
				if(((immed8 >>> i) & 0x1) == 1) {
					formattedInstruction += ", {R" + i;
					if(modeCPU)
						storeMemory(address, reg[i], dataType.Int);
					address += 4;
					aux = false;
				}
			}
			else {
				if(((immed8 >>> i) & 0x1) == 1) {
					formattedInstruction += ", R" + i;
					if(modeCPU)
						storeMemory(address, reg[i], dataType.Int);
					address += 4;
				}
			}
		}
		formattedInstruction += "}";
	}
	
	/*****************************************************************************************
	 * Name: 		LDMIA_Ln_register_list
	 * Tag:			LDMIA
	 * Description: Load multiple 32-bit words from memory to ARM registers
	 * Opcode: 		1100 1 <Ln-3b> <register_list-8b>
	 * Disassembly: LDMIA Ln!, <register_list>	
	 * Action:		Load multiple 32-bit words from memory to ARM registers  
	 * Effect CPSR: preserved
	 *****************************************************************************************/ 
	protected void LDMIA() {
		int Ln =  bits_11_8 & 0x7;
		int immed8 = (bits_7_4 << 4) + bits_3_0;
		formattedInstruction = "LDMIA R" + Ln + "!";
		boolean aux = true;	
		int address = reg[Ln];
		for(int i = 0; i < 8; i++) {
			if(aux) {
				if(((immed8 >>> i) & 0x1) == 1) {
					formattedInstruction += ", {R" + i;
					if(modeCPU)
						reg[i] = loadMemory(address, dataType.Int, signType.unsigned);
					address += 4;
					aux = false;
				}
			}
			else {
				if(((immed8 >>> i) & 0x1) == 1) {
					formattedInstruction += ", R" + i;
					if(modeCPU)
						reg[i] = loadMemory(address, dataType.Int, signType.unsigned);
					address += 4;	
				}
			}
		}
		formattedInstruction += "}";
	}

	/*****************************************************************************************
	 * Name: 		B_cond_offset8
	 * Tag:			B<cond>
	 * Description: Branch relative
	 * Opcode: 		1101  <cond-4b> <signed offset-8b>
	 * Disassembly: B<cond> <address8>	
	 * Action:		Branches to the given address or label. 
	 * 				The address is stored as a relative offset  
	 * Effect CPSR: preserved
	 *****************************************************************************************/ 
	protected void B_cond_offset8() {
		int cond = bits_11_8;
		int offset = (bits_7_4 << 4) + bits_3_0;
		if((offset >>> 7) == 1) 
			offset |= 0xFFFFFF00;
		// instruction_address + 4 + offset * 2
		int address = contMemory + 4 + offset * 2;
		formattedInstruction = String.format("B%s #0x%x", decodeCond(cond), address);
		if(modeCPU) {
			address = reg[15] + 4 + offset * 2;
			if(checkCondition(cond)) 
				updatePc(address);
		}
	}
	
	/*****************************************************************************************
	 * Name: 		SWI_immed8
	 * Tag:			SWI
	 * Description: Software interrupt
	 * Opcode: 		1101 1111 <immed-8b>
	 * Disassembly: SWI #immed	
	 * Action:		The SWI instruction causes the ARM to enter supervisor mode and start 
	 * 				executing from the SWI vector. The return address and cpsr are saved 
	 * 				in lr_svc and spsr_svc, respectively. 
	 * Effect CPSR: preserved
	 *****************************************************************************************/ 
	protected void SWI() {
		int immed8 = (bits_7_4 << 4) + bits_3_0;
		formattedInstruction = "SWI #" + immed8;
	}

	/*****************************************************************************************
	 * Name: 		B_offset11
	 * Tag:			B
	 * Description: Branch relative
	 * Opcode: 		1110 0 <signed offset-11b>
	 * Disassembly: B <address11>	
	 * Action:		Branches to the given address or label. 
	 * 				The address is stored as a relative offset  
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void B_offset11() {
		int offset = ((bits_11_8 & 0x7) << 8) + (bits_7_4 << 4) + bits_3_0;
		if((offset >>> 10) == 1) 
			offset |= 0xFFFFF800;
		// instruction_address + 4 + offset * 2 -> 20 - 20 + 4 -> 4
		int address = contMemory + (offset * 2) + 4;
		formattedInstruction = "B #" + String.format("0x%x", address);
		bl = true;
		if(modeCPU) {
			address = reg[15] + (offset * 2) + 4;
			updatePc(address);
		}
	}
	
	/*****************************************************************************************
	 * Name: 		BLX_offset10
	 * Tag:			BLX
	 * Description: Branch with link and exchange (subroutine call with possible state switch)
	 * Opcode: 		1110 1 <unsigned offset-10b> 0
	 * Disassembly: BLX <address10>	
	 * Action:		lr = ret+1; pc = <address10> 
	 * Effect CPSR: T = 0 (switch to ARM state)
	 *****************************************************************************************/
	protected void BLX_offset10() {
		int offset = ((bits_11_8 & 0x7) << 7) + (bits_7_4 << 3) + (bits_3_0 >>> 1);
		int address =  offset * 4 + 4 + (0 << 12) & ~3;
		formattedInstruction = "BLX #" + String.format("0x%x", address);
		bl = false;
		if(modeCPU) {
			reg[14] = reg[15] + 2;	// lr = ret+1
			updatePc(address);			// pc = <address10>
			regCPSR &= ~(1 << 5);	//T = 0 (switch to ARM state)
		}
	}
	
	/*****************************************************************************************
	 * Name: 		BLorBLX
	 * Tag:			BL or BLX
	 * Description: Branch with link and exchange (subroutine call with possible state switch)
	 * Opcode: 		1111 0 <unsigned offset-10b> 0
	 * Disassembly: BLX <address10>	
	 * Action:		Branches to the given address or label. 
	 * 				The address is stored as a relative offset
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void BLorBLX() {
		int offset = ((bits_11_8 & 0x7) << 8) + (bits_7_4 << 4) + bits_3_0;
		int address = offset * 2 + 4;
		formattedInstruction = bl ? "BL #" : "BLX #";
		formattedInstruction += String.format("0x%x", address);
		if(modeCPU) {
			reg[14] = reg[15] + 2;
			updatePc(address);
			if(!bl)
				regCPSR &= ~(1 << 5); // T=0 (switch to ARM state)
		}
	}

	/*****************************************************************************************
	 * Name: 		BL_offset11
	 * Tag:			BL
	 * Description: Branch with link and exchange (subroutine call with possible state switch)
	 * Opcode: 		1111 1 <unsigned offset-10b> 0
	 * Disassembly: BL <address22>	
	 * Action:		lr = ret+1; pc = <address22>
	 * Effect CPSR: preserved
	 *****************************************************************************************/
	protected void BL_offset11() {
		int offset = ((bits_11_8 & 0x7) << 8) + (bits_7_4 << 4) + bits_3_0;
		int address = offset * 2 + 4;
		formattedInstruction = "BL #" + String.format("0x%x", address);
		bl = true;
		if(modeCPU) {
			reg[14] = reg[15] + 2;
			updatePc(address);
		}
	}
	
	/*****************************************************************************************
	 * Name: 		UNDEFINED
	 * Tag:			UNDEFINED
	 * Description: Undefined and expected to remain so
	 * Opcode: 		1101 1110 <x-8b>
	 *****************************************************************************************/	
	protected void UNDEFINED() {
		formattedInstruction = "UNDEFINED";
	}

}
