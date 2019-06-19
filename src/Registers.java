import java.util.HashMap;
import java.util.Map;
import java.util.Map.*;

public class Registers {

	public static Map<String, String> regMap = new HashMap<String, String>();

	static{
	regMap.put("$0","00000");

	regMap.put("$zero","00000");

	regMap.put("$v0","00010");

	regMap.put("$v1","00011");

	regMap.put("$a0","00100");

	regMap.put("$a1","00101");

	regMap.put("$a2","00110");

	regMap.put("$a3","00111");

	regMap.put("$t0","01000");

	regMap.put("$t1","01001");

	regMap.put("$t2","01010");

	regMap.put("$t3","01011");

	regMap.put("$t4","01100");

	regMap.put("$t5","01101");

	regMap.put("$t6","01110");

	regMap.put("$t7","01111");

	regMap.put("$s0","10000");

	regMap.put("$s1","10001");

	regMap.put("$s2","10010");

	regMap.put("$s3","10011");

	regMap.put("$s4","10100");

	regMap.put("$s5","10101");

	regMap.put("$s6","10110");

	regMap.put("$s7","10111");

	regMap.put("$t8","11000");

	regMap.put("$t9","11001");

	regMap.put("$sp","11101");

	regMap.put("$ra","11111");

};
		
		
		

	

}
