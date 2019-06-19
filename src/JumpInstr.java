public class JumpInstr extends Instruction {
    //opcode will be inherited from Instruction
    public String addr;

    public JumpInstr(String op, String addr) {
        super.op = op;
        this.addr = addr;
    }

    public String toString() {
        return super.toString() + " " + addr;
    }
}
