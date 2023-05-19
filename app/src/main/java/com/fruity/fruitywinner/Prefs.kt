package com.fruity.fruitywinner

import android.content.Context
import android.content.SharedPreferences
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec



class Prefs(context: MainActivity) {
    private val customKey = Secrets().getKxtHdnCustomKey(BuildConfig.APPLICATION_ID)
    private val customVector = Secrets().getKxtHdnCustomVector(BuildConfig.APPLICATION_ID)

    val NAMING_KEY = "naming_key" + "_${BuildConfig.APPLICATION_ID}"
    val PUSH_KEY = "onesignal_key" + "_${BuildConfig.APPLICATION_ID}"
    val AD_ID_KEY = "advertiser_id_key" + "_${BuildConfig.APPLICATION_ID}"
    val LINK_KEY = "offer_key" + "_${BuildConfig.APPLICATION_ID}"
    val DEEP_KEY = "deeplink_key" + "_${BuildConfig.APPLICATION_ID}"
    val FINAL_KEY = "final_ky" + "_${BuildConfig.APPLICATION_ID}"
    val REF_KEY = "refferer_key" + "_${BuildConfig.APPLICATION_ID}"
    val USER_KEY = "is_user_key" + "_${BuildConfig.APPLICATION_ID}"
    val CHECKER_KEY = "is_checker" + "_${BuildConfig.APPLICATION_ID}"
    val CHECK_KEY = "is_checker_chek" + "_${BuildConfig.APPLICATION_ID}"

    private val preferences: SharedPreferences =
        context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    var advertiser: String
        get() = decrypt(preferences.getString(AD_ID_KEY, null) ?: "", customKey, customVector)
        set(value) = preferences.edit()
            .putString(AD_ID_KEY, encrypt(value, customKey, customVector)).apply()

    var organic: String
        get() = decrypt(preferences.getString(DEEP_KEY, null) ?: "", customKey, customVector)
        set(value) = preferences.edit()
            .putString(DEEP_KEY, encrypt(value, customKey, customVector)).apply()
    var lk: String
        get() = decrypt(preferences.getString(LINK_KEY, null) ?: "", customKey, customVector)
        set(value) = preferences.edit()
            .putString(LINK_KEY, encrypt(value, customKey, customVector)).apply()

    var onesignal: String
        get() = decrypt(preferences.getString(PUSH_KEY, null) ?: "", customKey, customVector)
        set(value) = preferences.edit()
            .putString(PUSH_KEY, encrypt(value, customKey, customVector)).apply()

    var finalLink: String
        get() = decrypt(preferences.getString(FINAL_KEY, null) ?: "", customKey, customVector)
        set(value) = preferences.edit()
            .putString(FINAL_KEY, encrypt(value, customKey, customVector)).apply()

    var apps: String
        get() = decrypt(preferences.getString(REF_KEY, null) ?: "", customKey, customVector)
        set(value) = preferences.edit()
            .putString(REF_KEY, encrypt(value, customKey, customVector)).apply()

    var dom: String
        get() = decrypt(preferences.getString(NAMING_KEY, null) ?: "", customKey, customVector)
        set(value) = preferences.edit()
            .putString(NAMING_KEY, encrypt(value, customKey, customVector)).apply()

    var user: Boolean
        get() = preferences.getBoolean(USER_KEY, false)
        set(value) = preferences.edit().putBoolean(USER_KEY, value).apply()
    var checkChecker: Boolean
        get() = preferences.getBoolean(CHECK_KEY, false)
        set(value) = preferences.edit().putBoolean(CHECK_KEY, value).apply()
    var checkLoc: Boolean
        get() = preferences.getBoolean(CHECKER_KEY, false)
        set(value) = preferences.edit().putBoolean(CHECKER_KEY, value).apply()

    var isAdIdSaved: Boolean
        get() = preferences.contains(AD_ID_KEY)
        set(value) = preferences.edit().putBoolean(AD_ID_KEY, value).apply()

    fun encrypt(data: String, key: String, ivs: String): String {
        val keySpec = SecretKeySpec(
            key.toByteArray(), Secrets().getKxtHdnKxtHdnCryptoSpecific(
                BuildConfig.APPLICATION_ID
            )
        )
        val iv = IvParameterSpec(ivs.toByteArray())
        val ciph = Cipher.getInstance(
            Secrets().getKxtHdnCryptoTransform(
                BuildConfig.APPLICATION_ID
            )
        )
        ciph.init(Cipher.ENCRYPT_MODE, keySpec, iv)
        val crypted = ciph.doFinal(data.toByteArray())
        val encodB = Base64.getUrlEncoder().withoutPadding().encode(crypted)
        return encodB.toString(Charsets.UTF_8)
    }

    fun decrypt(data: String, key: String, ivs: String): String {
        return try {
            val iv = IvParameterSpec(ivs.toByteArray())
            val decodB: ByteArray = Base64.getUrlDecoder().decode(data)
            val keySpec = SecretKeySpec(
                key.toByteArray(), Secrets().getKxtHdnKxtHdnCryptoSpecific(
                    BuildConfig.APPLICATION_ID
                )
            )
            val cipher = Cipher.getInstance(
                Secrets().getKxtHdnCryptoTransform(
                    BuildConfig.APPLICATION_ID
                )
            )
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
            val output = cipher.doFinal(decodB)
            return String(output)
        } catch (e: Exception) {
            ""
        }
    }
}