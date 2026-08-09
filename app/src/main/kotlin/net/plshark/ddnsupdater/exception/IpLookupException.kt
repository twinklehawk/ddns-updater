package net.plshark.ddnsupdater.exception

/** A [RuntimeException] indicating a problem when looking up the current IP address. */
class IpLookupException(
  message: String,
) : RuntimeException(message)
