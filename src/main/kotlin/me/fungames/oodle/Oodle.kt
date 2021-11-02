package me.fungames.oodle

import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.Pointer
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileOutputStream

const val COMPRESSOR_LZH = 0
const val COMPRESSOR_LZHLW = 1
const val COMPRESSOR_LZNIB = 2
const val COMPRESSOR_NONE = 3
const val COMPRESSOR_LZB16 = 4
const val COMPRESSOR_LZBLW = 5
const val COMPRESSOR_LZA = 6
const val COMPRESSOR_LZNA = 7
const val COMPRESSOR_KRAKEN = 8
const val COMPRESSOR_MERMAID = 9
const val COMPRESSOR_BITKNIT = 10
const val COMPRESSOR_SELKIE = 11
const val COMPRESSOR_HYDRA = 12
const val COMPRESSOR_LEVIATHAN = 13

const val COMPRESSION_LEVEL_NONE = 0
const val COMPRESSION_LEVEL_SUPER_FAST = 1
const val COMPRESSION_LEVEL_VERY_FAST = 2
const val COMPRESSION_LEVEL_FAST = 3
const val COMPRESSION_LEVEL_NORMAL = 4
const val COMPRESSION_LEVEL_OPTIMAL1 = 5
const val COMPRESSION_LEVEL_OPTIMAL2 = 6
const val COMPRESSION_LEVEL_OPTIMAL3 = 7
const val COMPRESSION_LEVEL_OPTIMAL4 = 8
const val COMPRESSION_LEVEL_OPTIMAL5 = 9

/**
 * Singleton for Oodle decompression and compression
 */
object Oodle {
    private val logger = LoggerFactory.getLogger("Oodle")
    private lateinit var oodleLib: OodleLibrary

    /**
     * Decompresses an Oodle compressed array
     * @param src the compressed source data
     * @param dstLen the uncompressed length
     * @return the decompressed data
     * @throws DecompressException when the decompression fails
     */
    @Throws(DecompressException::class)
    @JvmStatic
    fun decompress(src: ByteArray, dstLen: Int) = ByteArray(dstLen).also { decompress(src, it) }

    /**
     * Decompresses an Oodle compressed array
     * @param src the compressed source data
     * @param dst the destination buffer
     * @throws DecompressException when the decompression fails
     * @throws IllegalStateException when the library could not be loaded
     */
    @Throws(DecompressException::class)
    @JvmStatic
    fun decompress(src: ByteArray, dst: ByteArray) {
        decompress(src, 0, src.size, dst, 0, dst.size)
    }

    /**
     * Decompresses an Oodle compressed array
     * @param src the compressed source data
     * @param srcOff the offset into `src`
     * @param srcLen the compressed length
     * @param dst the destination buffer
     * @param dstOff the offset into `dst`
     * @param dstLen the uncompressed length
     * @throws DecompressException when the decompression fails
     * @throws IllegalStateException when the library could not be loaded
     */
    @Throws(DecompressException::class)
    @JvmStatic
    fun decompress(src: ByteArray, srcOff: Int, srcLen: Int, dst: ByteArray, dstOff: Int, dstLen: Int) {
        ensureLib()
        val start = System.currentTimeMillis()
        val sourcePointer = Memory(srcLen.toLong())
        sourcePointer.write(0L, src, srcOff, srcLen)
        val dstPointer = Memory(dstLen.toLong())
        val resultCode = oodleLib.OodleLZ_Decompress(
            sourcePointer, srcLen,
            dstPointer, dstLen.toLong(),
            0, 0, Integer.MAX_VALUE, Pointer.NULL, 0L, Pointer.NULL, Pointer.NULL, Pointer.NULL, 0L, 0
        )
        if (resultCode <= 0)
            throw DecompressException("Oodle decompression failed with code $resultCode")
        dstPointer.read(0, dst, dstOff, dstLen)
        val stop = System.currentTimeMillis()
        val seconds = (stop - start).toFloat() / 1000
        logger.trace("Oodle decompress: $srcLen => $dstLen ($seconds seconds)")
    }

    /**
     * Compresses a byte array
     * @param uncompressed the uncompressed source data
     * @param compressor the compressor to use
     * @param compressionLevel the compression level to use
     * @return the compressed data
     * @throws CompressException when the compression fails
     * @throws IllegalStateException when the library could not be loaded
     */
    @Throws(CompressException::class)
    @JvmStatic
    fun compress(uncompressed: ByteArray, compressor: Int, compressionLevel: Int): ByteArray {
        ensureLib()
        val start = System.currentTimeMillis()
        val srcLength = uncompressed.size
        val dstLength = srcLength + 65536
        val sourcePointer = Memory(srcLength.toLong())
        sourcePointer.write(0L, uncompressed, 0, srcLength)
        val dstPointer = Memory(dstLength.toLong())
        val resultCode = oodleLib.OodleLZ_Compress(
            compressor,
            sourcePointer, srcLength.toLong(),
            dstPointer, compressionLevel,
            Pointer.NULL, 0, 0, Pointer.NULL, 0
        )
        if (resultCode <= 0)
            throw CompressException("Oodle compression failed with code $resultCode")
        val dst = dstPointer.getByteArray(0, resultCode)
        val stop = System.currentTimeMillis()
        val seconds = (stop - start).toFloat() / 1000
        logger.trace("Oodle decompress: $srcLength => $dstLength ($seconds seconds)")
        return dst
    }

    @Throws(IllegalStateException::class)
    private fun ensureLib() {
        if (::oodleLib.isInitialized)
            return
        val oodleLibName = System.getProperty("JOodle.libName", "oo2core_7_win64.dll")
        val oodleLibFile = File(oodleLibName)
        var canLoad = oodleLibFile.exists()
        if (!canLoad && System.getProperty("os.name").contains("Windows")) {
            val input = this::class.java.getResourceAsStream("/oo2core_7_win64.dll")
            if (input != null) {
                val out = FileOutputStream(oodleLibFile)
                input.copyTo(out)
                input.close()
                out.close()
                canLoad = true
            }
        }
        if (!canLoad){
            throw IllegalStateException("Oodle library could not be loaded")
        }
        System.setProperty("jna.library.path", System.getProperty("user.dir"))
        try {
            oodleLib = Native.load(oodleLibName, OodleLibrary::class.java)
        } catch (e: Exception) {
            throw IllegalStateException("Oodle library failed to load", e)
        }
    }
}