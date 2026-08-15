package net.plshark.ddnsupdater

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import net.plshark.ddnsupdater.ddns.DdnsIpUpdater
import net.plshark.ddnsupdater.exception.ConfigurationException
import net.plshark.ddnsupdater.ipaddress.HostIpLookup
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.net.InetAddress

/** A [CommandLineRunner] that updates the DDNS entries for the configured hosts. */
@Component
class UpdateRunner(
  private val settings: Settings,
  private val ipLookup: HostIpLookup,
  private val ddnsIpUpdater: DdnsIpUpdater,
) : CommandLineRunner {
  private val log = LoggerFactory.getLogger(Application::class.java)

  override fun run(vararg args: String) {
    validateConfig(settings)

    runBlocking {
      val newIp = ipLookup.getLocalIpv4()
      settings.ddns
        .flatMap { entry -> entry.hosts.map { Pair(it, entry) } }
        .forEach { processHost(it.first, it.second, newIp) }
    }
  }

  @Suppress("TooGenericExceptionCaught")
  private suspend fun processHost(
    host: String,
    ddnsEntry: DdnsEntrySettings,
    newIp: InetAddress,
  ) {
    val hostname = "$host.${ddnsEntry.domain}"
    try {
      val currentIp = ipLookup.getIpv4ForHost(hostname)
      if (currentIp != newIp) {
        log.info("Updating IP to {} for {}", newIp.hostAddress, hostname)
        ddnsIpUpdater.updateHostIp(host, ddnsEntry.domain, ddnsEntry.provider, newIp)
      } else {
        log.info("Skipping unchanged IP {} for {}", currentIp.hostAddress, hostname)
      }
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      log.error("Failed to process host {}", hostname, e)
    }
  }

  private fun validateConfig(settings: Settings) {
    if (settings.ddns.isEmpty()) {
      throw ConfigurationException("No ddns entries configured")
    }
  }
}
