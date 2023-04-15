package com.dunghn2201.eternalcalendar.util.extension

import io.sentry.Sentry
import timber.log.Timber

inline fun tryCatch(crossinline func: () -> Unit) {
    try {
        func()
    } catch (ex: Exception) {
        Timber.e(ex)
        Sentry.captureException(ex)
    }
}

interface UiState {
    val uiState: State
    override fun equals(other: Any?): Boolean
    enum class State {
        INITIAL, LOADING, COMPLETE, FAILURE,
    }
}
