package com.acoding.hospital.helpers

fun String.getValueBeforeSlash(): String {
    return this.substringBefore("/")
}

fun String.getValueAfterSlash(): String {
    return this.substringAfter("/")
}