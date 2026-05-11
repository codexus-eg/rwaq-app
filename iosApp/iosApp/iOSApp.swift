import SwiftUI
import FirebaseMessaging
import FirebaseAnalytics
import FirebaseCore
import ComposeApp
import FirebaseDynamicLinks
import PaymobSDK

 

class AppDelegate: NSObject, UIApplicationDelegate {

  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
      FirebaseApp.configure()
      AppInitializer.shared.onApplicationStart()

      
    return true
  }

// Handle Universal Links and Deferred Dynamic Links
    func application(_ application: UIApplication,
                     continue userActivity: NSUserActivity,
                     restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {

        if let incomingURL = userActivity.webpageURL {

            // Let Firebase handle the deferred dynamic link first
            let linkHandled = DynamicLinks.dynamicLinks().handleUniversalLink(incomingURL) { (dynamicLink, error) in
                if let error = error {
                    print("Dynamic Link Error: \(error.localizedDescription)")
                    return
                }
                if let dynamicLink = dynamicLink {
                    self.processReferralLink(dynamicLink.url)
                }
            }

            // If Firebase didn't handle it, fallback to your standard deep link logic
            if !linkHandled {
                print("Universal Link opened directly:", incomingURL)
                processReferralLink(incomingURL)
            }
            return true
        }
        return false
    }

    // Fallback for custom schemes (older iOS versions)
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        if let dynamicLink = DynamicLinks.dynamicLinks().dynamicLink(fromCustomSchemeURL: url) {
            self.processReferralLink(dynamicLink.url)
            return true
        }
        return false
    }

    // Extract the userId and store it for Kotlin to read
    private func processReferralLink(_ url: URL?) {
        guard let url = url else { return }

        // Example URL: https://rwaq-b04b189d2e97.herokuapp.com/redirect?userId=refer123
        if let components = URLComponents(url: url, resolvingAgainstBaseURL: false),
           let queryItems = components.queryItems {

            if let userIdItem = queryItems.first(where: { $0.name == "userId" }),
               let userId = userIdItem.value {
                  print("Stored Referral ID: \(userId)")
                // Check if it starts with "refer"
                if userId.hasPrefix("refer") {
                    print("Stored Referral ID: \(userId)")
                    // Store in UserDefaults so actual class ReferralHelper can read it
                    UserDefaults.standard.set(userId, forKey: "pending_referral_id")
                }
            }
        }
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }


    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
        NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
        return UIBackgroundFetchResult.newData
    }

}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    private let paymobAdapter = PaymobLauncherAdapter()
    init() {
            // Must happen before any Composable runs
            PaymobRegistry.shared.launcher = paymobAdapter
        }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
