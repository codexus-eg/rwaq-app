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
      NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(
          showPushNotification: true,
          askNotificationPermissionOnStart: true,
          notificationSoundName: nil
      )
      )
      
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

    // Extract the referral code from the invite link and store it for the Kotlin
    // ReferralHelper to read after login.
    //
    // Example URL: https://rwaq-b04b189d2e97.herokuapp.com/invite?ref=ABC123
    // We read the `ref` query param (current scheme), falling back to `userId`
    // for any legacy links still in the wild. The value is stored under
    // "pending_referral_code"; ReferralHelper picks it up and clears it.
    private func processReferralLink(_ url: URL?) {
        guard let url = url else { return }

        guard let components = URLComponents(url: url, resolvingAgainstBaseURL: false),
              let queryItems = components.queryItems else { return }

        let code = queryItems.first(where: { $0.name == "ref" })?.value
            ?? queryItems.first(where: { $0.name == "userId" })?.value

        if let code = code, !code.isEmpty {
            print("Stored Referral Code: \(code)")
            UserDefaults.standard.set(code, forKey: "pending_referral_code")
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
