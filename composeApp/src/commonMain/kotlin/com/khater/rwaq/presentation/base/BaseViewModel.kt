package com.khater.rwaq.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<SCREEN_STATE, SCREEN_EFFECT>(
    initialState: SCREEN_STATE,
) : ViewModel() {
    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()
    protected val _effect = MutableSharedFlow<SCREEN_EFFECT>()
    val effect = _effect.asSharedFlow()

protected val currentState: SCREEN_STATE
    get() = _state.value

    protected fun <T> tryToExecute(
        callee: suspend () -> T,
        onSuccess: (suspend (T) -> Unit),
        onError: (suspend (Throwable) -> Unit),
        onStart: (suspend () -> Unit)? = null,
        onFinish: (suspend () -> Unit)? = null,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        inScope: CoroutineScope = viewModelScope
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            inScope.launch {
                onError(throwable)
                onFinish?.invoke()
            }
        }

        return inScope.launch(dispatcher + exceptionHandler) {
            onStart?.invoke()
            onSuccess(callee())
            onFinish?.invoke()
        }
    }
    protected fun <T> tryToCollect(
        onStart: () -> Unit = {},
        collect: () -> Flow<T>,
        onCollect: suspend (T) -> Unit,
        onError: (Throwable) -> Unit = {},
        coroutineScope: CoroutineScope = viewModelScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable -> onError(throwable) }
        return coroutineScope.launch(exceptionHandler + dispatcher) {
            collect()
                .onStart { onStart() }
                .catch { onError(it) }
                .collect { onCollect(it) }
        }
    }

    protected fun updateState(updater: (SCREEN_STATE) -> SCREEN_STATE) = _state.update(updater)

    protected fun sendNewEffect(newEffect: SCREEN_EFFECT) {
        viewModelScope.launch  {
            _effect.emit(newEffect)
        }
    }
}
