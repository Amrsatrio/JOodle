package me.fungames.oodle

import com.sun.jna.Library
import com.sun.jna.Pointer

internal interface OodleLibrary : Library {
    /**
     * Original signature: `int OodleLZ_Compress(int, uint8*, size_t, uint8*, int, void*, size_t, size_t, void*, size_t)`<br></br>
     * *native declaration: line 2*
     */
    fun OodleLZ_Compress(
        compressor: Int,
        rawBuf: Pointer,
        rawLen: Long,
        compBuf: Pointer,
        level: Int,
        pOptions: Pointer?,
        dictionaryBase: Long,
        lrm: Long,
        scratchMem: Pointer?,
        scratchSize: Long
    ): Int

    /**
     * Original signature: `int OodleLZ_Decompress(uint8*, int, uint8*, size_t, int, int, int, uint8*, size_t, void*, void*, void*, size_t, int)`
     */
    fun OodleLZ_Decompress(
        compBuf: Pointer,
        compBufSize: Int,
        rawBuf: Pointer,
        rawLen: Long,
        fuzzSafe: Int,
        checkCRC: Int,
        verbosity: Int,
        decBufBase: Pointer?,
        decBufSize: Long,
        fpCallback: Pointer?,
        callbackUserData: Pointer?,
        decoderMemory: Pointer?,
        decoderMemorySize: Long,
        threadPhase: Int
    ): Int
}