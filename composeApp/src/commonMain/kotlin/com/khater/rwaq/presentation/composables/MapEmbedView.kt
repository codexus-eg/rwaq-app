package com.khater.rwaq.presentation.composables

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData

@Composable
fun MapEmbedView(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    // 1. Initial HTML state (only created once)
    val html = remember {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                body { padding: 0; margin: 0; }
                html, body, #map { height: 100%; width: 100%; background: #f0f0f0; }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script>
                // Map setup using the first passed coordinates
                var map = L.map('map').setView([$latitude, $longitude], 15);
                
                L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19,
                    attribution: '© OpenStreetMap'
                }).addTo(map);
                
                var marker = L.marker([$latitude, $longitude]).addTo(map);

                // This function is called from Kotlin via evaluateJavaScript
                function updateLocation(lat, lng) {
                    var newLatLng = new L.LatLng(lat, lng);
                    marker.setLatLng(newLatLng);
                    map.panTo(newLatLng, { animate: true, duration: 0.5 });
                }
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    val webViewState = rememberWebViewStateWithHTMLData(data = html)
    val navigator = rememberWebViewNavigator()

    // 2. Settings configuration
    DisposableEffect(Unit) {
        webViewState.webSettings.apply {
            isJavaScriptEnabled = true
            androidWebSettings.apply {
                isAlgorithmicDarkeningAllowed = true
                safeBrowsingEnabled = true
            }
        }
        onDispose { }
    }

    // 3. Dynamic Marker Update (The key to preventing reloads)
    LaunchedEffect(latitude, longitude) {
        // We check if the function exists in JS before calling to avoid errors during initial load
        val jsCommand = "if (typeof updateLocation === 'function') { updateLocation($latitude, $longitude); }"
        navigator.evaluateJavaScript(jsCommand)
    }

    WebView(
        state = webViewState,
        navigator = navigator,
        modifier = modifier.fillMaxWidth().fillMaxHeight(0.5f)
    )
}