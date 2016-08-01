package binary;


//import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * An implementation for writing data as binary into a matlab file, and matlab is able to directly use its "load" function for this file
 * @author Yao Shi
 * Created by yao on 7/25/2016.
 */
public class writeFileInBinary{
    public boolean ifRunning;
    private String folderName, fileName, fileExtension;
    private File file;
    private boolean ifAppend;
    public FileOutputStream out;
    public int countDouble;

    /**
    * example for the file created: folderName = data, fileName = example; the file then will be /data/example.bin
    * @param ifRunning if the file is recorded in the file
    * @param folderName where the file is stored
    * @param ifAppend true for append, false for overwrite
    * */
    public writeFileInBinary(boolean ifRunning, String folderName, String fileName, boolean ifAppend){
        this.ifRunning = ifRunning;
        this.folderName = folderName;
        this.fileExtension = ".mat";
        this.fileName = fileName;
        this.ifAppend = ifAppend;
        this.countDouble = 0;
        file = new File(fileName + ".mat");
        try {
			out = new FileOutputStream(file, this.ifAppend);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //file = new File(getFilename());
        
    }

    /**
    * create a file and identify its directory,
    * the default storage is the external storage, but we can also put it into the sd card
    * */
  /*  private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File fileCreated = new File(filepath, folderName);

        if(!fileCreated.exists()){
            fileCreated.mkdirs();
        }
        return (fileCreated.getAbsolutePath() + "/" + fileName + fileExtension);
    }*/

    /**
    * write double as binary into the file
    * */
    public void writeInDouble(double data, FileOutputStream out) {
        if (ifRunning){
            if (!file.exists()){
                try{
                    file.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            
            try {
                ByteBuffer buffer = ByteBuffer.allocate(8);  
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putDouble(data);
                out.write(buffer.array());  
                buffer.clear();
                this.countDouble++;
                adjustDemesion(1, this.countDouble);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
    * write Byte directly into the file
    * */
    public void writeInByte(Byte data, FileOutputStream out) {
        if (ifRunning){
            if (!file.exists()){
                try{
                    file.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            try {
                DataOutputStream fop1 = new DataOutputStream(out);
                fop1.writeByte(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
    * write int16 as binary into the file
    * */
    public void writeInShort(Short data, FileOutputStream out) {
        if (ifRunning){
            if (!file.exists()){
                try{
                    file.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            try {
            	ByteBuffer buffer = ByteBuffer.allocate(8);  
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putShort(data);
                out.write(buffer.array());  
                buffer.clear(); 
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
    * use ifRunning to control the recording
    * */
    public void stopRecording(){
        ifRunning = false;
    }
    public void RestartRecording(){
        ifRunning = true;
    }


    /**
     *  @param datatype 0: double (64-bit); 1: char (64-bit,ascii value); 10: single (32-bit); 20: signed long (32-bit);
                   30: signed short (16-bit); 40: unsigned short (16-bit); 50: unsigned 8-bit int
     *  @param datarow number of rows of this data set, user may need to have a loop to track this number and later adjust it
     *  @param datacol number of columns of this data set, user may need to have a loop to track this number and later adjust it
     *  @param valname variable name which will be saved
    * */
    public void writeHeader(long dataType, long datarow, long datacol, String valname, FileOutputStream out) {
    	if(file.getTotalSpace() != 0) return;
    	
        long nameLength = valname.length()+1;
        long reserve = 0;
        byte[] b = valname.getBytes(Charset.forName("UTF-8"));
        int totalLength = 20 + b.length;

        byte[] header = new byte[totalLength+1];
        header[0] = (byte) (dataType & 0xff);
        header[1] = (byte) ((dataType >> 8) & 0xff);
        header[2] = (byte) ((dataType >> 16) & 0xff);
        header[3] = (byte) ((dataType >> 24) & 0xff);
        header[4] = (byte) (datarow & 0xff);
        header[5] = (byte) ((datarow >> 8) & 0xff);
        header[6] = (byte) ((datarow >> 16) & 0xff);
        header[7] = (byte) ((datarow >> 24) & 0xff);
        header[8] = (byte) (datacol & 0xff);
        header[9] = (byte) ((datacol >> 8) & 0xff);
        header[10] = (byte) ((datacol >> 16) & 0xff);
        header[11] = (byte) ((datacol >> 24) & 0xff);
        header[12] = (byte) (reserve & 0xff);
        header[13] = (byte) ((reserve >> 8) & 0xff);
        header[14] = (byte) ((reserve >> 16) & 0xff);
        header[15] = (byte) ((reserve >> 24) & 0xff);
        header[16] = (byte) (nameLength & 0xff);
        header[17] = (byte) ((nameLength >> 8) & 0xff);
        header[18] = (byte) ((nameLength >> 16) & 0xff);
        header[19] = (byte) ((nameLength >> 24) & 0xff);
        int i = 0;
        for(; i < b.length; i++) {
            header[20 + i] = b[i];
        }
        
        header[20 + i] = (byte) (0x0);

        try {
			out.write(header, 0, 21 + b.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 
     * @param datarow new data row 
     * @param datacol new data column
     * @throws IOException writeHeader is not called
     */
    public void adjustDemesion(long datarow, long datacol) throws IOException{
    	RandomAccessFile raf = new RandomAccessFile(file, "rw");
    	byte[] header = new byte[8];
    	 header[0] = (byte) (datarow & 0xff);
         header[1] = (byte) ((datarow >> 8) & 0xff);
         header[2] = (byte) ((datarow >> 16) & 0xff);
         header[3] = (byte) ((datarow >> 24) & 0xff);
         header[4] = (byte) (datacol & 0xff);
         header[5] = (byte) ((datacol >> 8) & 0xff);
         header[6] = (byte) ((datacol >> 16) & 0xff);
         header[7] = (byte) ((datacol >> 24) & 0xff);
    	try {
    	    raf.seek(4); // Go to byte at offset position 5.
    	    try {
				raf.write(header);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Write byte 70 (overwrites original byte at this offset).
    	} finally {
    	    raf.close(); // Flush/save changes and close resource.
    	}
    }
    
}
