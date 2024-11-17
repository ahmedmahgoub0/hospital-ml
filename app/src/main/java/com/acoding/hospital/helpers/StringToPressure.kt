package com.acoding.hospital.helpers

fun getValueBeforeSlash(input: String): String {
    return input.substringBefore("/")
}