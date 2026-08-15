package net.plshark.ddnsupdater

import net.plshark.ddnsupdater.ddns.DdnsProvider
import org.springframework.boot.context.properties.ConfigurationProperties

/** Settings for the ddns-updater application. */
@ConfigurationProperties("ddns-updater")
data class Settings(
  val ddns: List<DdnsEntrySettings>,
  val ipProviders: List<String> = listOf("ipify", "ifconfig"),
  val namecheap: NamecheapSettings = NamecheapSettings(),
  val ifconfig: IfconfigSettings = IfconfigSettings(),
  val ipify: IpifySettings = IpifySettings(),
)

/** Settings for what hosts should have DDNS entries updated. */
data class DdnsEntrySettings(
  val domain: String,
  val provider: DdnsProvider,
  val hosts: List<String>,
)

/** Settings for accessing Namecheap DDNS. */
data class NamecheapSettings(
  val url: String = "https://dynamicdns.park-your-domain.com",
  val password: String? = null,
)

/** Settings for fetching the current IP address using ifconfig. */
data class IfconfigSettings(
  val url: String = "https://ifconfig.me/ip",
)

/** Settings for fetching the current IP address using ipify. */
data class IpifySettings(
  val url: String = "https://api.ipify.org",
)
