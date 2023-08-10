package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.ProfileLoggedCounters
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.ProfileLoggedHeader
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.ProfileLoggedSection
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.SectionSelector
import com.github.diegoberaldin.raccoonforlemmy.feature.profile.di.getProfilePostsViewModel

internal class ProfilePostsScreen(
    private val modifier: Modifier = Modifier,
    private val user: UserModel,
    private val onSectionSelected: (ProfileLoggedSection) -> Unit,
) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel {
            getProfilePostsViewModel(
                user = user,
            )
        }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
            model.reduce(ProfilePostsMviModel.Intent.Refresh)
        })
        Box(
            modifier = modifier.pullRefresh(pullRefreshState),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        ProfileLoggedHeader(user = user)
                        ProfileLoggedCounters(user = user)
                        Spacer(modifier = Modifier.height(Spacing.xxs))
                        SectionSelector(
                            currentSection = ProfileLoggedSection.POSTS,
                            onSectionSelected = {
                                onSectionSelected(it)
                            },
                        )
                    }
                }
                items(uiState.posts) { post ->
                    ProfilePostCard(
                        post = post,
                        onOpenCommunity = { community ->
                            navigator.push(
                                CommunityDetailScreen(
                                    community = community,
                                    onBack = {
                                        navigator.pop()
                                    },
                                ),
                            )
                        },
                    )
                }
                item {
                    if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                        model.reduce(ProfilePostsMviModel.Intent.LoadNextPage)
                    }
                    if (uiState.loading && !uiState.refreshing) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(Spacing.xs),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(25.dp),
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = uiState.refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
