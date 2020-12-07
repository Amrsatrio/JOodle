package me.fungames.oodle

import java.io.IOException

open class OodleException(message: String?, cause: Throwable? = null) : IOException(message, cause)
class DecompressException(message: String?, cause: Throwable? = null) : OodleException(message, cause)
class CompressException(message: String?, cause: Throwable? = null) : OodleException(message, cause)