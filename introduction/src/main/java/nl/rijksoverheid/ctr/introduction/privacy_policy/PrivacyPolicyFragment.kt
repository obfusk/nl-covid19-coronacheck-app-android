package nl.rijksoverheid.ctr.introduction.privacy_policy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import nl.rijksoverheid.ctr.introduction.BuildConfig
import nl.rijksoverheid.ctr.introduction.CoronaCheckApp
import nl.rijksoverheid.ctr.introduction.IntroductionViewModel
import nl.rijksoverheid.ctr.introduction.databinding.FragmentPrivacyPolicyBinding
import nl.rijksoverheid.ctr.shared.ext.fromHtml
import nl.rijksoverheid.ctr.shared.ext.launchUrl
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
 *  Copyright (c) 2021 De Staat der Nederlanden, Ministerie van Volksgezondheid, Welzijn en Sport.
 *   Licensed under the EUROPEAN UNION PUBLIC LICENCE v. 1.2
 *
 *   SPDX-License-Identifier: EUPL-1.2
 *
 */
class PrivacyPolicyFragment : Fragment() {

    private val introductionData by lazy { (requireActivity().application as CoronaCheckApp).getIntroductionData() }
    private val introductionViewModel: IntroductionViewModel by viewModel()
    private lateinit var binding: FragmentPrivacyPolicyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivacyPolicyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            val canPop = findNavController().popBackStack()
            if (!canPop) {
                requireActivity().finish()
            }
        }

        binding.description.text =
            getString(introductionData.privacyPolicyStringResource).fromHtml()
        binding.description.setOnClickListener {
            BuildConfig.URL_PRIVACY_STATEMENT.launchUrl(requireContext())
        }
        binding.checkbox.text =
            getString(introductionData.privacyPolicyCheckboxStringResource)
        binding.button.text = getString(introductionData.onboardingNextButtonStringResource)

        val adapterItems = introductionData.privacyPolicyItems.map {
            PrivacyPolicyAdapterItem(
                it
            )
        }

        val adapter = GroupieAdapter()
        val section = Section()
        binding.items.adapter = adapter
        adapter.add(section)
        section.update(adapterItems)

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            binding.button.isEnabled = isChecked
        }

        binding.button.setOnClickListener {
            introductionViewModel.saveIntroductionFinished()
            introductionData.introductionDoneCallback.invoke(this)
        }
    }
}