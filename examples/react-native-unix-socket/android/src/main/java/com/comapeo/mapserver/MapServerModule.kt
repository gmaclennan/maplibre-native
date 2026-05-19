package com.comapeo.mapserver

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import okhttp3.Dns
import okhttp3.OkHttpClient
import org.maplibre.android.module.http.HttpRequestImpl
import java.net.InetAddress
import java.util.concurrent.TimeUnit

class MapServerModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName() = NAME

    @ReactMethod
    fun configure(socketPath: String, promise: Promise) {
        try {
            val client = OkHttpClient.Builder()
                .socketFactory(UnixDomainSocketFactory(socketPath))
                // OkHttp still resolves the host; force it to a loopback literal
                // so DNS never touches the network. The address is unused — our
                // SocketFactory ignores host/port and always dials the UDS.
                .dns(Dns { _ -> listOf(InetAddress.getByAddress(byteArrayOf(127, 0, 0, 1))) })
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            HttpRequestImpl.setOkHttpClient(client)
            promise.resolve(null)
        } catch (t: Throwable) {
            promise.reject("E_MAPSERVER_CONFIGURE", t)
        }
    }

    @ReactMethod
    fun reset(promise: Promise) {
        try {
            HttpRequestImpl.setOkHttpClient(null)
            promise.resolve(null)
        } catch (t: Throwable) {
            promise.reject("E_MAPSERVER_RESET", t)
        }
    }

    companion object {
        const val NAME = "MapServer"
    }
}
