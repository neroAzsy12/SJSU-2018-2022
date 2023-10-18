// Name: control_unit.v
// Module: CONTROL_UNIT
// Output: RF_DATA_W  : Data to be written at register file address RF_ADDR_W
//         RF_ADDR_W  : Register file address of the memory location to be written
//         RF_ADDR_R1 : Register file address of the memory location to be read for RF_DATA_R1
//         RF_ADDR_R2 : Registere file address of the memory location to be read for RF_DATA_R2
//         RF_READ    : Register file Read signal
//         RF_WRITE   : Register file Write signal
//         ALU_OP1    : ALU operand 1
//         ALU_OP2    : ALU operand 2
//         ALU_OPRN   : ALU operation code
//         MEM_ADDR   : Memory address to be read in
//         MEM_READ   : Memory read signal
//         MEM_WRITE  : Memory write signal
//         
// Input:  RF_DATA_R1 : Data at ADDR_R1 address
//         RF_DATA_R2 : Data at ADDR_R1 address
//         ALU_RESULT    : ALU output data
//         CLK        : Clock signal
//         RST        : Reset signal
//
// INOUT: MEM_DATA    : Data to be read in from or write to the memory
//
// Notes: - Control unit synchronize operations of a processor
//
// Revision History:
//
// Version	Date		Who		email			note
//------------------------------------------------------------------------------------------
//  1.0     Sep 10, 2014	Kaushik Patra	kpatra@sjsu.edu		Initial creation
//  1.1     Oct 19, 2014        Kaushik Patra   kpatra@sjsu.edu         Added ZERO status output
//------------------------------------------------------------------------------------------
`include "prj_definition.v"
module CONTROL_UNIT(MEM_DATA, RF_DATA_W, RF_ADDR_W, RF_ADDR_R1, RF_ADDR_R2, RF_READ, RF_WRITE,
                    ALU_OP1, ALU_OP2, ALU_OPRN, MEM_ADDR, MEM_READ, MEM_WRITE,
                    RF_DATA_R1, RF_DATA_R2, ALU_RESULT, ZERO, CLK, RST); 

// Output signals
// Outputs for register file 
output [`DATA_INDEX_LIMIT:0] RF_DATA_W;
output [`REG_ADDR_INDEX_LIMIT:0] RF_ADDR_W, RF_ADDR_R1, RF_ADDR_R2;
output RF_READ, RF_WRITE;
// Outputs for ALU
output [`DATA_INDEX_LIMIT:0]  ALU_OP1, ALU_OP2;
output  [`ALU_OPRN_INDEX_LIMIT:0] ALU_OPRN;
// Outputs for memory
output [`ADDRESS_INDEX_LIMIT:0]  MEM_ADDR;
output MEM_READ, MEM_WRITE;

// Input signals
input [`DATA_INDEX_LIMIT:0] RF_DATA_R1, RF_DATA_R2, ALU_RESULT;
input ZERO, CLK, RST;

// Inout signal
inout [`DATA_INDEX_LIMIT:0] MEM_DATA;

// State nets
wire [2:0] proc_state;

PROC_SM state_machine(.STATE(proc_state),.CLK(CLK),.RST(RST));

//Output Register
reg RF_READ_RET, RF_WRITE_RET, MEM_READ_RET, MEM_WRITE_RET;
reg [`DATA_INDEX_LIMIT:0] RF_DATA_W_RET, ALU_OP1_RET, ALU_OP2_RET, MEM_DATA_RET;
reg [`REG_ADDR_INDEX_LIMIT:0] RF_ADDR_W_RET, RF_ADDR_R1_RET, RF_ADDR_R2_RET;
reg [`ADDRESS_INDEX_LIMIT:0] MEM_ADDR_RET;
reg [`ALU_OPRN_INDEX_LIMIT:0] ALU_OPRN_RET;

//Internal Register
reg [`ADDRESS_INDEX_LIMIT:0] PC_REG, SP_REF;
reg [`DATA_INDEX_LIMIT:0] INST_REG;
reg [5:0] opcode;
reg [4:0] rs;
reg [4:0] rt;
reg [4:0] rd;
reg [4:0] shamt;
reg [5:0] funct;
reg [15:0] immediate;
reg [25:0] address;
reg [`DATA_INDEX_LIMIT:0] SIGN_EXTENDED;
reg [`DATA_INDEX_LIMIT:0] ZERO_EXTENDED;
reg [`DATA_INDEX_LIMIT:0] LUI;
reg [`DATA_INDEX_LIMIT:0] JUMP_ADDRESS;

//ALU Assign
assign ALU_OP1 = ALU_OP1_RET; assign ALU_OP2 = ALU_OP2_RET; assign ALU_OPRN = ALU_OPRN_RET;

