package com.gaby.ubika.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaby.ubika.domain.use_case.CheckUserSessionUseCase


class SplashViewModel(
    private val checkUserSessionUseCase: CheckUserSessionUseCase = CheckUserSessionUseCase()
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun checkSession() {
        _isLoggedIn.value = checkUserSessionUseCase()
    }
}
