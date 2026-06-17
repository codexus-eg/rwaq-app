package com.khater.rwaq.domain.entities.referral

// Outcome of submitting a referral code to the backend.
data class ReferralClaimResult(
    val success: Boolean,
    val pointsAwarded: Int,
    // True when the referral was already claimed earlier (a harmless no-op retry).
    val alreadyClaimed: Boolean,
    val message: String
)

// The current user's referral summary (code + share link + earned stats).
data class ReferralInfo(
    val referralCode: String,
    val referralLink: String,
    val referralPoints: Int,
    val totalReferrals: Int,
    val totalPointsEarned: Int,
    val pointsPerReferral: Int,
    val enabled: Boolean
)

// One person the current user has referred.
data class ReferralListItem(
    val userId: String,
    val username: String,
    val phone: String,
    val reward: Int,
    val status: String,
    val createdAt: String
)
