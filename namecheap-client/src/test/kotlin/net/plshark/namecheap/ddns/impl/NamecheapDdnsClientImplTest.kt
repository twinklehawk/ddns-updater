package net.plshark.namecheap.ddns.impl

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import net.plshark.namecheap.NamecheapClientException
import net.plshark.namecheap.ddns.NamecheapDdnsSettings
import net.plshark.test.TestUtils.doBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.reactive.function.client.WebClient

class NamecheapDdnsClientImplTest {
  private val server = MockWebServer()
  private lateinit var client: NamecheapDdnsClientImpl

  @BeforeEach
  fun init() {
    server.start()
    val settings =
      NamecheapDdnsSettings(
        baseUrl = server.url("").toString().removeSuffix("/"),
        password = "test-password",
      )
    client =
      NamecheapDdnsClientImpl(
        webClient = WebClient.create(),
        settings = settings,
      )
  }

  @AfterEach
  fun cleanup() {
    server.close()
  }

  @Test
  fun `updateHostIpv4 sends the correct request`() =
    doBlocking {
      server.enqueue(MockResponse())

      client.updateHostIpv4("test", "domain.com", "127.0.0.1")

      val request = server.takeRequest()
      assertThat(request.method).isEqualTo("GET")
      assertThat(request.target)
        .isEqualTo("/update?host=test&domain=domain.com&password=test-password&ip=127.0.0.1")
    }

  @Test
  fun `updateHostIpv4 wraps exceptions`() =
    doBlocking {
      server.enqueue(MockResponse(code = 404))

      val e = assertThrows<NamecheapClientException> { client.updateHostIpv4("test", "domain.com", "127.0.0.1") }

      assertThat(e.statusCode).isEqualTo(404)
    }
}
