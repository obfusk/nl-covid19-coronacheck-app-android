package nl.rijksoverheid.ctr.modules

import com.goterl.lazycode.lazysodium.LazySodiumAndroid
import com.goterl.lazycode.lazysodium.SodiumAndroid
import com.goterl.lazycode.lazysodium.utils.Base64MessageEncoder
import com.squareup.moshi.Moshi
import nl.rijksoverheid.ctr.citizen.CitizenViewModel
import nl.rijksoverheid.ctr.data.api.TestApiClient
import nl.rijksoverheid.ctr.qrcode.QrCodeTools
import nl.rijksoverheid.ctr.qrcode.ZxingQrCodeTools
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/*
 *  Copyright (c) 2021 De Staat der Nederlanden, Ministerie van Volksgezondheid, Welzijn en Sport.
 *   Licensed under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 *
 *   SPDX-License-Identifier: EUPL-1.2
 *
 */
val appModule = module {

    single {
        val retroFit = Retrofit.Builder()
            .baseUrl("https://api-ct.bananenhalen.nl")
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
        retroFit.create(TestApiClient::class.java)
    }
    single { Moshi.Builder().build() }
    single { LazySodiumAndroid(SodiumAndroid(), Base64MessageEncoder()) }
    single<QrCodeTools> { ZxingQrCodeTools() }

    // ViewModels
    viewModel { CitizenViewModel(get(), get(), get()) }
}