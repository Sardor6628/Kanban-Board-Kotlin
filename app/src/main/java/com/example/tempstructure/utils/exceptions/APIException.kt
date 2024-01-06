package com.example.tempstructure.utils.exceptions

class APIException : Exception {

    constructor(message: String?) : super(message)
    constructor(message: String?, throwable: Throwable?) : super(message, throwable)
}