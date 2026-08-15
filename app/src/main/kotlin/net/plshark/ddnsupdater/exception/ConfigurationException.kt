package net.plshark.ddnsupdater.exception

/** A [RuntimeException] indicating a problem with the application configuration. */
class ConfigurationException(
  message: String,
) : RuntimeException(message)
