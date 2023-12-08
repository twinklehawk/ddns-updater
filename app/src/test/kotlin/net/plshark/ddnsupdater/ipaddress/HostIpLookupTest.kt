package net.plshark.ddnsupdater.ipaddress

import com.google.common.net.InetAddresses
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import net.plshark.ddnsupdater.exception.IpLookupException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.Inet4Address
import java.net.UnknownHostException

class HostIpLookupTest {
    private val provider1 = mockk<LocalIpProvider>()
    private val provider2 = mockk<LocalIpProvider>()
    private val lookup = HostIpLookup(listOf(provider1, provider2))

    @Test
    fun `throws an exception if the provider list is empty`() {
        assertThrows<IllegalStateException> { HostIpLookup(emptyList()) }
    }

    @Test
    fun `returns the v4 IP address for a host`() {
        val ip = lookup.getIpv4ForHost("google.com")
        assertThat(ip).isNotNull()
    }

    @Test
    fun `returns null if no address for a host`() {
        val ip = lookup.getIpv4ForHost("invalid.plshark.net")
        assertThat(ip).isNull()
    }

    @Test
    fun `uses the configured provider to look up the local IP address`() =
        runTest {
            coEvery { provider1.getLocalIpv4() } returns InetAddresses.forString("127.0.0.1") as Inet4Address
            val ip = lookup.getLocalIpv4()
            assertThat(ip.hostAddress).isEqualTo("127.0.0.1")
        }

    @Test
    fun `uses the next configured provider if one fails`() =
        runTest {
            coEvery { provider1.getLocalIpv4() } throws UnknownHostException()
            coEvery { provider2.getLocalIpv4() } returns InetAddresses.forString("127.0.0.1") as Inet4Address
            val ip = lookup.getLocalIpv4()
            assertThat(ip.hostAddress).isEqualTo("127.0.0.1")
        }

    @Test
    fun `throws an exception if all providers fail`() =
        runTest {
            coEvery { provider1.getLocalIpv4() } throws UnknownHostException()
            coEvery { provider2.getLocalIpv4() } throws UnknownHostException()
            assertThrows<IpLookupException> { lookup.getLocalIpv4() }
        }
}
