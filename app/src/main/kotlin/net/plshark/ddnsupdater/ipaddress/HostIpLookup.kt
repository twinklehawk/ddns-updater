package net.plshark.ddnsupdater.ipaddress

import kotlinx.coroutines.CancellationException
import net.plshark.ddnsupdater.exception.IpLookupException
import org.slf4j.LoggerFactory
import java.net.Inet4Address
import java.net.InetAddress
import java.net.UnknownHostException

/** Handles looking up a IP addresses. */
class HostIpLookup(
  private val ipProviders: List<LocalIpProvider>,
) {
  private val log = LoggerFactory.getLogger(HostIpLookup::class.java)

  init {
    check(ipProviders.isNotEmpty()) { "At least one IP provider must be enabled" }
  }

  /** Retrieves the current IPv4 for a host. */
  fun getIpv4ForHost(host: String): Inet4Address? =
    getIpsForHost(host)
      .firstOrNull { it is Inet4Address }
      ?.let { it as Inet4Address }

  /** Retrieves the current local IP address. */
  @Suppress("TooGenericExceptionCaught")
  suspend fun getLocalIpv4(): Inet4Address {
    for (provider: LocalIpProvider in ipProviders) {
      try {
        return provider.getLocalIpv4()
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        log.warn("Failed to look up local IP address using provider {}", provider::class.java.name, e)
      }
    }
    throw IpLookupException("Failed to look up local IP address using configured providers")
  }

  private fun getIpsForHost(host: String): Array<InetAddress> =
    try {
      InetAddress.getAllByName(host)
    } catch (e: UnknownHostException) {
      log.debug("No IP addresses found for host {}", host, e)
      emptyArray<InetAddress>()
    }
}
