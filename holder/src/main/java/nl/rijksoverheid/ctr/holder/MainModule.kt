package nl.rijksoverheid.ctr.holder

import androidx.preference.PreferenceManager
import nl.rijksoverheid.ctr.holder.digid.DigiDViewModel
import nl.rijksoverheid.ctr.holder.myoverview.LocalTestResultViewModel
import nl.rijksoverheid.ctr.holder.myoverview.LocalTestResultViewModelImpl
import nl.rijksoverheid.ctr.holder.myoverview.QrCodeViewModel
import nl.rijksoverheid.ctr.holder.myoverview.TestResultsViewModel
import nl.rijksoverheid.ctr.holder.persistence.PersistenceManager
import nl.rijksoverheid.ctr.holder.persistence.SharedPreferencesPersistenceManager
import nl.rijksoverheid.ctr.holder.repositories.*
import nl.rijksoverheid.ctr.holder.usecase.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/*
 *  Copyright (c) 2021 De Staat der Nederlanden, Ministerie van Volksgezondheid, Welzijn en Sport.
 *   Licensed under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 *
 *   SPDX-License-Identifier: EUPL-1.2
 *
 */
val mainModule = module {

    single<PersistenceManager> {
        SharedPreferencesPersistenceManager(
            PreferenceManager.getDefaultSharedPreferences(
                androidContext(),
            )
        )
    }

    // Use cases
    single {
        GenerateHolderQrCodeUseCase(
            get()
        )
    }
    factory<QrCodeUseCase> {
        QrCodeUseCaseImpl(
            get(),
            get(),
            get()
        )
    }
    factory<SecretKeyUseCase> {
        SecretKeyUseCaseImpl(get())
    }
    factory<CommitmentMessageUseCase> {
        CommitmentMessageUseCaseImpl(get())
    }
    factory<TestProviderUseCase> {
        TestProviderUseCaseImpl(get())
    }
    single {
        TestResultUseCase(get(), get(), get(), get(), get(), get())
    }
    factory<LocalTestResultUseCase> {
        LocalTestResultUseCaseImpl(get(), get(), get(), get())
    }
    factory<TestResultAttributesUseCase> {
        TestResultAttributesUseCaseImpl(get())
    }

    // ViewModels
    viewModel { QrCodeViewModel(get()) }
    viewModel<LocalTestResultViewModel> { LocalTestResultViewModelImpl(get(), get()) }
    viewModel { DigiDViewModel(get()) }
    viewModel { TestResultsViewModel(get(), get(), get()) }

    // Repositories
    single { AuthenticationRepository() }
    factory<CoronaCheckRepository> {
        CoronaCheckRepositoryImpl(
            get(),
            get(named("ResponseError"))
        )
    }
    factory<TestProviderRepository> {
        TestProviderRepositoryImpl(
            get(),
            get(named("SignedResponseWithModel"))
        )
    }
    factory<CreateCredentialUseCase> {
        CreateCredentialUseCaseImpl()
    }
}
