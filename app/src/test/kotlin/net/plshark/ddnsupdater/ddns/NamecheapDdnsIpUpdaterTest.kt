package net.plshark.ddnsupdater.ddns

import com.google.common.net.InetAddresses
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import net.plshark.ddnsupdater.NamecheapConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.http.HttpClient

class NamecheapDdnsIpUpdaterTest {
    private val server = MockWebServer()
    private val httpClient = HttpClient.newHttpClient()
    private lateinit var updater: NamecheapDdnsIpUpdater

    @BeforeEach
    fun setup() {
        server.start()
        val config =
            NamecheapConfig(
                url = server.url("/").toString().dropLast(1),
                password = "test-password",
            )
        updater = NamecheapDdnsIpUpdater(httpClient, config)
    }

    @AfterEach
    fun cleanup() {
        server.close()
    }

    @Test
    fun `throws an exception if the URL is empty`() {
        assertThrows<IllegalStateException> {
            NamecheapDdnsIpUpdater(httpClient, NamecheapConfig("", "test-password"))
        }
    }

    @Test
    fun `throws an exception if the password is empty`() {
        assertThrows<IllegalStateException> {
            NamecheapDdnsIpUpdater(httpClient, NamecheapConfig("https://test", null))
        }
    }

    @Test
    fun `can handle namecheap providers`() {
        assertThat(updater.canHandle(DdnsProvider.Namecheap)).isTrue()
    }

    @Test
    fun `sends an update request to namecheap for a new IPv4 address`() =
        runTest {
            server.enqueue(MockResponse())

            updater.updateHostIp("test", "domain.com", InetAddresses.forString("127.0.0.1"))

            val request = server.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.target)
                .isEqualTo("/update?host=test&domain=domain.com&password=test-password&ip=127.0.0.1")
        }

    @Test
    fun `skips update requests for IPv6 addresses`() =
        runTest {
            updater.updateHostIp("test", "domain.com", InetAddresses.forString("::1"))
            assertThat(server.requestCount).isEqualTo(0)
        }
}
