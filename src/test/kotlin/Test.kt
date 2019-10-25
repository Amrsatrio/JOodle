import me.fungames.oodle.Oodle
import java.io.File

fun main() {
    var bytes = File("D:\\Fabian\\Desktop\\PakBrowser\\Oodle\\oodlerec\\scanner\\test.t").readBytes()
    bytes = bytes.copyOfRange(4, bytes.size)
    val dec = Oodle.decompress(bytes, 8056)
    println(dec.toString(Charsets.UTF_8))
}