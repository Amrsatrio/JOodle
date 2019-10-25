package me.fungames.oodle

class DecompressException(override val message: String?, override val cause: Throwable? = null) : Exception()
class CompressException(override val message: String?, override val cause: Throwable? = null) : Exception()