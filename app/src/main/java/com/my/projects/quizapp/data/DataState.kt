package com.my.projects.quizapp.data


sealed class DataState {

    object Loading : DataState()
    object Success : DataState()
    data class Error(val error: Int) : DataState()
    data class NetworkException(val error: String) : DataState()
    sealed class HttpErrors : DataState() {
        data class NoResults(val exception: Int) : HttpErrors()
        data class InvalidParameter(val exception: Int) : HttpErrors()
        data class TokenNotFound(val exception: Int) : HttpErrors()
        data class TokenEmpty(val exception: Int) : HttpErrors()
    }
}