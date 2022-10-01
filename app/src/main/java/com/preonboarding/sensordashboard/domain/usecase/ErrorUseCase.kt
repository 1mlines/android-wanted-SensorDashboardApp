package com.preonboarding.sensordashboard.domain.usecase

import kotlinx.coroutines.flow.MutableSharedFlow

/***
 * @Created by 서강휘 2022.09.28
 */

interface ErrorUseCase {

    fun getErrorFlow(): MutableSharedFlow<Throwable>
}