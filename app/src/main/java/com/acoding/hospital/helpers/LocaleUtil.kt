package com.acoding.hospital.helpers

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

object LocaleHelper {

    /**
     * Sets the application's locale to the specified language.
     *
     * @param context The current context.
     * @param language The language code to switch to, defaults to "en" (English).
     * @return A Context configured with the new locale.
     */
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }

        return context.createConfigurationContext(configuration)
    }

    private fun isRtlLanguage(language: String): Boolean {
        val rtlLanguages = listOf("ar") // Add more RTL languages if needed
        return rtlLanguages.contains(language)
    }

}

/**
 * Logs the current locale for debugging.
 *
 * @param context The current context.
 */
fun getCurrentLocale(context: Context): String {
    return context.resources.configuration.locales[0].language
}

/**
 * Updates the application's locale and applies it to the current context.
 *
 * @param context The current context.
 * @param selectedLocale The locale code to apply (e.g., "en", "fr", "es").
 */
fun updateLocale(context: Context, selectedLocale: String) {
    val updatedContext = LocaleHelper.setLocale(context, selectedLocale)
    context.resources.updateConfiguration(
        updatedContext.resources.configuration,
        updatedContext.resources.displayMetrics
    )
}

// Define a CompositionLocal to hold the app's Configuration
val LocalAppConfiguration = staticCompositionLocalOf<Configuration> {
    error("No Configuration provided")
}

// Helper function to update the locale and layout direction
fun updateConfiguration(context: Context, language: String, rtl: Boolean = false): Configuration {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val configuration = Configuration(context.resources.configuration)
    configuration.setLocale(locale)
    configuration.setLayoutDirection(locale)

    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    return configuration
}