package net.plshark.ddnsupdater.ipaddress

import com.google.common.net.InetAddresses
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import net.plshark.ddnsupdater.IpifySettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.reactive.function.client.WebClient

class IpifyLocalIpProviderTest {
  private val server = MockWebServer()
  private val httpClient = WebClient.create()
  private lateinit var provider: IpifyLocalIpProvider

  @BeforeEach
  fun setup() {
    server.start()
    val config =
      IpifySettings(
        url = server.url("/").toString().dropLast(1),
      )
    provider = IpifyLocalIpProvider(httpClient, config)
  }

  @AfterEach
  fun cleanup() {
    server.close()
  }

  @Test
  fun `throws an exception if the configured URL is empty`() {
    assertThrows<IllegalStateException> { IpifyLocalIpProvider(httpClient, IpifySettings("")) }
  }

  @Test
  fun `retrieves the local IP from the configured URL`() =
    runTest {
      server.enqueue(MockResponse(body = "127.0.0.1"))

      assertThat(provider.getLocalIpv4()).isEqualTo(InetAddresses.forString("127.0.0.1"))

      val request = server.takeRequest()
      assertThat(request.method).isEqualTo("GET")
      assertThat(request.headers["Accept"]).isEqualTo("text/plain")
    }
}
