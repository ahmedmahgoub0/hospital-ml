package com.acoding.hospital.helpers

fun getValueBeforeSlash(input: String): String {
    return input.substringBefore("/")
}

fun getValueAfterSlash(input: String): String {
    return input.substringAfter("/")
}