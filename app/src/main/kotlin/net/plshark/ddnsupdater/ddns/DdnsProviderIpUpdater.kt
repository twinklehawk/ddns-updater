package net.plshark.ddnsupdater.ddns

import java.net.InetAddress

interface DdnsProviderIpUpdater {
    fun canHandle(provider: DdnsProvider): Boolean

    suspend fun updateHostIp(
        host: String,
        domain: String,
        ip: InetAddress,
    )
}
