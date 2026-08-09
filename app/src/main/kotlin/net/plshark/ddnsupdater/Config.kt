package net.plshark.ddnsupdater

import net.plshark.ddnsupdater.ddns.DdnsProvider
import org.springframework.boot.context.properties.ConfigurationProperties

/** Settings for the ddns-updater application. */
@ConfigurationProperties("ddns-updater")
data class Config(
  val ddns: List<DdnsEntryConfig>,
  val ipProviders: List<String> = listOf("ipify", "ifconfig"),
  val namecheap: NamecheapConfig = NamecheapConfig(),
  val ifconfig: IfconfigConfig = IfconfigConfig(),
  val ipifyConfig: IpifyConfig = IpifyConfig(),
)

/** Settings for what hosts should have DDNS entries updated. */
data class DdnsEntryConfig(
  val domain: String,
  val provider: DdnsProvider,
  val hosts: List<String>,
)

/** Settings for accessing Namecheap DDNS. */
data class NamecheapConfig(
  val url: String = "https://dynamicdns.park-your-domain.com",
  val password: String? = null,
)

/** Settings for fetching the current IP address using ifconfig. */
data class IfconfigConfig(
  val url: String = "https://ifconfig.me/ip",
)

/** Settings for fetching the current IP address using ipify. */
data class IpifyConfig(
  val url: String = "https://api.ipify.org",
)
