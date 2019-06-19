# mips-assembler - 2 pass assembler

## Assume the following regarding the input: 

1. Comments - Comments start with '#'.  A comment will start at the beginning of a line or may appear after an instruction (on the same line).  If a comment appears after an instruction, there may be whitespace before the '#' symbol.

2.  Labels - Labels will only contain alphanumeric characters (0-9, a-z, and A-Z).  There will be no other symbols in the labels.  A colon ':' will appear after a label.  There may be whitespace before a label and the label may be followed by an instruction. A label may appear on a line without an instruction. 

3.  Blank lines may contain a mixture of whitespace characters.
4.  There may be some whitespace between operands. 

5.  Immediate values may be negative.

6.  Your assembler must support the following instructions:  and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, and jal.

7. You do NOT need to support the following registers: $at, $k0, $k1, $gp, $fp.

8. On the same line, whitespace may appear between a label and an instruction. There will always be whitespace between j/jal instructions and the target label.

9. Your program should error check for invalid instructions.  This assembler will be used in future lab assignments, so you may wish to create data structures to store the instructions.

## Implementation:
This assembler is called a 2-pass assembler because it takes 2 passes through the file in order to generate the machine code.  The first pass should run through all the lines of the file to compute the address of each label.  During the second pass, all the instructions are converted to machine code.

## Usage
cd src
make
java mipsAssembler inFile.asm

