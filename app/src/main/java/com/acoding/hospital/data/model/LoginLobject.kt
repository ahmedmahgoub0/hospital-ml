package com.acoding.hospital.data.model

object LoginDataStore {
    var loginResponse: LoginResponse? = null
        private set // Makes the setter private

    var user: User? = null
        private set // Makes the setter private

    /**
     * Initialize the singleton with data.
     * This can be called after a successful login or API response.
     */
    fun initialize(loginResponse: LoginResponse) {
        this.loginResponse = loginResponse
        this.user = loginResponse.data
    }

    /**
     * Clear the data, e.g., during logout.
     */
    fun clear() {
        loginResponse = null
        user = null
    }
}
