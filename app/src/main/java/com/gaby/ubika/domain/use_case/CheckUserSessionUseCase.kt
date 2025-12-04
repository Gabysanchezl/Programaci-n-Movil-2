package com.gaby.ubika.domain.use_case

import com.gaby.ubika.data.firebase.AuthService


class CheckUserSessionUseCase {
    operator fun invoke(): Boolean {
        return AuthService.isLoggedIn()
    }
}
