package net.plshark.ddnsupdater.ipaddress

import com.google.common.net.InetAddresses
import net.plshark.ddnsupdater.IfconfigSettings
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.net.Inet4Address

/** A [LocalIpProvider] using ifconfig. */
class IfconfigLocalIpProvider(
  private val httpClient: WebClient,
  private val settings: IfconfigSettings,
) : LocalIpProvider {
  init {
    check(settings.url.isNotEmpty()) { "Ifconfig URL cannot be empty" }
  }

  override suspend fun getLocalIpv4(): Inet4Address {
    val response =
      httpClient
        .get()
        .uri(settings.url)
        .accept(MediaType.TEXT_PLAIN)
        .retrieve()
        .awaitBody<String>()
    return InetAddresses.forString(response) as Inet4Address
  }
}
