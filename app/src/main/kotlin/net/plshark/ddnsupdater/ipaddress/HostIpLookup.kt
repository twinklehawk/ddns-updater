package net.plshark.ddnsupdater.ipaddress

import net.plshark.ddnsupdater.exception.IpLookupException
import org.slf4j.LoggerFactory
import java.net.Inet4Address
import java.net.InetAddress
import java.net.UnknownHostException

class HostIpLookup(
    private val ipProviders: List<LocalIpProvider>,
) {
    private val log = LoggerFactory.getLogger(HostIpLookup::class.java)

    init {
        check(ipProviders.isNotEmpty()) { "At least one IP provider must be enabled" }
    }

    fun getIpv4ForHost(host: String): Inet4Address? =
        getIpsForHost(host)
            .firstOrNull { it is Inet4Address }
            ?.let { it as Inet4Address }

    @Suppress("TooGenericExceptionCaught")
    suspend fun getLocalIpv4(): Inet4Address {
        for (provider: LocalIpProvider in ipProviders) {
            try {
                return provider.getLocalIpv4()
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
