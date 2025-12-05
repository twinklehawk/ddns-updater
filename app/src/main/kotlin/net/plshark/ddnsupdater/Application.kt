package net.plshark.ddnsupdater

import net.plshark.ddnsupdater.ddns.DdnsIpUpdater
import net.plshark.ddnsupdater.ddns.DdnsProvider
import net.plshark.ddnsupdater.ddns.NamecheapDdnsIpUpdater
import net.plshark.ddnsupdater.exception.ConfigurationException
import net.plshark.ddnsupdater.ipaddress.HostIpLookup
import net.plshark.ddnsupdater.ipaddress.IfconfigLocalIpProvider
import net.plshark.ddnsupdater.ipaddress.IpifyLocalIpProvider
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.net.http.HttpClient

@SpringBootApplication
@EnableConfigurationProperties(Config::class)
class Application {
    @Bean
    fun httpClient(): HttpClient = HttpClient.newHttpClient()

    @Bean
    fun ddnsIpUpdater(
        config: Config,
        httpClient: HttpClient,
    ): DdnsIpUpdater {
        val providers =
            config.ddns
                .map { it.provider }
                .distinct()
                .map {
                    when (it) {
                        DdnsProvider.Namecheap -> NamecheapDdnsIpUpdater(httpClient, config.namecheap)
                    }
                }

        return DdnsIpUpdater(providers)
    }

    @Bean
    fun hostIpLookup(
        config: Config,
        httpClient: HttpClient,
    ): HostIpLookup {
        val providers =
            config.ipProviders
                .distinct()
                .map {
                    when (it) {
                        "ifconfig" -> IfconfigLocalIpProvider(httpClient, config.ifconfig)
                        "ipify" -> IpifyLocalIpProvider(httpClient, config.ipifyConfig)
                        else -> throw ConfigurationException("Unknown IP provider $it")
                    }
                }.toList()

        return HostIpLookup(providers)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(args = args) {
        setWebApplicationType(WebApplicationType.NONE)
    }
}
