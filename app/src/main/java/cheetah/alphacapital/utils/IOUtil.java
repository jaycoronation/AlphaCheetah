package cheetah.alphacapital.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IOUtil {

    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }
    
    public static String getExtension(String fileName) 
    {
    	String ext = "";
    	try {
			ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    	} catch (Exception e) {
			e.printStackTrace();
		}
        return ext;
    }
}
