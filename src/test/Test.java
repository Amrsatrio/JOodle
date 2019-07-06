/**
 * 
 */
package test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import oodle.Oodle;
import oodle.OodleCompressionFormat;
import oodle.OodleCompressionLevel;

/**
 * @author FunGames
 *
 */
public class Test {
	public static void main(String[] args) throws IOException {
		InputStream in = new FileInputStream("D:\\Fabian\\Desktop\\PakBrowser\\Oodle\\oodlerec\\scanner\\test.t");
		in.skip(4);
	    byte[] buffer = new byte[1024];
	    int read = -1;
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    while((read = in.read(buffer)) != -1) {
	    	bos.write(buffer, 0, read);
	    }
	    
	    byte[] src = bos.toByteArray();
	    int src_Length = src.length;
	    byte[] dst = new byte[8056];
	    int dst_Length = dst.length;
	    
	   byte[] res = Oodle.oodleDecompress(src, dst_Length);
	   
	   byte[] recompressed = Oodle.oodleCompress(res, OodleCompressionFormat.COMPRESSOR_LZB16, OodleCompressionLevel.COMPRESSION_LEVEL_OPTIMAL5);
	   System.out.println();
	    
	    
	}
}
