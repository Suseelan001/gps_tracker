package com.example.crmmainproject.model.apiUtil.interceptor

import java.io.IOException

class NoConnectivityException: IOException() {

    override val message: String?
        get() = "No connection exception"
}