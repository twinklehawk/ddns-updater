package net.plshark.ddnsupdater

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.plshark.ddnsupdater.ddns.DdnsIpUpdater
import net.plshark.ddnsupdater.exception.ConfigurationException
import net.plshark.ddnsupdater.ipaddress.HostIpLookup
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.net.InetAddress

@Component
class UpdateRunner(
    private val config: Config,
    private val ipLookup: HostIpLookup,
    private val ddnsIpUpdater: DdnsIpUpdater,
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(Application::class.java)

    override fun run(vararg args: String?) {
        validateConfig(config)

        runBlocking {
            val newIp = ipLookup.getLocalIpv4()
            config.ddns
                .flatMap { entry -> entry.hosts.map { Pair(it, entry) } }
                .map {
                    launch(Dispatchers.Default) {
                        processHost(it.first, it.second, newIp)
                    }
                }.joinAll()
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun processHost(
        host: String,
        ddnsEntry: DdnsEntryConfig,
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
        } catch (e: Exception) {
            log.error("Failed to process host {}", hostname, e)
        }
    }

    private fun validateConfig(config: Config) {
        if (config.ddns.isEmpty()) {
            throw ConfigurationException("No ddns entries configured")
        }
    }
}
