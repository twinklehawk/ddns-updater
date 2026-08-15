package net.plshark.ddnsupdater.ddns.namecheap

import net.plshark.ddnsupdater.ddns.DdnsProvider
import net.plshark.ddnsupdater.ddns.DdnsProviderIpUpdater
import net.plshark.namecheap.ddns.NamecheapDdnsClient
import org.slf4j.LoggerFactory
import java.net.Inet4Address
import java.net.InetAddress

/** A [DdnsProviderIpUpdater] implementation for Namecheap. */
class NamecheapDdnsIpUpdater(
  private val client: NamecheapDdnsClient,
) : DdnsProviderIpUpdater {
  private val log = LoggerFactory.getLogger(NamecheapDdnsIpUpdater::class.java)

  override fun canHandle(provider: DdnsProvider) = provider == DdnsProvider.Namecheap

  override suspend fun updateHostIp(
    host: String,
    domain: String,
    ip: InetAddress,
  ) {
    if (ip is Inet4Address) {
      client.updateHostIpv4(host, domain, ip.hostAddress)
    } else {
      log.warn("Namecheap does not support IPv6 addresses for DDNS")
    }
  }
}
