package net.plshark.namecheap.ddns.impl

import net.plshark.namecheap.NamecheapClientException
import net.plshark.namecheap.ddns.NamecheapDdnsClient
import net.plshark.namecheap.ddns.NamecheapDdnsSettings
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBodilessEntity

/** Default [NamecheapDdnsClient] implementation. */
class NamecheapDdnsClientImpl(
  private val webClient: WebClient,
  settings: NamecheapDdnsSettings,
) : NamecheapDdnsClient {
  private val baseUrl: String
  private val password: String

  init {
    check(settings.baseUrl.isNotEmpty()) { "Namecheap URL cannot be empty" }
    baseUrl = settings.baseUrl
    check(!settings.password.isNullOrEmpty()) { "Namecheap password cannot be empty" }
    password = settings.password
  }

  override suspend fun updateHostIpv4(
    host: String,
    domain: String,
    ip: String,
  ) {
    try {
      webClient
        .get()
        .uri("$baseUrl/update") { builder ->
          builder.queryParam("host", host)
          builder.queryParam("domain", domain)
          builder.queryParam("password", password)
          builder.queryParam("ip", ip)
          builder.build()
        }.retrieve()
        .awaitBodilessEntity()
    } catch (e: WebClientRequestException) {
      throw e.toNamecheapException()
    } catch (e: WebClientResponseException) {
      throw e.toNamecheapException()
    }
  }

  private fun WebClientRequestException.toNamecheapException() =
    NamecheapClientException(statusCode = null, cause = this)

  private fun WebClientResponseException.toNamecheapException() =
    NamecheapClientException(
      statusCode = this.statusCode.value(),
      message = this.responseBodyAsString,
      cause = this,
    )
}
