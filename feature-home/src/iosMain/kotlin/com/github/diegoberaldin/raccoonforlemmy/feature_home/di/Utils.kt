package com.github.diegoberaldin.raccoonforlemmy.feature_home.di

import com.github.diegoberaldin.raccoonforlemmy.feature_home.viewmodel.HomeScreenModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getHomeScreenModel() = HomeScreenModelHelper.model

object HomeScreenModelHelper : KoinComponent {
    val model: HomeScreenModel by inject()
}