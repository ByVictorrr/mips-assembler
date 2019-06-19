public class ImmedInstr extends Instruction
{
    //opcode will be inherited from Instruction
   public String rs;
   public String rt;
   public String immed;

    public ImmedInstr(String op, String rs, String rt, String immed){
        super.op = op;
        this.rs = rs;
        this.rt = rt;
        this.immed = immed;
    }
     public String toString() {
        return  super.toString() + " " +
                this.rt  + " " + this.rs + " " + this.immed;
    }

}
