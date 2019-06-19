/* * Instructions.java * Copyright (C) 2019 victor <victor@TheShell> * * Distributed under terms of the MIT license. */


import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.regex.Pattern;


public class Parser {
	//=================vars=====================\\
	public static List<String> lines = new ArrayList<String>(); //this gives lines of instructions unFiltered
	public static Map<String,Integer> labelMap = new HashMap<>(); //gives map Label Name (key) => Address(value)
    public static Map<String,Integer> instructMap = new HashMap<>();
	//=================Function=====================\\

	public void filterCommentsWhites(String[] args) {

		Pattern noFrontComments = Pattern.compile("^[^#].*");
		Pattern noBlankLines = Pattern.compile("[^\\s][a-z].*$");

		//After passing these patterns its possible for therre to be # after an instruction
		try (Stream<String> stream = Files.lines(Paths.get(args[0]))) {
			//=============filter front of line comments and blank lines=====================\\
			lines = stream
					.filter(noFrontComments.asPredicate()) //get all lines that dont start with
					.filter(noBlankLines.asPredicate()) //get all lines that dont start with
					.collect(Collectors.toList());
			//=================================================================================\\
		} catch (IOException e) {
			e.getSuppressed();
		}

		//================Filters out the comments after instructions\ labels==========\\
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).contains("#")) {
				lines.set(i, lines.get(i).substring(0, lines.get(i).indexOf("#")));
			}
		}
		//====================================================\\


		//=========================Remove white spaces before in the string=========\\
		for (int i = 0; i < lines.size(); i++) {
				lines.set(i,lines.get(i).trim());
		}

		//System.out.println("checking items ");
		//========================================================================\\\
		//System.out.println("done Reading items ");
	}

	public List<String> getInst(List<String>line){

		List<String> filteredLines = new ArrayList<>();
//===========================Filter out  just instruction============\\
		for (int i = 0; i < line.size(); i++) {
			//Get rid of labells if on same line as instruction
			if (line.get(i).contains(":") || line.get(i).contains(": ") || line.get(i).contains(" :") ) {

				line.set(i, line.get(i).substring(line.get(i).indexOf(":"), line.get(i).length()));

				line.set(i,line.get(i).replaceAll("^:\\s?","").trim());

				//System.out.println("debugger = " + line.get(i));

			}
		}
		//Filter out white blank lines
		filteredLines = line.stream().filter(s->!s.trim().isEmpty()).collect(Collectors.toList());

	//	line.forEach(//System.out::println);

		//System.out.println("done bish");
		return filteredLines;
	}

	public Map<String,Integer> getInstrMap() {return instructMap; }
	public Map<String,Integer> getLabelMap() {return labelMap; }

	public void setMaps(String [] args, Integer BaseAddress) {
		//1. pass throught filtering out lines
		filterCommentsWhites(args);

		Pattern LabelFormat = Pattern.compile("^[0-9a-zA-Z]+\\s?:");
		//get filtered String that correcponds to labels put them in a list

		//2.Filter out labels
		List<String> labels = lines.stream()
				.filter(LabelFormat.asPredicate())
				.collect(Collectors.toList());


		//this is give a test to see if we can incrment the address (if label is on the same line)
  		  Pattern labelFollowedByInst = Pattern.compile("^[0-9a-zA-z]+\\s?:\\s*[0-9a-zA-z].*$");
  		  Pattern instrMatch = Pattern.compile("^[0-9a-zA-z]+\\s?[^:]+$");
		//===================get address of corrersponding address of labels===================\\
		int Address = BaseAddress;

		List<Integer> AddrListLabel = new ArrayList<Integer>();
		List<Integer> AddrListInstr = new ArrayList<>();
		//This only test is labels are on a new line after

		//=======================GETTING ADDR corrsponding to the line string===========================================\\
		for (int i = 0; i < lines.size(); i++) {

			//Label Followed instruction
			if (labelFollowedByInst.matcher(lines.get(i)).find())
			{
			    //System.out.println("labelFollowedByinstr = " + Address);
				if(i==0) {
					AddrListLabel.add(Address);
					AddrListInstr.add(Address);
					Address++;

				}
				else {
					AddrListLabel.add(Address);
					AddrListInstr.add(Address);
					Address++;
				}

			}
			//just instruction found
			else if(instrMatch.matcher(lines.get(i)).find()) {

				//System.out.println("just intruction found" + Address);

				AddrListInstr.add(Address);
				Address++;
			}

			else { //if it is a label store that address in it
				if(i == lines.size() -1) //if the last run is an incrutio
					Address++;

				AddrListLabel.add(Address);
					//System.out.println("just label found" + Address);
			}


			//System.out.println("address = " + Address + ", addrInstrc.size = " + AddrListInstr.size());
		}

		//AddrListInstr.forEach(System.out::println);
		//===========================Filter out inline instructions with labels============\\
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).contains("#")) {
				lines.set(i, lines.get(i).substring(0, lines.get(i).indexOf("#")));
			}

		}
		//=======================put label, and corresonding addr in map=================================\\
		for (int i = 0; i < labels.size(); i++) { //get key (label name ) -> value (address)//

		    //System.out.println( "currently FUcking here = " +labels.get(i).substring(0, labels.get(i).indexOf(":")));
			labels.set(i, labels.get(i).substring(0, labels.get(i).indexOf(":")));
			labelMap.put(labels.get(i), AddrListLabel.get(i));

		}
		//=======================put label, and corresonding addr in map=================================\\
		//4. filter out instruction
		//4.1 if opcode$r -> opcode $r
		lines = getInst(lines);

		//System.out.print("Ater lines sorted");

		//System.out.println("instruct.size= " + lines.size());

		//System.out.println("AddrList inst size= " + AddrListInstr.size());



		//System.out.println(lines.size());
		//lines.stream().forEach(s->//System.out.println(s));
			for (int i = 0; i < lines.size(); i++) { //get key (label name ) -> value (address)//
			instructMap.put(lines.get(i), AddrListInstr.get(i));
		}//3. filter out instruction




		//5. sort maps by address

	}

	//==================================get Field functions =======================================================\\
	//NEED TO FIX reading parsing up to next $ for regs ALSO PUT each of these function in the instruction Type class
