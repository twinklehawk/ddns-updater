package net.plshark.namecheap.ddns

/** Settings for the Namecheap DDNS client. */
data class NamecheapDdnsSettings(
  val baseUrl: String = "https://dynamicdns.park-your-domain.com",
  val password: String? = null,
)
