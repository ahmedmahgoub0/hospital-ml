package com.acoding.hospital.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferencesData(
    val languageData: LanguageData = LanguageData.ENGLISH,
    val patientId: String = "0"
)

@Serializable
enum class LanguageData(val locale: String) {
    ENGLISH(locale = "en"),
    ARABIC(locale = "ar"),
}

data class UserPreferences(
    val language: Language,
    val patientId: String = "0"
)

enum class Language(val languageName: String, val locale: String) {
    ENGLISH(languageName = "English", locale = "en"),
    ARABIC(languageName = "العربية", locale = "ar")
}

fun UserPreferencesData.toDomain(): UserPreferences {
    return UserPreferences(
        language = languageData.toDomain()
    )
}


fun LanguageData.toDomain(): Language {
    return when (this) {
        LanguageData.ENGLISH -> Language.ENGLISH
        LanguageData.ARABIC -> Language.ARABIC
    }
}

fun Language.toData(): LanguageData {
    return when (this) {
        Language.ENGLISH -> LanguageData.ENGLISH
        Language.ARABIC -> LanguageData.ARABIC
    }
}