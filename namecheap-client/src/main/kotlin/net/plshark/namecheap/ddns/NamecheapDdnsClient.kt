package net.plshark.namecheap.ddns

import net.plshark.namecheap.ddns.impl.NamecheapDdnsClientImpl
import org.springframework.web.reactive.function.client.WebClient

/** Client for updating Namecheap DDNS records. */
interface NamecheapDdnsClient {
  /**
   * Updates the IPv4 address in the A record for a host+domain combination.
   *
   * @param domain the top-level domain managed by Namecheap, like example.com
   * @param host the subdomain to be updated
   */
  suspend fun updateHostIpv4(
    host: String,
    domain: String,
    ip: String,
  )

  companion object {
    /** Creates a new [NamecheapDdnsClient]. */
    fun create(settings: NamecheapDdnsSettings): NamecheapDdnsClient = create(settings, WebClient.create())

    /** Creates a new [NamecheapDdnsClient]. */
    fun create(
      settings: NamecheapDdnsSettings,
      webClient: WebClient,
    ): NamecheapDdnsClient = NamecheapDdnsClientImpl(webClient = webClient, settings = settings)
  }
}
