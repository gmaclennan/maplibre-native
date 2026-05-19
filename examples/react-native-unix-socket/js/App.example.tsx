import React, { useEffect, useState } from 'react';
import { View, Text } from 'react-native';
import MapLibreGL, { MapView, ShapeSource } from '@maplibre/maplibre-react-native';
import { configureMapServer } from './mapServer';

// The host/port don't matter — our SocketFactory ignores them and always
// dials the UDS. Pick anything; "127.0.0.1" keeps OkHttp happy.
const STYLE_URL = 'http://127.0.0.1/styles/default.json';

export default function App() {
  const [ready, setReady] = useState(false);

  useEffect(() => {
    (async () => {
      // Abstract socket — UID-scoped, no filesystem entry.
      await configureMapServer('@comapeo-map');
      setReady(true);
    })().catch(console.error);
  }, []);

  if (!ready) {
    return (
      <View><Text>Wiring map server…</Text></View>
    );
  }

  return (
    <MapView style={{ flex: 1 }} styleURL={STYLE_URL} />
  );
}
