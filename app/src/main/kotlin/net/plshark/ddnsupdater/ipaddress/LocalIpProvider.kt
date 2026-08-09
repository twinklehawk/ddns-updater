package net.plshark.ddnsupdater.ipaddress

import java.net.Inet4Address

/** An interface for retrieving the current local IP address. */
interface LocalIpProvider {
  /** Retrieves the current local IPv4 address. */
  suspend fun getLocalIpv4(): Inet4Address
}
