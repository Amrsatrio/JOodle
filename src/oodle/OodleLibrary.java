package oodle;
import com.ochafik.lang.jnaerator.runtime.NativeSize;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
public interface OodleLibrary extends Library {
	public static final String JNA_LIBRARY_NAME = "oo2core_7_win64";
	public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(OodleLibrary.JNA_LIBRARY_NAME);
	@SuppressWarnings("deprecation")
	public static final OodleLibrary INSTANCE = (OodleLibrary)Native.loadLibrary(OodleLibrary.JNA_LIBRARY_NAME, OodleLibrary.class);
	/**
	 * Original signature : <code>int OodleLZ_Compress(int, uint8*, size_t, uint8*, int, void*, size_t, size_t, void*, size_t)</code><br>
	 * <i>native declaration : line 2</i>
	 */
	int OodleLZ_Compress(int codec, OodleLibrary.uint8 src_buf, NativeSize src_len, OodleLibrary.uint8 dst_buf, int level, Pointer opts, NativeSize offs, NativeSize unused, Pointer scratch, NativeSize scratch_size);
	/**
	 * Original signature : <code>int OodleLZ_Decompress(uint8*, int, uint8*, size_t, int, int, int, uint8*, size_t, void*, void*, void*, size_t, int)</code><br>
	 */
	int OodleLZ_Decompress(OodleLibrary.uint8 src_buf, int src_len, OodleLibrary.uint8 dst, NativeSize dst_size, int fuzz, int crc, int verbose, OodleLibrary.uint8 dst_base, NativeSize e, Pointer cb, Pointer cb_ctx, Pointer scratch, NativeSize scratch_size, int threadPhase);
	
	public static class uint8 extends PointerType {
		public uint8(Pointer address) {
			super(address);
		}
		public uint8() {
			super();
		}
	};
}
