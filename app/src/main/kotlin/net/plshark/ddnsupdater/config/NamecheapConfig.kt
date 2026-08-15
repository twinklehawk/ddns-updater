package net.plshark.ddnsupdater.config

import net.plshark.ddnsupdater.Settings
import net.plshark.namecheap.ddns.NamecheapDdnsClient
import net.plshark.namecheap.ddns.NamecheapDdnsSettings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

/** Spring configuration for Namecheap. */
@Configuration(proxyBeanMethods = false)
class NamecheapConfig {
  /** Creates a [NamecheapDdnsClient] instance. */
  @Bean
  fun client(
    webClientBuilder: WebClient.Builder,
    settings: Settings,
  ): NamecheapDdnsClient {
    val clientSettings =
      NamecheapDdnsSettings(
        password = settings.namecheap.password,
        baseUrl = settings.namecheap.url,
      )
    return NamecheapDdnsClient.create(clientSettings, webClientBuilder.build())
  }
}
