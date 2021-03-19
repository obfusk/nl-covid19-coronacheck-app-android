package nl.rijksoverheid.ctr.introduction.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import nl.rijksoverheid.ctr.introduction.R
import nl.rijksoverheid.ctr.introduction.databinding.FragmentOnboardingItemBinding
import nl.rijksoverheid.ctr.introduction.onboarding.models.OnboardingItem
import nl.rijksoverheid.ctr.shared.ext.fromHtml

/*
 *  Copyright (c) 2021 De Staat der Nederlanden, Ministerie van Volksgezondheid, Welzijn en Sport.
 *   Licensed under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 *
 *   SPDX-License-Identifier: EUPL-1.2
 *
 */
class OnboardingItemFragment : Fragment(R.layout.fragment_onboarding_item) {

    companion object {

        private const val EXTRA_ONBOARDING_ITEM = "EXTRA_ONBOARDING_ITEM"

        fun getInstance(onboardingItem: OnboardingItem): OnboardingItemFragment {
            val fragment =
                OnboardingItemFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_ONBOARDING_ITEM, onboardingItem)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentOnboardingItemBinding
    private val item: OnboardingItem by lazy {
        arguments?.getParcelable<OnboardingItem>(
            EXTRA_ONBOARDING_ITEM
        ) ?: throw Exception("Failed to get item")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentOnboardingItemBinding.bind(view)

        binding.title.text = getString(item.titleResource)
        binding.description.text = item.description.fromHtml()
        if (item.imageResource != 0) {
            binding.image.setImageResource(item.imageResource)
        }
    }
}
