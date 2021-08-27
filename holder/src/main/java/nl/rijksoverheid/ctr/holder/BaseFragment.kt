package nl.rijksoverheid.ctr.holder

import androidx.fragment.app.Fragment
import nl.rijksoverheid.ctr.shared.models.NetworkRequestResult
import nl.rijksoverheid.ctr.design.fragments.ErrorResultFragment
import nl.rijksoverheid.ctr.design.utils.DialogUtil
import nl.rijksoverheid.ctr.shared.factories.ErrorCodeStringFactory
import nl.rijksoverheid.ctr.shared.models.ErrorResultFragmentData
import nl.rijksoverheid.ctr.shared.models.Flow
import nl.rijksoverheid.ctr.shared.ext.findNavControllerSafety
import nl.rijksoverheid.ctr.shared.models.ErrorResult
import org.koin.android.ext.android.inject

/**
 * BaseFragment that should be used by all fragments that require error handling
 */
abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    private val errorCodeStringFactory: ErrorCodeStringFactory by inject()
    private val dialogUtil: DialogUtil by inject()

    abstract fun getFlow(): Flow

    fun presentError(errorResult: ErrorResult) {
        if (errorResult is NetworkRequestResult.Failed.NetworkError<*>) {
            dialogUtil.presentDialog(
                context = requireContext(),
                title = R.string.dialog_no_internet_connection_title,
                message = getString(R.string.dialog_no_internet_connection_description),
                positiveButtonText = R.string.dialog_close,
                positiveButtonCallback = {}
            )
        } else {
            if (errorResult is NetworkRequestResult.Failed.CoronaCheckHttpError<*> && errorResult.e.code() == 429) {
                // On HTTP 429 we make an exception and show a too busy screen
                presentError(
                    data = ErrorResultFragmentData(
                        title = getString(R.string.error_too_busy_title),
                        description = getString(R.string.error_too_busy_description),
                        buttonTitle = getString(R.string.back_to_overview),
                        buttonDestinationId = R.id.action_my_overview
                    )
                )
            } else {
                val errorCodeString = errorCodeStringFactory.get(
                    flow = getFlow(),
                    errorResult = errorResult
                )

                val errorDescription = if (errorResult is NetworkRequestResult.Failed.CoronaCheckHttpError<*>) {
                    getString(R.string.error_something_went_wrong_http_error_description, errorCodeString)
                } else {
                    getString(R.string.error_something_went_wrong_making_proof_description, errorCodeString)
                }

                val data = ErrorResultFragmentData(
                    title = getString(R.string.error_something_went_wrong_title),
                    description = errorDescription,
                    urlData = ErrorResultFragmentData.UrlData(
                        urlButtonTitle = getString(R.string.error_something_went_wrong_outage_button),
                        urlButtonUrl = getString(R.string.error_something_went_wrong_outage_button_url)
                    ),
                    buttonTitle = getString(R.string.back_to_overview),
                    buttonDestinationId = R.id.action_my_overview
                )
                presentError(data)
            }
        }
    }

    fun presentError(data: ErrorResultFragmentData) {
        findNavControllerSafety()?.navigate(R.id.action_error_result, ErrorResultFragment.getBundle(data))
    }
}