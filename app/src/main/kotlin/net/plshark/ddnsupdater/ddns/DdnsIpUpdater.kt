package net.plshark.ddnsupdater.ddns

import net.plshark.ddnsupdater.exception.ConfigurationException
import java.net.InetAddress

/** Handles updating DDNS records. */
class DdnsIpUpdater(
  private val providerUpdaters: List<DdnsProviderIpUpdater>,
) {
  /** Updates the A DNS record for the domain to the specified IP address using the specified DDNS provider. */
  suspend fun updateHostIp(
    host: String,
    domain: String,
    provider: DdnsProvider,
    ip: InetAddress,
  ) {
    getUpdaterForProvider(provider).updateHostIp(host, domain, ip)
  }

  private fun getUpdaterForProvider(provider: DdnsProvider) =
    providerUpdaters.firstOrNull { it.canHandle(provider) }
      ?: throw ConfigurationException("Provider $provider not configured")
}
