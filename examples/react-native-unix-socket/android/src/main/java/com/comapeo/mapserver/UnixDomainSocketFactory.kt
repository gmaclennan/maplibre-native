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
 * Pass either a filesystem path ("/data/data/com.comapeo/files/map.sock")
 * or an abstract socket name prefixed with "@" ("@comapeo-map").
 * Abstract sockets are Linux-only and live outside the filesystem, which
 * makes them a good fit for app-scoped IPC on Android.
 */
class UnixDomainSocketFactory(socketPath: String) : SocketFactory() {

    private val address: AFUNIXSocketAddress = if (socketPath.startsWith("@")) {
        AFUNIXSocketAddress.inAbstractNamespace(socketPath.substring(1))
    } else {
        AFUNIXSocketAddress.of(File(socketPath))
    }

    private fun connect(): Socket = AFUNIXSocket.connectTo(address)

    override fun createSocket(): Socket = connect()
    override fun createSocket(host: String, port: Int): Socket = connect()
    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket = connect()
    override fun createSocket(host: InetAddress, port: Int): Socket = connect()
    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket = connect()
}
