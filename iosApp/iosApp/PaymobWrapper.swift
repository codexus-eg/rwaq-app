import Foundation
import UIKit
import PaymobSDK

@objcMembers
 class PaymobWrapper: NSObject, PaymobSDKDelegate {

    private var onSuccess: (() -> Void)?
    private var onFailure: ((String?) -> Void)?
    private var onPending: (() -> Void)?

    private let paymob = PaymobSDK()

    override init() {
        super.init()
        paymob.delegate = self
    }

    @objc func startPayment(
        clientSecret: String,
        publicKey: String,
        onSuccess: @escaping () -> Void,
        onFailure: @escaping (String?) -> Void,
        onPending: @escaping () -> Void
    ) {
        self.onSuccess = onSuccess
        self.onFailure = onFailure
        self.onPending = onPending

        guard let rootVC = UIApplication.shared.connectedScenes
            .compactMap({ ($0 as? UIWindowScene)?.keyWindow })
            .first?.rootViewController else {
                onFailure("No root view controller")
                return
        }

        do {
            paymob.paymobSDKCustomization.buttonBackgroundColor = UIColor(red: 219/255.0, green: 171/255.0, blue: 109/255.0, alpha: 1.0)
            
            if let logo = UIImage(named: "rwaq_logo") {
                paymob.paymobSDKCustomization.appIcon = logo
            } else {
                print("Image not found")
            }
            try paymob.presentPayVC(
                VC: rootVC,
                PublicKey: publicKey,
                ClientSecret: clientSecret
            )
        } catch {
            onFailure(error.localizedDescription)
        }
    }

    // MARK: - PaymobSDKDelegate

    func transactionAccepted(transactionDetails: [String : Any]) {
        onSuccess?()
    }

    func transactionRejected(message: String) {
        onFailure?(message)
    }

    func transactionPending() {
        onPending?()
    }
}
