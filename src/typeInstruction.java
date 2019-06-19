import java.util.*;
import java.util.stream.Stream;

public class typeInstruction{

	public Parser p = new Parser();

	public static Map<String, String> opMap;
	static {
		opMap= new HashMap<>();
		opMap.put("add", "000000");
		opMap.put("sub", "000000");
		opMap.put("and", "000000");
		opMap.put("or", "000000");
		opMap.put("addi", "001000");
		opMap.put("sll", "000000");
		opMap.put("slt", "000000");
		opMap.put("beq", "000100");
		opMap.put("bne", "000101");
		opMap.put("lw", "100011");
		opMap.put("sw", "101011");
		opMap.put("j", "000010");
		opMap.put("jr", "000000");
		opMap.put("jal", "000011");
	};

	public static Map<String, String> functMap;
	static {
		functMap= new HashMap<>();
		functMap.put("add", "100000");
		functMap.put("sub", "100010");
		functMap.put("and", "100100");
		functMap.put("or", "100101");
		functMap.put("sll", "000000");
		functMap.put("slt", "101010");
		functMap.put("jr", "001000");
	};

/*getFormat:  reads in a string s that instruction text
 * returns 0 - if it decides that string is  register
 * returns 1 - if it decides that string is immediate*
 * returns 2 - if it decides that string is jump 
 */
	public static int getFormat(String opCode)
	{

		//CHECK IS opMap doesnt contain opcode
		if(opMap.get(opCode) == null) {
		    //System.out.println("No such instruction\n");
			return -1;
		}
		//To see if you opMap s is a register type
		else if (opCode.equals("add") || //add
			opCode.equals("or") || //or
			opCode.equals("and") || //and
			opCode.equals("sub") || //sub
			opCode.equals("slt") || //slt
			opCode.equals("jr")  ||//jr
		    opCode.equals("sll"))
				return 0;
		//TO see if opMap is immediate type
		else if(opCode.equals("beq") || //beq
				opCode.equals("bne") || //bne
				opCode.equals("addi") || //addi
				opCode.equals("sw") ||
				opCode.equals("lw"))
				return 1;
		else if(opCode.equals("j") || //j
				opCode.equals("jal")) //jal
				return 2;

		return -1; //if not in thi isa
}

}


