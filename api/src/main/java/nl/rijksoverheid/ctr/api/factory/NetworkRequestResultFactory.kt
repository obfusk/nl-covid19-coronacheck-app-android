package nl.rijksoverheid.ctr.api.factory

import nl.rijksoverheid.ctr.shared.models.NetworkRequestResult
import nl.rijksoverheid.ctr.shared.models.CoronaCheckErrorResponse
import nl.rijksoverheid.ctr.shared.models.Step
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * This class should be used for every network request
 *
 */
class NetworkRequestResultFactory(
    private val errorResponseBodyConverter: Converter<ResponseBody, CoronaCheckErrorResponse>) {

    suspend fun <R: Any> createResult(
        step: Step,
        provider: String ? = null,
        interceptHttpError: (suspend (e: HttpException) -> R?)? = null,
        networkCall: suspend () -> R): NetworkRequestResult<R> {
        return try {
            val response = networkCall.invoke()
            NetworkRequestResult.Success(response)
        } catch (httpException: HttpException) {
            try {
                // We intercept here if a HttpException is expected
                val result = interceptHttpError?.invoke(httpException)
                result?.let {
                    return NetworkRequestResult.Success(it)
                }

                provider?.let {
                    // If this is a call to a provider we return a ProviderHttpError
                    return NetworkRequestResult.Failed.ProviderHttpError(step, httpException, it)
                }

                // Check if there is a error body
                val errorBody = httpException.response()?.errorBody() ?: return NetworkRequestResult.Failed.CoronaCheckHttpError(step, httpException)

                // Check if the error body is a [CoronaCheckErrorResponse]
                val errorResponse = errorResponseBodyConverter.convert(errorBody) ?: return NetworkRequestResult.Failed.CoronaCheckHttpError(step, httpException)

                return NetworkRequestResult.Failed.CoronaCheckWithErrorResponseHttpError(step, httpException, errorResponse)
            } catch (e: Exception) {
                return NetworkRequestResult.Failed.CoronaCheckHttpError(step, httpException)
            }
        } catch (e: IOException) {
            when (e) {
                is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                    NetworkRequestResult.Failed.NetworkError(step, e)
                }
                else -> NetworkRequestResult.Failed.Error(step, e)
            }
        } catch (e: Exception) {
            NetworkRequestResult.Failed.Error(step, e)
        }
    }
}