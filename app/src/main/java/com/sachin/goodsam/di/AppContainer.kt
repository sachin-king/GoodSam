package com.sachin.goodsam.di

import android.content.Context
import com.sachin.goodsam.data.DeviceInfoProvider
import com.sachin.goodsam.data.remote.ApiService
import com.sachin.goodsam.data.repository.AuthRepositoryImpl
import com.sachin.goodsam.data.repository.UserRepositoryImpl
import com.sachin.goodsam.domain.repository.AuthRepository
import com.sachin.goodsam.domain.repository.UserRepository
import com.sachin.goodsam.domain.usecase.GetUsersUseCase
import com.sachin.goodsam.domain.usecase.RegisterDeviceUseCase
import com.sachin.goodsam.domain.usecase.SignInUseCase
import com.sachin.goodsam.domain.usecase.VerifyOtpUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class AppContainer(context: Context) {



    private val client = OkHttpClient.Builder()
        .cookieJar(object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookieStore[url.host] = cookies
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> =
                cookieStore[url.host] ?: emptyList()
        })
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    private val api: ApiService = Retrofit.Builder()
        .baseUrl("http://210.212.165.119/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    private val authRepository: AuthRepository =
        AuthRepositoryImpl(api, DeviceInfoProvider(context.applicationContext))
    private val userRepository: UserRepository = UserRepositoryImpl(api)

    val registerDeviceUseCase = RegisterDeviceUseCase(authRepository)
    val signInUseCase = SignInUseCase(authRepository)
    val verifyOtpUseCase = VerifyOtpUseCase(authRepository)
    val getUsersUseCase = GetUsersUseCase(userRepository)


    private val cookieStore = mutableMapOf<String, List<Cookie>>()


}