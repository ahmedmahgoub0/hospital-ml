package com.acoding.hospital.data.networking

private const val BASE_URL = "https://ml-health-system-api.vercel.app/api/"

fun constructUrl(url: String): String {
    return when {
        url.contains(BASE_URL) -> url
        url.startsWith("/") -> BASE_URL + url.drop(1)
        else -> BASE_URL + url
    }
}