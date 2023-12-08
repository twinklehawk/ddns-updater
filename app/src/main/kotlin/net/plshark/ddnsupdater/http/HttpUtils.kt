package net.plshark.ddnsupdater.http

import net.plshark.ddnsupdater.exception.HttpClientException
import java.net.http.HttpResponse

object HttpUtils {
    fun checkResponse(response: HttpResponse<String>) {
        if (response.statusCode() != HttpStatus.OK) {
            throw HttpClientException(response.statusCode(), response.body())
        }
    }
}
