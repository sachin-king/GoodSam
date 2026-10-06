# GoodSam Assignment

Android app (Kotlin, XML) built with Clean Architecture
(data / domain / presentation).

Flow: Splash (device API) -> Login (signin API) -> OTP (checkotp API) -> User list (dummyjson).

Stack: Kotlin, MVVM, Coroutines + StateFlow, Retrofit, Coil, ViewBinding.

Notes:
- API 3 uses otpPass = "1" and uniqueId = "0" as clarified by the team.
- Server error messages are shown as Toasts.
