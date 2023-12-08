package net.plshark.ddnsupdater

import net.plshark.ddnsupdater.ddns.DdnsProvider
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("ddns-updater")
data class Config(
    val ddns: List<DdnsEntryConfig>,
    val ipProviders: List<String> = listOf("ipify", "ifconfig"),
    val namecheap: NamecheapConfig = NamecheapConfig(),
    val ifconfig: IfconfigConfig = IfconfigConfig(),
    val ipifyConfig: IpifyConfig = IpifyConfig(),
)

data class DdnsEntryConfig(
    val domain: String,
    val provider: DdnsProvider,
    val hosts: List<String>,
)

data class NamecheapConfig(
    val enabled: Boolean = false,
    val url: String = "https://dynamicdns.park-your-domain.com",
    val password: String? = null,
)

data class IfconfigConfig(
    val url: String = "https://ifconfig.me/ip",
)

data class IpifyConfig(
    val url: String = "https://api.ipify.org",
)
