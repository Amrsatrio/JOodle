package me.fungames.oodle

import com.sun.jna.Memory
import com.sun.jna.Pointer
import mu.KotlinLogging
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

object Oodle {
    private val logger = KotlinLogging.logger {  }
    private lateinit var oodleLib: OodleLibrary

    fun decompress(src: ByteArray, dstLength : Int) : ByteArray {
        val dst = ByteArray(dstLength)
        decompress(src, dst)
        return dst
    }
    fun decompress(src : ByteArray, dst : ByteArray) {
        val start = System.currentTimeMillis()
        if (!loadLib())
            throw IllegalStateException("oodle library could not be loaded")

        val srcLength = src.size
        val dstLength = dst.size
        val sourcePointer = Memory(srcLength.toLong())
        sourcePointer.write(0L, src, 0, srcLength)
        val dstPointer = Memory(dstLength.toLong())
        val resultCode = oodleLib.OodleLZ_Decompress(
            sourcePointer, srcLength,
            dstPointer, dstLength.toLong(),
            0, 0, 0, Pointer.NULL, 0L, Pointer.NULL, Pointer.NULL, Pointer.NULL, 0L, 0
        )
        if (resultCode <= 0)
            throw DecompressException("Oodle decompression failed with code $resultCode")
        dstPointer.read(0, dst, 0, dstLength)
        val stop = System.currentTimeMillis()
        val seconds = (stop - start).toFloat() / 1000
        logger.debug("Oodle decompress: $srcLength => $dstLength ($seconds seconds)")
    }

    fun compress(src : ByteArray, compressor : Int, compressionLevel : Int) : ByteArray {
        val start = System.currentTimeMillis()
        if (!loadLib())
            throw IllegalStateException("oodle library could not be loaded")

        val srcLength = src.size
        val dstLength = srcLength + 65536
        val sourcePointer = Memory(srcLength.toLong())
        sourcePointer.write(0L, src, 0, srcLength)
        val dstPointer = Memory(dstLength.toLong())
        val resultCode = oodleLib.OodleLZ_Compress(
            compressor,
            sourcePointer, srcLength.toLong(),
            dstPointer, compressionLevel,
            Pointer.NULL, 0, 0, Pointer.NULL, 0
        )
        if (resultCode <= 0)
            throw DecompressException("Oodle compression failed with code $resultCode")
        val dst = dstPointer.getByteArray(0, resultCode)
        val stop = System.currentTimeMillis()
        val seconds = (stop - start).toFloat() / 1000
        logger.debug("Oodle decompress: $srcLength => $dstLength ($seconds seconds)")
        return dst
    }

    private fun loadLib() : Boolean {
        if(::oodleLib.isInitialized)
            return true
        val oodleLibFile = File(OodleLibrary.JNA_LIBRARY_NAME + ".dll")
        if (!oodleLibFile.exists()) {
            val input = this::class.java.getResourceAsStream("/${OodleLibrary.JNA_LIBRARY_NAME}.dll") ?: return false
            val out = FileOutputStream(oodleLibFile)
            input.copyTo(out)
            input.close()
            out.close()
        }
        System.setProperty("jna.library.path", System.getProperty("user.dir"))
        return try {
            this.oodleLib = OodleLibrary.INSTANCE
            true
        } catch (e : Exception) {
            false
        }
    }
}