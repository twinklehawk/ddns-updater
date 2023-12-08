package net.plshark.ddnsupdater.ddns

import com.google.common.net.InetAddresses
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import net.plshark.ddnsupdater.exception.ConfigurationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DdnsIpUpdaterTest {
    private val provider = mockk<DdnsProviderIpUpdater>()
    private val updater = DdnsIpUpdater(listOf(provider))

    @Test
    fun `finds a matching provider and updates the host IP`() =
        runTest {
            val ip = InetAddresses.forString("127.0.0.1")
            every { provider.canHandle(DdnsProvider.Namecheap) } returns true
            coJustRun { provider.updateHostIp(any(), any(), any()) }

            updater.updateHostIp("test", "domain.com", DdnsProvider.Namecheap, ip)

            coVerify { provider.updateHostIp("test", "domain.com", ip) }
        }

    @Test
    fun `throws an exception if nothing can handle the provider`() =
        runTest {
            val ip = InetAddresses.forString("127.0.0.1")
            every { provider.canHandle(DdnsProvider.Namecheap) } returns false

            assertThrows<ConfigurationException> {
                updater.updateHostIp("test", "domain.com", DdnsProvider.Namecheap, ip)
            }
        }
}
