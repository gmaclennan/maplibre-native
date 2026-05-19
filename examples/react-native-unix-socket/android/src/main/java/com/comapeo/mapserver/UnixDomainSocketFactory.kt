package com.comapeo.mapserver

import org.newsclub.net.unix.AFUNIXSocket
import org.newsclub.net.unix.AFUNIXSocketAddress
import java.io.File
import java.net.InetAddress
import java.net.Socket
import javax.net.SocketFactory

/**
 * A SocketFactory that always connects to a single Unix domain socket,
 * regardless of the host/port OkHttp asks for.
 *
 * The path MUST live inside the app's private data directory
 * (e.g. context.filesDir). That gives two layers of isolation: DAC
 * (the directory is mode 0700 owned by the app's UID) and SELinux
 * per-app categories. Abstract sockets are deliberately not supported —
 * they have no DAC and are reachable by other apps under the default
 * Android SELinux policy for untrusted_app.
 */
class UnixDomainSocketFactory(socketPath: String) : SocketFactory() {

    private val address: AFUNIXSocketAddress = AFUNIXSocketAddress.of(File(socketPath))

    private fun connect(): Socket = AFUNIXSocket.connectTo(address)

    override fun createSocket(): Socket = connect()
    override fun createSocket(host: String, port: Int): Socket = connect()
    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket = connect()
    override fun createSocket(host: InetAddress, port: Int): Socket = connect()
    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket = connect()
}
