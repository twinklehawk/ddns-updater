package net.plshark.ddnsupdater.ddns

import kotlinx.coroutines.future.await
import net.plshark.ddnsupdater.NamecheapConfig
import net.plshark.ddnsupdater.http.HttpUtils
import org.slf4j.LoggerFactory
import java.net.Inet4Address
import java.net.InetAddress
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class NamecheapDdnsIpUpdater(
    private val httpClient: HttpClient,
    private val config: NamecheapConfig,
) : DdnsProviderIpUpdater {
    private val log = LoggerFactory.getLogger(NamecheapDdnsIpUpdater::class.java)

    init {
        check(config.url.isNotEmpty()) { "Namecheap URL cannot be empty" }
        check(!config.password.isNullOrEmpty()) { "Namecheap password cannot be empty" }
    }

    override fun canHandle(provider: DdnsProvider) = provider == DdnsProvider.Namecheap

    override suspend fun updateHostIp(
        host: String,
        domain: String,
        ip: InetAddress,
    ) {
        if (ip is Inet4Address) {
            updateHostIpv4(host, domain, ip.hostAddress)
        } else {
            log.warn("Namecheap does not support IPv6 addresses for DDNS")
        }
    }

    private suspend fun updateHostIpv4(
        host: String,
        domain: String,
        ip: String,
    ) {
        val url = "${config.url}/update?host=$host&domain=$domain&password=${config.password}&ip=$ip"
        val request =
            HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build()
        val response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).await()
        HttpUtils.checkResponse(response)
    }
}