//Memory and Register Assign
assign RF_READ = RF_READ_RET; assign RF_WRITE = RF_WRITE_RET;
assign MEM_READ = MEM_READ_RET; assign MEM_WRITE = MEM_WRITE_RET;
assign RF_ADDR_W = RF_ADDR_W_RET; assign RF_DATA_W = RF_DATA_W_RET;
assign RF_ADDR_R1 = RF_ADDR_R1_RET; assign RF_ADDR_R2 = RF_ADDR_R2_RET;
assign MEM_ADDR = MEM_ADDR_RET;
assign MEM_DATA = ((MEM_READ===1'b0)&&(MEM_WRITE===1'b1))?MEM_DATA_RET:{`DATA_WIDTH{1'bz} };

initial
begin
 PC_REG = 'h0001000;
 SP_REF = 'h3ffffff;
end

always @ (proc_state)
begin
if(proc_state === `PROC_FETCH)
begin
 MEM_ADDR_RET = PC_REG;
 MEM_READ_RET = 1'b1; MEM_WRITE_RET = 1'b0;
 RF_READ_RET = 1'b0; RF_WRITE_RET = 1'b0;
end

if(proc_state === `PROC_DECODE)
begin
 INST_REG = MEM_DATA;
 //Parse Instruction
 //R-Type
 {opcode, rs, rt, rd, shamt, funct} = INST_REG;
 //I-Type
 {opcode, rs, rt, immediate} = INST_REG;
 //J-Type
 {opcode, address} = INST_REG;

 //Immediate sign extension
 SIGN_EXTENDED = {{16{immediate[15]}},immediate};

 //Immediate zero extension
 ZERO_EXTENDED = {16'h0000, immediate};
 //LUI value
 LUI = {immediate, 16'h0000};

 //Store 32-bit jumpaddress
 JUMP_ADDRESS = {6'b0, address};

 //Register
 RF_ADDR_R1_RET = rs;
 RF_ADDR_R2_RET = rt;
 RF_READ_RET = 1'b1;

 end

if(proc_state === `PROC_EXE)
begin
 case (opcode)
 // R-Type Instructions, share same opcode
 6'h00 : 
 begin
   if(funct === 6'h08)
   begin
    PC_REG = RF_DATA_R1;
   end
   //line 149 handles logical right/left shift
   else if(funct === 6'h01 || funct === 6'h02)
   begin
    ALU_OPRN_RET = funct;
    ALU_OP1_RET = RF_DATA_R1;
    ALU_OP2_RET = shamt;
   end

   else
   begin 
     ALU_OPRN_RET = funct;
     ALU_OP1_RET = RF_DATA_R1;
     ALU_OP2_RET = RF_DATA_R2;
   end
 end

 //I-Type
 6'h08 : //addi
 begin
   ALU_OPRN_RET = `ALU_OPRN_WIDTH'h20;
   ALU_OP1_RET = RF_DATA_R1;
   ALU_OP2_RET = SIGN_EXTENDED;
 end

 6'h1d : //muli
 begin
   ALU_OPRN_RET =`ALU_OPRN_WIDTH'h2c;
   ALU_OP1_RET = RF_DATA_R1;
   ALU_OP2_RET = SIGN_EXTENDED;
 end

 6'h0c : //andi
 begin
   ALU_OPRN_RET = `ALU_OPRN_WIDTH'h24;
   ALU_OP1_RET = RF_DATA_R1;
   ALU_OP2_RET = ZERO_EXTENDED;
 end

 6'h0d : //ori
 begin
   ALU_OPRN_RET = `ALU_OPRN_WIDTH'h25;
   ALU_OP1_RET = RF_DATA_R1;
   ALU_OP2_RET = ZERO_EXTENDED;
 end

 6'h0a : //slti
 begin
   ALU_OPRN_RET = `ALU_OPRN_WIDTH'h2a;
   ALU_OP1_RET = RF_DATA_R1;
   ALU_OP2_RET = SIGN_EXTENDED;
 end

 6'h23 : //load word
 begin
   ALU_OPRN_RET = `ALU_OPRN_WIDTH'h20;
   ALU_OP1_RET = RF_DATA_R1;
   ALU_OP2_RET = SIGN_EXTENDED;
 end

 6'h2b : //store word
 begin
   ALU_OPRN_RET =`ALU_OPRN_WIDTH'h20;
   ALU_OP1_RET = RF_DATA_R1;
   ALU_OP2_RET = SIGN_EXTENDED;
 end

 //J-Type
 6'h1b : //push
 begin
 RF_ADDR_R1_RET = 0;
 end
 endcase

end

if(proc_state === `PROC_MEM)
begin
 MEM_READ_RET = 1'b0;
 MEM_WRITE_RET = 1'b0;

 case (opcode)
 
 6'h23 : //LW
 begin
   MEM_ADDR_RET = ALU_RESULT;
   MEM_READ_RET = 1'b1;
 end

 6'h2b : //SW
 begin
   MEM_ADDR_RET = ALU_RESULT;
   MEM_DATA_RET = RF_DATA_R2;
   MEM_WRITE_RET = 1'b1;
 end 
 
 6'h1b : //Push
 begin
   MEM_ADDR_RET = SP_REF;
   MEM_DATA_RET = RF_DATA_R1;
   MEM_WRITE_RET = 1'b1;
   SP_REF = SP_REF - 1;
 end
 
 6'h1c : //pop
 begin
   SP_REF = SP_REF + 1;
   MEM_ADDR_RET = SP_REF;
   MEM_READ_RET = 1'b1;
 end

 endcase
