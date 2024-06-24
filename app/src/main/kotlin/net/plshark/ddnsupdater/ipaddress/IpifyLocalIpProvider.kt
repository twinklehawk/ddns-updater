package net.plshark.ddnsupdater.ipaddress

import com.google.common.net.InetAddresses
import kotlinx.coroutines.future.await
import net.plshark.ddnsupdater.IpifyConfig
import net.plshark.ddnsupdater.http.HttpUtils
import java.net.Inet4Address
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class IpifyLocalIpProvider(
    private val httpClient: HttpClient,
    private val config: IpifyConfig,
) : LocalIpProvider {
    init {
        check(config.url.isNotEmpty()) { "Ipify URL cannot be empty" }
    }

    override suspend fun getLocalIpv4(): Inet4Address {
        val request =
            HttpRequest
                .newBuilder()
                .uri(URI.create(config.url))
                .header("Accept", "text/plain")
                .GET()
                .build()
        val response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).await()
        HttpUtils.checkResponse(response)
        return InetAddresses.forString(response.body()) as Inet4Address
    }
}
