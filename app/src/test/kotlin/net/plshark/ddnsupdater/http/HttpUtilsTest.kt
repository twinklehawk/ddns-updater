package net.plshark.ddnsupdater.http

import io.mockk.every
import io.mockk.mockk
import net.plshark.ddnsupdater.exception.HttpClientException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.http.HttpResponse

class HttpUtilsTest {
    @Test
    fun `returns if the response status is 200`() {
        val response = mockk<HttpResponse<String>>()
        every { response.statusCode() } returns HttpStatus.OK
        HttpUtils.checkResponse(response)
    }

    @Test
    fun `throws an exception if the response status is not 200`() {
        val response = mockk<HttpResponse<String>>()
        every { response.statusCode() } returns HttpStatus.INTERNAL_SERVER_ERROR
        every { response.body() } returns null
        assertThrows<HttpClientException> { HttpUtils.checkResponse(response) }
    }
}
