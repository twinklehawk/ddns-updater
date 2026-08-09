package net.plshark.ddnsupdater.http

import net.plshark.ddnsupdater.exception.HttpClientException
import java.net.http.HttpResponse

/** Utilities for using [java.net.http.HttpClient]. */
object HttpUtils {
  /** Throws an [HttpClientException] if the response status code is an error code. */
  fun checkResponse(response: HttpResponse<String>) {
    if (response.statusCode() != HttpStatus.OK) {
      throw HttpClientException(response.statusCode(), response.body())
    }
  }
}
