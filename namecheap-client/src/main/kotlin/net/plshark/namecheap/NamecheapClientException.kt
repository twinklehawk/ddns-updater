package net.plshark.namecheap

/** A [RuntimeException] indicating an error calling a Namecheap API. */
class NamecheapClientException(
  val statusCode: Int? = null,
  message: String? = null,
  cause: Throwable? = null,
) : RuntimeException(message, cause)
