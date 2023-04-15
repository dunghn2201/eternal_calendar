package com.dunghn2201.eternalcalendar.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dunghn2201.eternalcalendar.util.NetworkWrapper
import io.sentry.Sentry
import kotlinx.coroutines.*
import timber.log.Timber

open class BaseViewModel(val application: Application) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        Timber.e(e)
        Sentry.captureException(e)
    }

    protected fun launchCoroutine(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        return viewModelScope.launch(coroutineExceptionHandler + dispatcher) {
            block()
        }
    }

    fun isNetworkAvailable() = NetworkWrapper.isNetworkAvailable(application)
}
