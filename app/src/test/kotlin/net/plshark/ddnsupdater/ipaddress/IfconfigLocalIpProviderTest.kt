package net.plshark.ddnsupdater.ipaddress

import com.google.common.net.InetAddresses
import kotlinx.coroutines.test.runTest
import net.plshark.ddnsupdater.IfconfigConfig
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.http.HttpClient

class IfconfigLocalIpProviderTest {
    private val server = MockWebServer()
    private val httpClient = HttpClient.newHttpClient()
    private lateinit var provider: IfconfigLocalIpProvider

    @BeforeEach
    fun setup() {
        server.start()
        val config =
            IfconfigConfig(
                url = server.url("/").toString().dropLast(1),
            )
        provider = IfconfigLocalIpProvider(httpClient, config)
    }

    @AfterEach
    fun cleanup() {
        server.shutdown()
    }

    @Test
    fun `throws an exception if the configured URL is empty`() {
        assertThrows<IllegalStateException> { IfconfigLocalIpProvider(httpClient, IfconfigConfig("")) }
    }

    @Test
    fun `retrieves the local IP from the configured URL`() =
        runTest {
            server.enqueue(MockResponse().setBody("127.0.0.1"))

            assertThat(provider.getLocalIpv4()).isEqualTo(InetAddresses.forString("127.0.0.1"))

            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.headers["Accept"]).isEqualTo("text/plain")
        }
}
