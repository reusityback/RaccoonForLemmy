package com.github.diegoberaldin.raccoonforlemmy.feature.home.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.navigation.NavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.PostListViewModel

expect fun getHomeScreenModel(): PostListViewModel
