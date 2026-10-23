package com.example.system

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.widget.Toast

class BiometricAuthHelper(private val context: Context) {

  fun canAuthenticate(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      val bm = context.getSystemService(BiometricManager::class.java)
      val canAuth = bm?.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
      return canAuth == BiometricManager.BIOMETRIC_SUCCESS
    }
    return true
  }

  fun authenticate(
    activity: Activity,
    title: String = "Face ID & Passcode",
    subtitle: String = "Confirm your identity to unlock iOS Settings",
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      val cancellationSignal = CancellationSignal()
      val executor = activity.mainExecutor

      try {
        val promptBuilder = BiometricPrompt.Builder(activity)
          .setTitle(title)
          .setSubtitle(subtitle)
          .setDescription("Simulated Face ID powered by Android Secure Biometrics")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          promptBuilder.setAllowedAuthenticators(
            BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL
          )
        } else {
          promptBuilder.setNegativeButton(
            "Use Passcode",
            executor,
            DialogInterface.OnClickListener { _, _ ->
              onError("Passcode option chosen")
            }
          )
        }

        val prompt = promptBuilder.build()
        prompt.authenticate(
          cancellationSignal,
          executor,
          object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
              super.onAuthenticationSucceeded(result)
              onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
              super.onAuthenticationError(errorCode, errString)
              onError(errString?.toString() ?: "Biometric authentication cancelled")
            }

            override fun onAuthenticationFailed() {
              super.onAuthenticationFailed()
              Toast.makeText(context, "Face ID not recognized, try again", Toast.LENGTH_SHORT).show()
            }
          }
        )
      } catch (e: Exception) {
        onError("Biometric prompt error: ${e.message}")
      }
    } else {
      // Direct pass for legacy test environments
      onSuccess()
    }
  }
}
