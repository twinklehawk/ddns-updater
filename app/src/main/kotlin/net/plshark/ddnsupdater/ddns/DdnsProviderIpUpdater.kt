package net.plshark.ddnsupdater.ddns

import java.net.InetAddress

/** Interface for updating a host's A record for a specific DDNS provider. */
interface DdnsProviderIpUpdater {
  /** Returns whether this [DdnsProviderIpUpdater] can handle the DDNS provider. */
  fun canHandle(provider: DdnsProvider): Boolean

  /** Updates the host's A record to the specified IP address. */
  suspend fun updateHostIp(
    host: String,
    domain: String,
    ip: InetAddress,
  )
}
