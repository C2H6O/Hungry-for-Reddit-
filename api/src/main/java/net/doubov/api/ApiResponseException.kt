package net.doubov.api

class ApiResponseException(message: String? = null, exception: Exception? = null) :
    Exception(message)