package com.sachin.goodsam



import android.app.Application
import com.sachin.goodsam.di.AppContainer

class GoodSamApp : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}