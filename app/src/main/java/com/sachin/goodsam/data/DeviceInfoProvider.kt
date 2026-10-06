package com.sachin.goodsam.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

class DeviceInfoProvider(private val context: Context) {

    @SuppressLint("HardwareIds")
    val deviceId: String = Settings.Secure.getString(
        context.contentResolver, Settings.Secure.ANDROID_ID
    ) ?: ""

    val osVersion: String = Build.VERSION.RELEASE ?: ""
    val modelName: String = Build.MODEL ?: ""

    val appVersion: String = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0"
    } catch (e: Exception) {
        "1.0"
    }
}