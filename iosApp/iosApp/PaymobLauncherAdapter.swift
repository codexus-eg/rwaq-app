// iosApp/Payment/PaymobLauncherAdapter.swift
import Foundation
import ComposeApp

// your KMP framework
/// Bridges the Kotlin PaymobNativeLauncher protocol to your existing PaymobWrapper.
/// No PaymobSDK import needed here — PaymobWrapper owns the SDK.
final class PaymobLauncherAdapter: NSObject, PaymobNativeLauncher {

    // PaymobWrapper already holds the SDK instance and delegate — reuse it
    private let wrapper = PaymobWrapper()

    func start(
        clientSecret: String,
        publicKey: String,
        callbacks: any PaymobCallbacks
    ) {
        wrapper.startPayment(
            clientSecret: clientSecret,
            publicKey: publicKey,
            onSuccess: {
                callbacks.onSuccess()
            },
            onFailure: { message in
                callbacks.onFailure(message: message)
            },
            onPending: {
                callbacks.onPending()
            }
        )
    }
}
