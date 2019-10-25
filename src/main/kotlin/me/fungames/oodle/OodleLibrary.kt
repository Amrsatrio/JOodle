package me.fungames.oodle

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer


internal interface OodleLibrary : Library {
    /**
     * Original signature : `int OodleLZ_Compress(int, uint8*, size_t, uint8*, int, void*, size_t, size_t, void*, size_t)`<br></br>
     * *native declaration : line 2*
     */
    fun OodleLZ_Compress(
        codec: Int,
        src_buf: Pointer,
        src_len: Long,
        dst_buf: Pointer,
        level: Int,
        opts: Pointer?,
        offs: Long,
        unused: Long,
        scratch: Pointer?,
        scratch_size: Long
    ): Int

    /**
     * Original signature : `int OodleLZ_Decompress(uint8*, int, uint8*, size_t, int, int, int, uint8*, size_t, void*, void*, void*, size_t, int)`<br></br>
     */
    fun OodleLZ_Decompress(
        src_buf: Pointer,
        src_len: Int,
        dst: Pointer,
        dst_size: Long,
        fuzz: Int,
        crc: Int,
        verbose: Int,
        dst_base: Pointer?,
        e: Long,
        cb: Pointer?,
        cb_ctx: Pointer?,
        scratch: Pointer?,
        scratch_size: Long,
        threadPhase: Int
    ): Int

    companion object {
        const val JNA_LIBRARY_NAME = "oo2core_7_win64"
        val INSTANCE = Native.loadLibrary(JNA_LIBRARY_NAME, OodleLibrary::class.java) as OodleLibrary
    }
}