end

if(proc_state === `PROC_WB)
begin
 PC_REG = PC_REG + 1;
 RF_READ_RET = 1'b0; RF_WRITE_RET = 1'b0;
 MEM_READ_RET = 1'b0; MEM_WRITE_RET = 1'b0;
 //Write to register for necessary instructions
  case (opcode)
 
//R-Type write back
 6'h00 : //opcode for R-Type instructions
 begin
   if(funct === 6'h08)
     PC_REG = RF_DATA_R1;
   else
   begin
     RF_ADDR_W_RET = rd;
     // result from the ALU
     RF_DATA_W_RET = ALU_RESULT; 
     RF_WRITE_RET = 1'b1;
   end
 end

 //I-Type
 6'h08 : 
 begin
  RF_ADDR_W_RET = rt;
  RF_DATA_W_RET = ALU_RESULT;
  RF_WRITE_RET = 1'b1;
 end
 6'h1d : 
 begin
  RF_ADDR_W_RET = rt;
  RF_DATA_W_RET = ALU_RESULT;
  RF_WRITE_RET = 1'b1;
 end
 6'h0c :
 begin
  RF_ADDR_W_RET = rt;
  RF_DATA_W_RET = ALU_RESULT;
  RF_WRITE_RET = 1'b1;
 end
 6'h0d :
 begin
  RF_ADDR_W_RET = rt;
  RF_DATA_W_RET = ALU_RESULT;
  RF_WRITE_RET = 1'b1;
 end
 6'h0f :
 begin
  RF_ADDR_W_RET = rt;
  RF_DATA_W_RET = LUI;
  RF_WRITE_RET = 1'b1;
 end
 6'h0a :
 begin
  RF_ADDR_W_RET = rt;
  RF_DATA_W_RET = ALU_RESULT;
  RF_WRITE_RET = 1'b1;
 end
 6'h04 :
 begin
  if(RF_DATA_R1 == RF_DATA_R2)
    PC_REG = PC_REG + SIGN_EXTENDED;
 end
 6'h05 :
 begin
  if(RF_DATA_R1 != RF_DATA_R2)
    PC_REG = PC_REG + SIGN_EXTENDED;
 end
 6'h23 :
 begin
   RF_ADDR_W_RET = rt;
   RF_DATA_W_RET = MEM_DATA;
   RF_WRITE_RET = 1'b1;
 end

 //J-Type
 6'h02 : PC_REG = JUMP_ADDRESS; //jmp

 6'h03 : //jal
 begin
   RF_ADDR_W_RET = 31;
   RF_DATA_W_RET = PC_REG;
   RF_WRITE_RET = 1'b1;
   PC_REG = JUMP_ADDRESS;
 end

 6'h1c : //pop
 begin
   RF_ADDR_W_RET = 0;
   RF_DATA_W_RET = MEM_DATA;
   RF_WRITE_RET = 1'b1;
 end
 endcase

end

end
endmodule;

//------------------------------------------------------------------------------------------
// Module: CONTROL_UNIT
// Output: STATE      : State of the processor
//         
// Input:  CLK        : Clock signal
//         RST        : Reset signal
//
// INOUT: MEM_DATA    : Data to be read in from or write to the memory
//
// Notes: - Processor continuously cycle witnin fetch, decode, execute, 
//          memory, write back state. State values are in the prj_definition.v
//
// Revision History:
//
// Version	Date		Who		email			note
//------------------------------------------------------------------------------------------
//  1.0     Sep 10, 2014	Kaushik Patra	kpatra@sjsu.edu		Initial creation
//------------------------------------------------------------------------------------------
module PROC_SM(STATE,CLK,RST);
// list of inputs
input CLK, RST;
// list of outputs
output [2:0] STATE;
//reg
reg [2:0] state_reg;
reg [2:0] next_state;

assign STATE = state_reg;

// initiation of the state
initial
begin
  state_reg = 3'bxx;
  next_state = `PROC_FETCH;
end

// reset signal to end the state machine
always@(negedge RST)
begin
  state_reg = 2'bxx;
  next_state = `PROC_FETCH;
end

//state switching
always@(posedge CLK)
begin

 case(STATE) // implements the 5 clock states
   `PROC_FETCH : next_state = `PROC_DECODE;
   `PROC_DECODE : next_state = `PROC_EXE;
   `PROC_EXE : next_state = `PROC_MEM;
   `PROC_MEM : next_state = `PROC_WB;
   `PROC_WB : next_state = `PROC_FETCH;
 endcase
 state_reg = next_state;
end
endmodule;