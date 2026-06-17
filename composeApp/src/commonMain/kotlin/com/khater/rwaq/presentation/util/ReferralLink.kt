package com.khater.rwaq.presentation.util

// Public base URL the referral invite links point at. This must be a publicly
// reachable host (NOT the LAN dev API base) because the link is opened from
// other people's phones. It targets the backend `/invite` redirect, which opens
// the app if installed and otherwise sends the user to the right store while
// preserving the referral code through installation.
const val REFERRAL_INVITE_BASE_URL = "https://rwaq-b04b189d2e97.herokuapp.com"

/**
 * Build a shareable referral link for the given code.
 *
 * Output: `https://rwaq-b04b189d2e97.herokuapp.com/invite?ref=USER123`
 *
 * The link:
 *  - opens the app directly if it is installed (App Links / Universal Links), or
 *  - redirects to the Play Store / App Store if not installed, preserving the
 *    referral code so it survives installation.
 *
 * @param storesAppLink fallback store link, returned when [referralCode] is blank
 *   so sharing still does something useful for users without a code yet.
 */
fun generateReferralLink(
    storesAppLink: String,
    referralCode: String
): String {
    val code = referralCode.trim()
    if (code.isEmpty()) return storesAppLink
    return "$REFERRAL_INVITE_BASE_URL/invite?ref=$code"
}

/**
 * Extract a referral code from a raw deep-link / install-referrer payload.
 *
 * Handles the shapes the platforms hand us:
 *  - `ref=ABC123`              (current scheme: Play Store referrer / universal link query)
 *  - `userId=referXYZ`         (legacy scheme, kept for backward compatibility)
 *  - a full URL or query string containing one of the above
 *  - a bare code `ABC123`
 *
 * Returns null when nothing code-like can be found.
 */
fun parseReferralCode(raw: String?): String? {
    if (raw.isNullOrBlank()) return null
    val trimmed = raw.trim()

    // Pull the query portion if a full URL was passed; otherwise scan the whole
    // string for key=value pairs separated by & or ;.
    val queryPart = trimmed.substringAfter('?', trimmed)
    val keys = setOf("ref", "referralcode", "code", "userid")
    queryPart.split('&', ';').forEach { pair ->
        val key = pair.substringBefore('=', "").trim().lowercase()
        val value = pair.substringAfter('=', "").trim()
        if (key in keys && value.isNotEmpty()) {
            return value
        }
    }

    // No key=value found — accept a bare token that isn't a URL/structured string.
    if (!trimmed.contains('=') && !trimmed.contains('/') && !trimmed.contains('?')) {
        return trimmed
    }
    return null
}
