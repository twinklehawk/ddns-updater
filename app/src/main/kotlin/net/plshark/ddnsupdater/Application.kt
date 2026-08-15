package net.plshark.ddnsupdater

import net.plshark.ddnsupdater.ddns.DdnsIpUpdater
import net.plshark.ddnsupdater.ddns.DdnsProviderIpUpdater
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

/** Main application class. */
@SpringBootApplication
@EnableConfigurationProperties(Settings::class)
class Application {
  /** Creates an [HttpClient] instance. */
  @Bean
  fun httpClient(): HttpClient = HttpClient.newHttpClient()

  /** Creates a [DdnsIpUpdater] instance. */
  @Bean
  fun ddnsIpUpdater(updaters: List<DdnsProviderIpUpdater>): DdnsIpUpdater = DdnsIpUpdater(updaters)

  /** Creates a [HostIpLookup] instance. */
  @Bean
  fun hostIpLookup(
    settings: Settings,
    httpClient: HttpClient,
  ): HostIpLookup {
    val providers =
      settings.ipProviders
        .distinct()
        .map {
          when (it) {
            "ifconfig" -> IfconfigLocalIpProvider(httpClient, settings.ifconfig)
            "ipify" -> IpifyLocalIpProvider(httpClient, settings.ipify)
            else -> throw ConfigurationException("Unknown IP provider $it")
          }
        }.toList()

    return HostIpLookup(providers)
  }
}

/** Application entry point. */
fun main(args: Array<String>) {
  runApplication<Application>(args = args) {
    setWebApplicationType(WebApplicationType.NONE)
  }
}
