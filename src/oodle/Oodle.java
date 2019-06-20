/**
 * 
 */
package oodle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.ochafik.lang.jnaerator.runtime.NativeSize;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * @author FunGames
 *
 */
public class Oodle {
	private static OodleLibrary oodleLib = null;

	public static byte[] oodleDecompress(byte[] src, int dst_Length) {
		if (!loadLib())
			return null;
		
		int src_Length = src.length;
		byte[] dst = new byte[dst_Length];
		
		long start = System.currentTimeMillis();
		Memory sourcePointer = new Memory(src_Length);
		sourcePointer.write(0, src, 0, src_Length);
		Memory resultPointer = new Memory(dst_Length);
		resultPointer.write(0, dst, 0, dst_Length);
		int resultCode = oodleLib.OodleLZ_Decompress(new OodleLibrary.uint8(sourcePointer), src_Length, new OodleLibrary.uint8(resultPointer),  new NativeSize(dst_Length), 0, 0, 0, new OodleLibrary.uint8(), new NativeSize(0), com.sun.jna.Pointer.createConstant(0), com.sun.jna.Pointer.createConstant(0), com.sun.jna.Pointer.createConstant(0), new NativeSize(0), 0);
		double seconds = (double) ((System.currentTimeMillis() - start) / 1000) % 60 ;
		if(resultCode <= 0) {
			System.err.println("Oodle Decompression failed: " + resultCode);
			return null;
		}
		byte[] res = resultPointer.getByteArray(0, resultCode); 
		double mbPerSec = src_Length * 1e-6 / seconds;
		System.out.println(String.format("Oodle Decompress: %d => %d (%.2f seconds, %.2f MB/s)", src_Length, resultCode, seconds, mbPerSec));
		return res;
	}
	
	public static byte[] oodleCompress(byte[] input, int compressor, int compressionLevel) {
		if (!loadLib())
			return null;
		
		int input_Size = input.length;
		int dst_Length = input_Size + 65536;
		byte[] dst = new byte[dst_Length];
		
		long start = System.currentTimeMillis();
		Memory inputPointer = new Memory(input_Size);
		inputPointer.write(0, input, 0, input_Size);
		Memory resultPointer = new Memory(dst_Length);
		resultPointer.write(0, dst, 0, dst_Length);
		int resultCode = oodleLib.OodleLZ_Compress(compressor, new OodleLibrary.uint8(inputPointer), new NativeSize(input_Size), new OodleLibrary.uint8(resultPointer), compressionLevel, Pointer.createConstant(0), new NativeSize(0), new NativeSize(0), Pointer.createConstant(0), new NativeSize(0));
		double seconds = (double) ((System.currentTimeMillis() - start) / 1000) % 60 ;
		if(resultCode <= 0) {
			System.err.println("Oodle Compression failed: " + resultCode);
			return null;
		}
		byte[] res = resultPointer.getByteArray(0, resultCode);
		double mbPerSec = input_Size * 1e-6 / seconds;
		System.out.println(String.format("Oodle Compress: %d => %d (%.2f seconds, %.2f MB/s)", input_Size, resultCode, seconds, mbPerSec));
		return res;
	}

	private static boolean loadLib() {
		if (oodleLib != null)
			return true;
		System.setProperty("jna.library.path", System.getProperty("user.dir"));
		InputStream in = Oodle.class.getResourceAsStream("oo2core_7_win64.dll");

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(System.getProperty("user.dir") + "/oo2core_7_win64.dll"));
			int read = -1;
			byte[] buffer = new byte[1024];
			while ((read = in.read(buffer)) != -1) {
				fos.write(buffer, 0, read);
			}
			in.close();
			fos.close();
			oodleLib = OodleLibrary.INSTANCE;
			return oodleLib != null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
