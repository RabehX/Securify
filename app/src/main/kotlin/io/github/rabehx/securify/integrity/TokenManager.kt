package io.github.rabehx.securify.integrity

import android.content.Context
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityServiceException
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.google.android.play.core.integrity.model.IntegrityErrorCode
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.rabehx.securify.BuildConfig
import io.github.rabehx.securify.R
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class TokenManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    /**
     * Request an integrity token from Google Play Integrity API
     * using nonce for round-trip validation.
     *
     * @param nonce Nonce that the server can validate after decoding the token.
     * @return The integrity token string
     */
    suspend fun getIntegrityToken(nonce: String): String {
        return try {
            val integrityManager = IntegrityManagerFactory.create(context)
            val tokenResponse = integrityManager.requestIntegrityToken(
                IntegrityTokenRequest.builder()
                    .setCloudProjectNumber(BuildConfig.CLOUD_PROJECT_NUMBER)
                    .setNonce(nonce)
                    .build()
            ).await()

            tokenResponse.token()
        } catch (e: IntegrityServiceException) {
            throw IllegalStateException(e.toUserMessage(), e)
        } catch (e: Exception) {
            val msg = e.message ?: e::class.java.simpleName
            throw IllegalStateException(context.getString(R.string.integrity_error_generic, msg), e)
        }
    }

    private fun IntegrityServiceException.toUserMessage(): String {
        val name = errorCodeName(errorCode)
        val help = errorCodeHelp(errorCode)
        return if (help.isNotBlank()) {
            context.getString(R.string.integrity_error_prefix, name, errorCode, help)
        } else {
            context.getString(R.string.integrity_error_prefix_short, name, errorCode)
        }
    }

    private fun errorCodeName(errorCode: Int): String = when (errorCode) {
        IntegrityErrorCode.NO_ERROR -> context.getString(R.string.integrity_code_no_error)
        IntegrityErrorCode.API_NOT_AVAILABLE -> context.getString(R.string.integrity_code_api_not_available)
        IntegrityErrorCode.PLAY_STORE_NOT_FOUND -> context.getString(R.string.integrity_code_play_store_not_found)
        IntegrityErrorCode.NETWORK_ERROR -> context.getString(R.string.integrity_code_network_error)
        IntegrityErrorCode.PLAY_STORE_ACCOUNT_NOT_FOUND -> context.getString(R.string.integrity_code_account_not_found)
        IntegrityErrorCode.APP_NOT_INSTALLED -> context.getString(R.string.integrity_code_app_not_installed)
        IntegrityErrorCode.PLAY_SERVICES_NOT_FOUND -> context.getString(R.string.integrity_code_play_services_not_found)
        IntegrityErrorCode.APP_UID_MISMATCH -> context.getString(R.string.integrity_code_uid_mismatch)
        IntegrityErrorCode.TOO_MANY_REQUESTS -> context.getString(R.string.integrity_code_too_many_requests)
        IntegrityErrorCode.CANNOT_BIND_TO_SERVICE -> context.getString(R.string.integrity_code_cannot_bind)
        IntegrityErrorCode.NONCE_TOO_SHORT -> context.getString(R.string.integrity_code_nonce_too_short)
        IntegrityErrorCode.NONCE_TOO_LONG -> context.getString(R.string.integrity_code_nonce_too_long)
        IntegrityErrorCode.GOOGLE_SERVER_UNAVAILABLE -> context.getString(R.string.integrity_code_server_unavailable)
        IntegrityErrorCode.NONCE_IS_NOT_BASE64 -> context.getString(R.string.integrity_code_nonce_not_base64)
        IntegrityErrorCode.PLAY_STORE_VERSION_OUTDATED -> context.getString(R.string.integrity_code_play_store_outdated)
        IntegrityErrorCode.PLAY_SERVICES_VERSION_OUTDATED -> context.getString(R.string.integrity_code_play_services_outdated)
        IntegrityErrorCode.CLOUD_PROJECT_NUMBER_IS_INVALID -> context.getString(R.string.integrity_code_cloud_project_invalid)
        IntegrityErrorCode.CLIENT_TRANSIENT_ERROR -> context.getString(R.string.integrity_code_client_transient)
        IntegrityErrorCode.INTERNAL_ERROR -> context.getString(R.string.integrity_code_internal_error)
        else -> context.getString(R.string.integrity_code_unknown)
    }

    private fun errorCodeHelp(errorCode: Int): String = when (errorCode) {
        IntegrityErrorCode.API_NOT_AVAILABLE -> context.getString(R.string.integrity_help_api_not_available)
        IntegrityErrorCode.PLAY_STORE_NOT_FOUND -> context.getString(R.string.integrity_help_play_store_not_found)
        IntegrityErrorCode.NETWORK_ERROR -> context.getString(R.string.integrity_help_network_error)
        IntegrityErrorCode.PLAY_STORE_ACCOUNT_NOT_FOUND -> context.getString(R.string.integrity_help_account_not_found)
        IntegrityErrorCode.APP_NOT_INSTALLED -> context.getString(R.string.integrity_help_app_not_installed)
        IntegrityErrorCode.PLAY_SERVICES_NOT_FOUND -> context.getString(R.string.integrity_help_play_services_not_found)
        IntegrityErrorCode.APP_UID_MISMATCH -> context.getString(R.string.integrity_help_uid_mismatch)
        IntegrityErrorCode.TOO_MANY_REQUESTS -> context.getString(R.string.integrity_help_too_many_requests)
        IntegrityErrorCode.CANNOT_BIND_TO_SERVICE -> context.getString(R.string.integrity_help_cannot_bind)
        IntegrityErrorCode.NONCE_TOO_SHORT -> context.getString(R.string.integrity_help_nonce_too_short)
        IntegrityErrorCode.NONCE_TOO_LONG -> context.getString(R.string.integrity_help_nonce_too_long)
        IntegrityErrorCode.GOOGLE_SERVER_UNAVAILABLE -> context.getString(R.string.integrity_help_server_unavailable)
        IntegrityErrorCode.NONCE_IS_NOT_BASE64 -> context.getString(R.string.integrity_help_nonce_not_base64)
        IntegrityErrorCode.PLAY_STORE_VERSION_OUTDATED -> context.getString(R.string.integrity_help_play_store_outdated)
        IntegrityErrorCode.PLAY_SERVICES_VERSION_OUTDATED -> context.getString(R.string.integrity_help_play_services_outdated)
        IntegrityErrorCode.CLOUD_PROJECT_NUMBER_IS_INVALID -> context.getString(R.string.integrity_help_cloud_project_invalid)
        IntegrityErrorCode.CLIENT_TRANSIENT_ERROR -> context.getString(R.string.integrity_help_client_transient)
        IntegrityErrorCode.INTERNAL_ERROR -> context.getString(R.string.integrity_help_internal_error)
        else -> ""
    }
}
