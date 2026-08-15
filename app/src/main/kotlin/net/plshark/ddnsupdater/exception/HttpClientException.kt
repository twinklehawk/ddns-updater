package net.plshark.ddnsupdater.exception

/** An exception that will probably be removed. */
class HttpClientException(
  statusCode: Int,
  message: String?,
) : RuntimeException("$statusCode: $message")
