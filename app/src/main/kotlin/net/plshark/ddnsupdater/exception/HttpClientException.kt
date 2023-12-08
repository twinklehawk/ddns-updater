package net.plshark.ddnsupdater.exception

class HttpClientException(statusCode: Int, message: String?) : RuntimeException("$statusCode: $message")
