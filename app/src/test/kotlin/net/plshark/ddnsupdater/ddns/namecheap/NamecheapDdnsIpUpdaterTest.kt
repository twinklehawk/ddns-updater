package net.plshark.ddnsupdater.ddns.namecheap

import com.google.common.net.InetAddresses
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import net.plshark.ddnsupdater.ddns.DdnsProvider
import net.plshark.namecheap.ddns.NamecheapDdnsClient
import net.plshark.test.TestUtils.doBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NamecheapDdnsIpUpdaterTest {
  private val client = mockk<NamecheapDdnsClient>()
  private val updater = NamecheapDdnsIpUpdater(client)

  @Test
  fun `can handle namecheap providers`() {
    assertThat(updater.canHandle(DdnsProvider.Namecheap)).isTrue()
  }

  @Test
  fun `sends an update request to namecheap for a new IPv4 address`() =
    doBlocking {
      coEvery { client.updateHostIpv4(any(), any(), any()) } just Runs

      updater.updateHostIp("test", "domain.com", InetAddresses.forString("127.0.0.1"))

      coVerify { client.updateHostIpv4("test", "domain.com", "127.0.0.1") }
    }

  @Test
  fun `skips update requests for IPv6 addresses`() =
    doBlocking {
      updater.updateHostIp("test", "domain.com", InetAddresses.forString("::1"))

      coVerify(exactly = 0) { client.updateHostIpv4(any(), any(), any()) }
    }
}