// getOp can go in Instruction class because everyinstruction has a opcode
//When creating a new object of sub instruction pass line -> when passed parse it to each variable
//Format for Reg: op rd, rs, rt


//Returns a list of the fields in binary : corresponding to the type
	public static List<String> getFields(String inst, int type)
	{
		List<String> binaryFields = new ArrayList<>();
		List<String> nmeumonicFields = new ArrayList<>();

		//=============================REG TYPE=======================================================\\
		//Fields ={opcode,rd,rt,rs, shamt, funct}
		if (type == 0)
		{
		    //Case 1 - if jr instruct, rd = 0, rt =  0 , rs = reg jump to, shamt =: 0, funct
			if (getOp(inst).equals("jr"))
			{
				//Step 1.1 - get opcode
				nmeumonicFields.add(getOp(inst));
				//Step 1.2 - get Rs
				nmeumonicFields.add(getRd(inst)); //just beause rs is right char
				//Step 1.3 - get Rt
				nmeumonicFields.add("00000");
				//Step 1.4 - get rd
				nmeumonicFields.add("00000");
				//Step 1.5 - get shamt
				nmeumonicFields.add("00000");
				//Step 1.6 - getFunc is already known so therefor its just used to conver to binary

				//Step 2: translate nuemonic fields -> binary fields
				//Step 2.1 - map opcode nmeuonic -> binary version
				binaryFields.add(typeInstruction.opMap.get(nmeumonicFields.get(0)));
				//Step 2.2 - map rs nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(1)));
				//Step 2.3 - map rt nmeuonic -> binary version
				binaryFields.add(nmeumonicFields.get(2));
				//Step 2.4 - map rd nmeuonic -> binary version
				binaryFields.add(nmeumonicFields.get(3));

				//Step 2.2 - map shamt nmeuonic -> binary version
				binaryFields.add(nmeumonicFields.get(4));

				//Step 2.3 - map funct nmeuonic -> binary version
				binaryFields.add(getFunct(inst));

			}
			//Special parse case
			else if (getOp(inst).equals("sll"))
			{
					//Step 1.1 - get opcode
				nmeumonicFields.add(getOp(inst));
				//Step 1.2 - get Rs
				nmeumonicFields.add("00000"); //just beause rs is right char
				//Step 1.3 - get Rt using getRS because location in parse
				nmeumonicFields.add(getRs(inst));
				//Step 1.4 - get rd
				nmeumonicFields.add(getRd(inst));
				//Step 1.5 - get shamt
				nmeumonicFields.add(getShamt(inst));
				//Step 1.6 - getFunc is already known so therefor its just used to conver to binary

				//Step 2: translate nuemonic fields -> binary fields
				//Step 2.1 - map opcode nmeuonic -> binary version
				binaryFields.add(typeInstruction.opMap.get(nmeumonicFields.get(0)));
				//Step 2.2 - map rs nmeuonic -> binary version
				binaryFields.add(nmeumonicFields.get(1));
				//Step 2.3 - map rt nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(2)));
				//Step 2.4 - map rd nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(3)));

				//Step 2.2 - map shamt nmeuonic -> binary version
				binaryFields.add(nmeumonicFields.get(4));

				//Step 2.3 - map funct nmeuonic -> binary version
				binaryFields.add(getFunct(inst));

			}
			else {
				//step 1: parse into neumonic fields (getRs().....getShamt())
				//Step 1.1 - get opcode
				nmeumonicFields.add(getOp(inst));
				//Step 1.2 - get Rs
				nmeumonicFields.add(getRs(inst));
				//Step 1.3 - get Rt
				nmeumonicFields.add(getRt(inst));
				//Step 1.4 - get rd
				nmeumonicFields.add(getRd(inst));
				//System.out.println("currently " + getRd(inst));
				//Step 1.5 - get shamt
				nmeumonicFields.add(getShamt(inst));
				//Step 1.6 - getFunc is already known so therefor its just used to conver to binary

				//Step 2: translate nuemonic fields -> binary fields
				//Step 2.1 - map opcode nmeuonic -> binary version
				binaryFields.add(typeInstruction.opMap.get(nmeumonicFields.get(0)));
				//Step 2.2 - map rs nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(1)));
				//Step 2.3 - map rt nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(2)));
				//Step 2.4 - map rd nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(3)));

				//Step 2.2 - map shamt nmeuonic -> binary version
				binaryFields.add(nmeumonicFields.get(4));

				//Step 2.3 - map funct nmeuonic -> binary version
				binaryFields.add(getFunct(inst));

			}
			//step 3: return binary fields
			return binaryFields;
		}
	//=================================================ImmedInst===========================================\\
		//if type == 1 - ImmedInst
		if (type == 1)
		{
			//Format: opcode rt, rs, immed
			//Fields ={opcode,rs,rt,immed}
			///step 1: parse into neumonic fields (into binary)

			if (getOp(inst).equals("beq") || getOp(inst).equals("bne"))
			{
				//Parse string along
				nmeumonicFields.add(getOp(inst));
				//Step 1.2 - get Rs
				nmeumonicFields.add(getRs(inst));
				//Step 1.3 - get Rt
				nmeumonicFields.add(getRd(inst));
				//Step 1.4 - get offset address = label - current_address

				//System.out.println("label addr = " + Parser.labelMap.get(getLabel(inst)));
				//System.out.println("instr addr = " + Parser.instructMap.get(inst));

				nmeumonicFields.add(
						getBinaryRep( Parser.labelMap.get(getLabel(inst)) - (Parser.instructMap.get(inst)+1) , 16)
				);




				////System.out.println("instruction looking for = "+ getImmed(inst));

				//Step 2: translate nuemonic fields -> binary fields
				binaryFields.add(typeInstruction.opMap.get(nmeumonicFields.get(0)));
				//Step 2.2 - map rs nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(1)));
				//Step 2.3 - map rt nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(2)));
				//Step 2.4 - map immed -> binary vaersion
				binaryFields.add(nmeumonicFields.get(3));

			}
			//if lw or sw
			else if (getOp(inst).equals("lw") || getOp(inst).equals("sw"))
			{
					//Parse string along
				nmeumonicFields.add(getOp(inst));
				//Step 1.2 - get Rs
				nmeumonicFields.add(getRd(inst));
				//Step 1.3 - get Rt
				nmeumonicFields.add(getLwReg(inst));
				//Step 1.4 - get offset address = label - current_address

				//://System.out.println("parser  map = " + Parser.labelMap.get(getLabel(inst)));

				//16 -bit imm
				nmeumonicFields.add(getLwImmed(inst));

				//System.out.println("lwImmed = " + getLwImmed(inst));

				////System.out.println("instruction looking for = "+ getImmed(inst));

				//Step 2: translate nuemonic fields -> binary fields
				binaryFields.add(typeInstruction.opMap.get(nmeumonicFields.get(0)));
				//Step 2.2 - map rs nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(1)));
				//Step 2.3 - map rt nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(2)));
				//Step 2.4 - map immed -> binary vaersion
				binaryFields.add(getBinaryRep(Integer.parseInt(nmeumonicFields.get(3)),16));



			}
			else {
				//Parse string along
				nmeumonicFields.add(getOp(inst));
				//Step 1.2 - get Rs
				nmeumonicFields.add(getRs(inst));
				//Step 1.3 - get Rt
				nmeumonicFields.add(getRd(inst));
				//Step 1.4 - get immed
				nmeumonicFields.add(getImmed(inst));

				//System.out.println("instruction looking for = "+getImmed(inst));

				//Step 2: translate nuemonic fields -> binary fields
				binaryFields.add(typeInstruction.opMap.get(nmeumonicFields.get(0)));
				//Step 2.2 - map rs nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(1)));
				//Step 2.3 - map rt nmeuonic -> binary version
				binaryFields.add(Registers.regMap.get(nmeumonicFields.get(2)));
				//Step 2.4 - map immed -> binary vaersion
				binaryFields.add(nmeumonicFields.get(3));

			}
			return binaryFields;
		}
		//=======================================================================================================\\
		//if type == 2 - jump instru
		if (type == 2)
		{


		//		//System.out.println("parser  map = " + Parser.labelMap.get(getLabel(inst)));

				////System.out.println("instruction looking for = "+ getImmed(inst));

				//Step 2: translate nuemonic fields -> binary fields
				binaryFields.add(typeInstruction.opMap.get(getOp(inst)));
				//Step 2.2 - map rs nmeuonic -> binary version

			//System.out.println("binaryrep = " + getBinaryRep(labelMap.get(getTargetAddr(inst)),26));
			//System.out.println("binaryrep = " + getBinaryRep(labelMap.get(getTargetAddr(inst)),26));

				binaryFields.add(getBinaryRep(labelMap.get(getTargetAddr(inst)),26));

		}

		return binaryFields;
	}


	public static String getLwReg(String line)
	{
		String rs = (line.split(",")[1]); //get add from add$r1
		rs = rs.split("\\(")[1].split("\\)")[0].trim();  //remove blank space
		//System.out.println("here rn =" + rs );
		return  rs;

	}

	public static String getLwImmed(String line)
	{
		String lwImmed = (line.split(",")[1]); //get add from add$r1
		lwImmed = lwImmed.split("\\(")[0].trim();  //remove blank space
		//System.out.println("getLWiMmed = " + lwImmed);

		return  lwImmed;
	}
	public static String getOp(String line)
	{
		String op = (line.split("\\$")[0]); //get add from add$r1
        op = op.split("\\s")[0].trim();  //remove blank space
		//System.out.println("op code fucking currently = " + op);
		return op;
	}		//==============TEST1 - initalize maps (instruction and label map)================\\

	public static String getRd(String line)
	{
		String Rd = line.split(",")[0];

		//System.out.println("Rd= " +Rd);
		Rd = "$"+Rd.split("\\$")[1].trim();
		////System.out.println("Rd= " +Rd);
		return Rd;
	}


	public static String getRs(String line)
	{
		String Rs = line.split(",")[1];
		Rs = Rs.split(",")[0];
			Rs = Rs.trim();

		//System.out.println("Rs="+Rs);
		return Rs;

	}
	public static String getRt(String line) {
		String Rt = line.split(",")[2];
		Rt = Rt.trim();
		//System.out.println("Rt="+Rt);
		return Rt;
	}

	public static String getShamt(String line)
	{
		if(!getOp(line).equals("sll"))
			return "00000";

		String format = "%05d";
		String val = line.split(",")[2];
		val = val.trim();
		val  =  String.format(format,Integer.parseInt(decStringToBinary(val)));

		return val;
	}
	public static String getFunct(String line) {
		return typeInstruction.functMap.get(getOp(line));
	}

	public static String getLabel(String line)
	{
		String imm = line.split(",")[2].trim();
		//System.out.println("immediate now = " + imm);
		return imm;
	}

	public static String getImmed(String line)
	{
		String imm = line.split(",")[2].trim();
		String format = "%0" + 16 + "d";

		//if the immedate is a negative value
		if (0 > Integer.parseInt(imm))
		{
			imm = Integer.toBinaryString(Integer.parseInt(imm));

			imm = imm.substring(16,32);
			//System.out.println(" I am currently test neg immed = " + imm);
		}
		else{
			imm =  String.format(format,Integer.parseInt(decStringToBinary(imm)));
		}
		//System.out.print("binary = " + imm);

		return imm;
	}


	public static String getBinaryRep(Integer result, int numOfBits)
	{
		String format = "%0" + numOfBits + "d";
		String offSet;


		if(result<0) {
			//System.out.println("hi = " + result);
			offSet = Integer.toBinaryString(result);
			//cut string
			offSet = offSet.substring(16,32);
				//System.out.println("offset  =" + offSet);
		}
		else{
			String binary = Integer.toBinaryString(result);
			offSet = String.format(format,Integer.parseInt(binary));

		}

		return offSet;
	}

	public static String getTargetAddr(String line)
	{
		String tAddr = line.split("\\s")[1];
		tAddr = tAddr.trim();

		//System.out.println("tAddr="+tAddr);
		return tAddr;

	}

	//Converts a num given in string format to binary string
	public static String decStringToBinary(String dec)
	{
		return Integer.toBinaryString(Integer.parseInt(dec));
	}
}

