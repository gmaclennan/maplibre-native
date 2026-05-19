import { NativeModules, Platform } from 'react-native';

interface MapServerNative {
  configure(filename: string): Promise<string>;
  reset(): Promise<void>;
}

const MapServer: MapServerNative | undefined = NativeModules.MapServer;

/**
 * Route all MapLibre HTTP requests through a Unix domain socket inside
 * the app's private filesDir. Call this once at app startup, BEFORE
 * mounting any <MapView>.
 *
 * @param filename Bare filename (no slashes). Resolved against filesDir.
 * @returns The absolute path to the socket — hand this to the Node
 *   server so it binds to the same location.
 */
export async function configureMapServer(filename: string): Promise<string> {
  if (Platform.OS !== 'android') {
    throw new Error('configureMapServer: Android-only in this build');
  }
  if (!MapServer) {
    throw new Error('MapServer native module not linked — add MapServerPackage to MainApplication');
  }
  return MapServer.configure(filename);
}

export async function resetMapServer(): Promise<void> {
  if (Platform.OS !== 'android' || !MapServer) return;
  await MapServer.reset();
}
