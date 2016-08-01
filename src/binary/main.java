package binary;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
public class main{
	 public static void main(String[] args) throws IOException {
		 
		 
		 /**
		  * write number in int16 and make the file size much smaller
		  */
		 File fileEcg1 = new File("xxxx.bin");
		 
		 double[] data = {1.11,-0.551,5.55,4.44};
		 for(int i = 0; i < 4; i++){
			 if (!fileEcg1.exists()){
	             try{
	                 fileEcg1.createNewFile();
	             }
	             catch (IOException e)
	             {
	                 e.printStackTrace();
	             }
	         }
	         try {
	             //short temp = (short) (data[i]*32768/10);
	
	             DataOutputStream out = new DataOutputStream(new FileOutputStream(fileEcg1, true));
	             ByteBuffer buffer = ByteBuffer.allocate(8);
	             buffer.order(ByteOrder.LITTLE_ENDIAN);
	             buffer.putDouble(data[i]);
	             out.write(buffer.array());
	             buffer.clear();
	
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
		 }
		 
		 
		 /**
		  * testing code for the constructor
		  */
		 /*writeFileInBinary manager =  new writeFileInBinary(true, "data", "example", true);
		 manager.writeHeader(0, 1, 1, "ecg_data" , manager.out);
		 manager.writeInDouble(5, manager.out); // depend on
		 manager.writeInDouble(-4, manager.out); // depend on
		 
		 manager.adjustDemesion(1, 6);*/
		 
		 /*manager.writeInDouble(3, manager.out); // depend on
		 manager.writeInDouble(2, manager.out); // depend on
		 manager.writeInDouble(-14, manager.out); // depend on
		 manager.writeInDouble(31, manager.out); // depend on
		 manager.writeInDouble(21, manager.out); // depend on
		 manager.writeInDouble(21, manager.out); // depend on
		 manager.writeInDouble(5, manager.out); // depend on
		 manager.writeInDouble(-4, manager.out); // depend on
		 manager.writeInDouble(-103, manager.out); // depend on
		 manager.writeInDouble(221, manager.out); // depend on
		 manager.writeInDouble(-14, manager.out); // depend on
		 manager.writeInDouble(31, manager.out); // depend on
		 manager.writeInDouble(-201, manager.out); // depend on
		 manager.writeInDouble(21, manager.out); // depend on
		 manager.writeInDouble(3, manager.out); // depend on
		 manager.writeInDouble(2, manager.out); // depend on
		 manager.writeInDouble(-14, manager.out); // depend on
		 manager.writeInDouble(31, manager.out); // depend on
		 manager.writeInDouble(21, manager.out); // depend on
		 manager.writeInDouble(21, manager.out); // depend on
		 manager.writeInDouble(5, manager.out); // depend on
*/	 }
}