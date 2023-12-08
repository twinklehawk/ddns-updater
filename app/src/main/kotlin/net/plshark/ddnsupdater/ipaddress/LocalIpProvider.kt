package net.plshark.ddnsupdater.ipaddress

import java.net.Inet4Address

interface LocalIpProvider {
    suspend fun getLocalIpv4(): Inet4Address
}
