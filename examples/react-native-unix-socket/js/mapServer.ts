import { NativeModules, Platform } from 'react-native';

interface MapServerNative {
  configure(socketPath: string): Promise<void>;
  reset(): Promise<void>;
}

const MapServer: MapServerNative | undefined = NativeModules.MapServer;

/**
 * Route all MapLibre HTTP requests through a Unix domain socket.
 * Call this once at app startup, BEFORE mounting any <MapView>.
 *
 * @param socketPath Filesystem path, or "@name" for an abstract socket.
 */
export async function configureMapServer(socketPath: string): Promise<void> {
  if (Platform.OS !== 'android') {
    throw new Error('configureMapServer: Android-only in this build');
  }
  if (!MapServer) {
    throw new Error('MapServer native module not linked — add MapServerPackage to MainApplication');
  }
  await MapServer.configure(socketPath);
}

export async function resetMapServer(): Promise<void> {
  if (Platform.OS !== 'android' || !MapServer) return;
  await MapServer.reset();
}
