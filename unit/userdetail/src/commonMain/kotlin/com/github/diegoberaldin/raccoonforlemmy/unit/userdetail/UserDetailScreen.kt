package com.github.diegoberaldin.raccoonforlemmy.unit.userdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Dimensions
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenu
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenuItem
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.ProgressHud
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SectionSelector
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SwipeAction
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SwipeActionCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.CommentCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.CommentCardPlaceholder
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.Option
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.PostCardPlaceholder
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.UserDetailSection
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.UserHeader
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.ShareBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.getScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallback
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallbackArgs
import com.github.diegoberaldin.raccoonforlemmy.core.utils.toLocalDp
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.readableName
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toIcon
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toInt
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import com.github.diegoberaldin.raccoonforlemmy.unit.chat.InboxChatScreen
import com.github.diegoberaldin.raccoonforlemmy.unit.createreport.CreateReportScreen
import com.github.diegoberaldin.raccoonforlemmy.unit.rawcontent.RawContentDialog
import com.github.diegoberaldin.raccoonforlemmy.unit.userinfo.UserInfoScreen
import com.github.diegoberaldin.raccoonforlemmy.unit.web.WebViewScreen
import com.github.diegoberaldin.raccoonforlemmy.unit.zoomableimage.ZoomableImageScreen
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class UserDetailScreen(
    private val userId: Int,
    private val otherInstance: String = "",
) : Screen {

    override val key: ScreenKey
        get() = super.key + userId.toString()

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<UserDetailMviModel>(tag = userId.toString(),
            parameters = { parametersOf(userId, otherInstance) })
        model.bindToLifecycle(key + userId.toString())
        val uiState by model.uiState.collectAsState()
        val lazyListState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val genericError = stringResource(MR.strings.message_generic_error)
        val successMessage = stringResource(MR.strings.message_operation_successful)
        val isOnOtherInstance = otherInstance.isNotEmpty()
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val notificationCenter = remember { getNotificationCenter() }
        val fabNestedScrollConnection = remember { getFabNestedScrollConnection() }
        val isFabVisible by fabNestedScrollConnection.isFabVisible.collectAsState()
        val themeRepository = remember { getThemeRepository() }
        val upVoteColor by themeRepository.upVoteColor.collectAsState()
        val downVoteColor by themeRepository.downVoteColor.collectAsState()
        val replyColor by themeRepository.replyColor.collectAsState()
        val saveColor by themeRepository.saveColor.collectAsState()
        val defaultUpvoteColor = MaterialTheme.colorScheme.primary
        val defaultReplyColor = MaterialTheme.colorScheme.secondary
        val defaultSaveColor = MaterialTheme.colorScheme.secondaryContainer
        val defaultDownVoteColor = MaterialTheme.colorScheme.tertiary
        val navigationCoordinator = remember { getNavigationCoordinator() }
        var rawContent by remember { mutableStateOf<Any?>(null) }
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val detailOpener = remember { getDetailOpener() }

        LaunchedEffect(notificationCenter) {
            notificationCenter.resetCache()
        }
        LaunchedEffect(model) {
            model.effects.onEach {
                when (it) {
                    is UserDetailMviModel.Effect.BlockError -> {
                        snackbarHostState.showSnackbar(it.message ?: genericError)
                    }

                    UserDetailMviModel.Effect.BlockSuccess -> {
                        snackbarHostState.showSnackbar(successMessage)
                    }

                    UserDetailMviModel.Effect.BackToTop -> {
                        scope.launch {
                            lazyListState.scrollToItem(0)
                        }
                    }
                }
            }.launchIn(this)
        }

        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(Spacing.xs),
            contentWindowInsets = if (settings.edgeToEdge) {
                WindowInsets(0, 0, 0, 0)
            } else {
                WindowInsets.navigationBars
            },
            topBar = {
                val userName = uiState.user.readableName
                val maxTopInset = Dimensions.topBarHeight.value.toInt()
                var topInset by remember { mutableStateOf(maxTopInset) }
                snapshotFlow { topAppBarState.collapsedFraction }.onEach {
                    topInset = (maxTopInset * (1 - it)).toInt()
                }.launchIn(scope)

                TopAppBar(
                    windowInsets = if (settings.edgeToEdge) {
                        WindowInsets(0, topInset, 0, 0)
                    } else {
                        TopAppBarDefaults.windowInsets
                    },
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = userName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    actions = {
                        // sort button
                        Image(
                            modifier = Modifier.onClick(
                                onClick = rememberCallback {
                                    val sheet = SortBottomSheet(
                                        sheetKey = key,
                                        values = uiState.availableSortTypes.map { it.toInt() },
                                        comments = false,
                                        expandTop = true,
                                    )
                                    navigationCoordinator.showBottomSheet(sheet)
                                },
                            ),
                            imageVector = uiState.sortType.toIcon(),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        )

                        // options menu
                        Box {
                            val options = listOf(
                                Option(
                                    OptionId.Info, stringResource(MR.strings.user_detail_info)
                                ),
                                Option(
                                    OptionId.Block,
                                    stringResource(MR.strings.community_detail_block)
                                ),
                                Option(
                                    OptionId.BlockInstance,
                                    stringResource(MR.strings.community_detail_block_instance)
                                ),
                            )
                            var optionsExpanded by remember { mutableStateOf(false) }
                            var optionsOffset by remember { mutableStateOf(Offset.Zero) }
                            Image(
                                modifier = Modifier.onGloballyPositioned {
                                    optionsOffset = it.positionInParent()
                                }.padding(start = Spacing.s).onClick(
                                    onClick = rememberCallback {
                                        optionsExpanded = true
                                    },
                                ),
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                            )
                            CustomDropDown(
                                expanded = optionsExpanded,
                                onDismiss = {
                                    optionsExpanded = false
                                },
                                offset = DpOffset(
                                    x = optionsOffset.x.toLocalDp(),
                                    y = optionsOffset.y.toLocalDp(),
                                ),
                            ) {
                                options.forEach { option ->
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = Spacing.m,
                                            vertical = Spacing.s,
                                        ).onClick(
                                            onClick = rememberCallback {
                                                optionsExpanded = false
                                                when (option.id) {
                                                    OptionId.BlockInstance -> model.reduce(
                                                        UserDetailMviModel.Intent.BlockInstance
                                                    )

                                                    OptionId.Block -> model.reduce(
                                                        UserDetailMviModel.Intent.Block
                                                    )

                                                    OptionId.Info -> {
                                                        navigationCoordinator.showBottomSheet(
                                                            UserInfoScreen(uiState.user.id),
                                                        )
                                                    }

                                                    else -> Unit
                                                }
                                            },
                                        ),
                                        text = option.text,
                                    )
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        if (navigationCoordinator.canPop.value) {
                            Image(
                                modifier = Modifier.onClick(
                                    onClick = rememberCallback {
                                        navigationCoordinator.popScreen()
                                    },
                                ),
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                            )
                        }
                    },
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = isFabVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it * 2 },
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it * 2 },
                    ),
                ) {
                    FloatingActionButtonMenu(
                        items = buildList {
                            this += FloatingActionButtonMenuItem(
                                icon = Icons.Default.ExpandLess,
                                text = stringResource(MR.strings.action_back_to_top),
                                onSelected = rememberCallback {
                                    scope.launch {
                                        lazyListState.scrollToItem(0)
                                        topAppBarState.heightOffset = 0f
                                        topAppBarState.contentOffset = 0f
                                    }
                                },
                            )
                            if (uiState.isLogged && !isOnOtherInstance) {
                                this += FloatingActionButtonMenuItem(
                                    icon = Icons.Default.Chat,
                                    text = stringResource(MR.strings.action_chat),
                                    onSelected = rememberCallback {
                                        val screen = InboxChatScreen(otherUserId = userId)
                                        navigationCoordinator.pushScreen(screen)
                                    },
                                )
                            }
                        },
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        snackbarData = data,
                    )
                }
            },
        ) { padding ->
            val pullRefreshState = rememberPullRefreshState(
                refreshing = uiState.refreshing,
                onRefresh = rememberCallback(model) {
                    model.reduce(UserDetailMviModel.Intent.Refresh)
                },
            )
            Box(
                modifier = Modifier.padding(padding).then(
                    if (settings.hideNavigationBarWhileScrolling) {
                        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                    } else {
                        Modifier
                    }
                ).nestedScroll(fabNestedScrollConnection).pullRefresh(pullRefreshState),
            ) {
                LazyColumn(
                    state = lazyListState,
                ) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                        ) {
                            UserHeader(
                                user = uiState.user,
                                autoLoadImages = uiState.autoLoadImages,
                                onOpenImage = rememberCallbackArgs { url ->
                                    navigationCoordinator.pushScreen(
                                        ZoomableImageScreen(
                                            url
                                        )
                                    )
                                },
                            )
                            SectionSelector(
                                titles = listOf(
                                    stringResource(MR.strings.profile_section_posts),
                                    stringResource(MR.strings.profile_section_comments),
                                ),
                                currentSection = when (uiState.section) {
                                    UserDetailSection.Comments -> 1
                                    else -> 0
                                },
                                onSectionSelected = rememberCallbackArgs { idx ->
                                    val section = when (idx) {
                                        1 -> UserDetailSection.Comments
                                        else -> UserDetailSection.Posts
                                    }
                                    model.reduce(
                                        UserDetailMviModel.Intent.ChangeSection(
                                            section
                                        )
                                    )
                                },
                            )
                            Spacer(modifier = Modifier.height(Spacing.m))
                        }
                    }
                    if (uiState.section == UserDetailSection.Posts) {
                        if (uiState.posts.isEmpty() && uiState.loading && uiState.initial) {
                            items(5) {
                                PostCardPlaceholder(
                                    postLayout = uiState.postLayout,
                                )
                                if (uiState.postLayout != PostLayout.Card) {
                                    Divider(modifier = Modifier.padding(vertical = Spacing.s))
                                } else {
                                    Spacer(modifier = Modifier.height(Spacing.s))
                                }
                            }
                        }
                        items(
                            uiState.posts,
                            { it.id.toString() + (it.updateDate ?: it.publishDate) }) { post ->

                            @Composable
                            fun List<ActionOnSwipe>.toSwipeActions(): List<SwipeAction> =
                                mapNotNull {
                                    when (it) {
                                        ActionOnSwipe.UpVote -> SwipeAction(
                                            swipeContent = {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowCircleUp,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = upVoteColor
                                                ?: defaultUpvoteColor,
                                            onTriggered = rememberCallback {
                                                UserDetailMviModel.Intent.UpVotePost(
                                                    post.id,
                                                )
                                            },
                                        )

                                        ActionOnSwipe.DownVote -> SwipeAction(
                                            swipeContent = {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowCircleDown,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = downVoteColor
                                                ?: defaultDownVoteColor,
                                            onTriggered = rememberCallback {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.DownVotePost(
                                                        post.id,
                                                    )
                                                )
                                            },
                                        )

                                        ActionOnSwipe.Reply -> SwipeAction(
                                            swipeContent = {
                                                androidx.compose.material.Icon(
                                                    imageVector = Icons.Default.Reply,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = replyColor
                                                ?: defaultReplyColor,
                                            onTriggered = rememberCallback {
                                                detailOpener.openReply(originalPost = post)
                                            },
                                        )

                                        ActionOnSwipe.Save -> SwipeAction(
                                            swipeContent = {
                                                Icon(
                                                    imageVector = Icons.Default.Bookmark,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = saveColor
                                                ?: defaultSaveColor,
                                            onTriggered = rememberCallback {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.SavePost(
                                                        id = post.id,
                                                    ),
                                                )
                                            },
                                        )


                                        else -> null
                                    }
                                }

                            SwipeActionCard(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = uiState.swipeActionsEnabled,
                                onGestureBegin = rememberCallback(model) {
                                    model.reduce(UserDetailMviModel.Intent.HapticIndication)
                                },
                                swipeToStartActions = if (uiState.isLogged && !isOnOtherInstance) {
                                    uiState.actionsOnSwipeToStartPosts.toSwipeActions()
                                } else {
                                    emptyList()
                                },
                                swipeToEndActions = if (uiState.isLogged && !isOnOtherInstance) {
                                    uiState.actionsOnSwipeToEndPosts.toSwipeActions()
                                } else {
                                    emptyList()
                                },
                                content = {
                                    PostCard(
                                        post = post,
                                        hideAuthor = true,
                                        postLayout = uiState.postLayout,
                                        limitBodyHeight = true,
                                        fullHeightImage = uiState.fullHeightImages,
                                        blurNsfw = uiState.blurNsfw,
                                        voteFormat = uiState.voteFormat,
                                        autoLoadImages = uiState.autoLoadImages,
                                        actionButtonsActive = uiState.isLogged,
                                        onClick = rememberCallback {
                                            detailOpener.openPostDetail(post)
                                        },
                                        onDoubleClick = if (!uiState.doubleTapActionEnabled) {
                                            null
                                        } else {
                                            rememberCallback(model) {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.UpVotePost(
                                                        id = post.id,
                                                        feedback = true,
                                                    ),
                                                )
                                            }
                                        },
                                        onUpVote = if (!uiState.isLogged || isOnOtherInstance) {
                                            null
                                        } else {
                                            rememberCallback(model) {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.UpVotePost(
                                                        id = post.id,
                                                    ),
                                                )
                                            }
                                        },
                                        onDownVote = if (!uiState.isLogged || isOnOtherInstance) {
                                            null
                                        } else {
                                            rememberCallback(model) {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.DownVotePost(
                                                        id = post.id,
                                                    ),
                                                )
                                            }
                                        },
                                        onSave = if (!uiState.isLogged || isOnOtherInstance) {
                                            null
                                        } else {
                                            rememberCallback(model) {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.SavePost(
                                                        id = post.id,
                                                    ),
                                                )
                                            }
                                        },
                                        onOpenCommunity = rememberCallbackArgs { community, instance ->
                                            detailOpener.openCommunityDetail(
                                                community,
                                                instance
                                            )
                                        },
                                        onOpenCreator = rememberCallbackArgs { user, instance ->
                                            detailOpener.openUserDetail(user, instance)
                                        },
                                        onOpenPost = rememberCallbackArgs { p, instance ->
                                            detailOpener.openPostDetail(p, instance)
                                        },
                                        onOpenWeb = rememberCallbackArgs { url ->
                                            navigationCoordinator.pushScreen(
                                                WebViewScreen(url)
                                            )
                                        },
                                        onReply = if (!uiState.isLogged || isOnOtherInstance) {
                                            null
                                        } else {
                                            rememberCallback {
                                                detailOpener.openPostDetail(post)
                                            }
                                        },
                                        onOpenImage = rememberCallbackArgs { url ->
                                            navigationCoordinator.pushScreen(
                                                ZoomableImageScreen(url),
                                            )
                                        },
                                        options = buildList {
                                            add(
                                                Option(
                                                    OptionId.Share,
                                                    stringResource(MR.strings.post_action_share)
                                                )
                                            )
                                            add(
                                                Option(
                                                    OptionId.SeeRaw,
                                                    stringResource(MR.strings.post_action_see_raw)
                                                )
                                            )
                                            if (uiState.isLogged && !isOnOtherInstance) {
                                                add(
                                                    Option(
                                                        OptionId.CrossPost,
                                                        stringResource(MR.strings.post_action_cross_post)
                                                    )
                                                )
                                                add(
                                                    Option(
                                                        OptionId.Report,
                                                        stringResource(MR.strings.post_action_report)
                                                    )
                                                )
                                            }
                                        },
                                        onOptionSelected = rememberCallbackArgs { optionId ->
                                            when (optionId) {
                                                OptionId.Report -> {
                                                    navigationCoordinator.showBottomSheet(
                                                        CreateReportScreen(
                                                            postId = post.id
                                                        )
                                                    )
                                                }

                                                OptionId.CrossPost -> {
                                                    detailOpener.openCreatePost(
                                                        crossPost = post
                                                    )
                                                }

                                                OptionId.SeeRaw -> {
                                                    rawContent = post
                                                }

                                                OptionId.Share -> {
                                                    val urls = listOfNotNull(
                                                        post.originalUrl,
                                                        "https://${uiState.instance}/post/${post.id}"
                                                    ).distinct()
                                                    if (urls.size == 1) {
                                                        model.reduce(
                                                            UserDetailMviModel.Intent.Share(
                                                                urls.first()
                                                            )
                                                        )
                                                    } else {
                                                        val screen =
                                                            ShareBottomSheet(urls = urls)
                                                        navigationCoordinator.showBottomSheet(
                                                            screen
                                                        )
                                                    }
                                                }

                                                else -> Unit
                                            }
                                        })
                                },
                            )
                            if (uiState.postLayout != PostLayout.Card) {
                                Divider(modifier = Modifier.padding(vertical = Spacing.s))
                            } else {
                                Spacer(modifier = Modifier.height(Spacing.s))
                            }
                        }

                        if (uiState.posts.isEmpty() && !uiState.loading) {
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(top = Spacing.xs),
                                    textAlign = TextAlign.Center,
                                    text = stringResource(MR.strings.message_empty_list),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    } else {
                        if (uiState.comments.isEmpty() && uiState.loading && uiState.initial) {
                            items(5) {
                                CommentCardPlaceholder()
                                Divider(
                                    modifier = Modifier.padding(vertical = Spacing.xxxs),
                                    thickness = 0.25.dp
                                )
                            }
                        }
                        items(
                            items = uiState.comments,
                            key = { it.id.toString() + (it.updateDate ?: it.publishDate) },
                        ) { comment ->
                            @Composable
                            fun List<ActionOnSwipe>.toSwipeActions(): List<SwipeAction> =
                                mapNotNull {
                                    when (it) {
                                        ActionOnSwipe.UpVote -> SwipeAction(
                                            swipeContent = {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowCircleUp,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = upVoteColor
                                                ?: defaultUpvoteColor,
                                            onTriggered = rememberCallback {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.UpVoteComment(
                                                        comment.id
                                                    )
                                                )
                                            },
                                        )

                                        ActionOnSwipe.DownVote -> SwipeAction(
                                            swipeContent = {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowCircleDown,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = downVoteColor
                                                ?: defaultDownVoteColor,
                                            onTriggered = rememberCallback {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.DownVoteComment(
                                                        comment.id
                                                    ),
                                                )
                                            },
                                        )

                                        ActionOnSwipe.Reply -> SwipeAction(
                                            swipeContent = {
                                                Icon(
                                                    imageVector = Icons.Default.Reply,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = replyColor
                                                ?: defaultReplyColor,
                                            onTriggered = rememberCallback {
                                                detailOpener.openReply(
                                                    originalPost = PostModel(id = comment.postId),
                                                    originalComment = comment,
                                                )
                                            },
                                        )

                                        ActionOnSwipe.Save -> SwipeAction(
                                            swipeContent = {
                                                Icon(
                                                    imageVector = Icons.Default.Bookmark,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = saveColor
                                                ?: defaultSaveColor,
                                            onTriggered = rememberCallback {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.SaveComment(
                                                        id = comment.id,
                                                    ),
                                                )
                                            },
                                        )


                                        else -> null
                                    }
                                }

                            SwipeActionCard(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = uiState.swipeActionsEnabled,
                                onGestureBegin = rememberCallback(model) {
                                    model.reduce(UserDetailMviModel.Intent.HapticIndication)
                                },
                                swipeToStartActions = if (uiState.isLogged && !isOnOtherInstance) {
                                    uiState.actionsOnSwipeToStartComments.toSwipeActions()
                                } else {
                                    emptyList()
                                },
                                swipeToEndActions = if (uiState.isLogged && !isOnOtherInstance) {
                                    uiState.actionsOnSwipeToEndComments.toSwipeActions()
                                } else {
                                    emptyList()
                                },
                                content = {
                                    CommentCard(
                                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                                        comment = comment,
                                        voteFormat = uiState.voteFormat,
                                        autoLoadImages = uiState.autoLoadImages,
                                        hideCommunity = false,
                                        hideAuthor = true,
                                        hideIndent = true,
                                        actionButtonsActive = uiState.isLogged,
                                        onClick = rememberCallback {
                                            detailOpener.openPostDetail(
                                                post = PostModel(id = comment.postId),
                                                highlightCommentId = comment.id,
                                            )
                                        },
                                        onImageClick = rememberCallbackArgs { url ->
                                            navigationCoordinator.pushScreen(
                                                ZoomableImageScreen(url)
                                            )
                                        },
                                        onDoubleClick = if (!uiState.doubleTapActionEnabled) {
                                            null
                                        } else {
                                            rememberCallback(model) {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.UpVoteComment(
                                                        id = comment.id,
                                                        feedback = true,
                                                    ),
                                                )
                                            }
                                        },
                                        onSave = if (!uiState.isLogged || isOnOtherInstance) {
                                            null
                                        } else {
                                            rememberCallback(model) {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.SaveComment(
                                                        id = comment.id,
                                                    ),
                                                )
                                            }
                                        },
                                        onUpVote = if (!uiState.isLogged || isOnOtherInstance) {
                                            null
                                        } else {
                                            rememberCallback(model) {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.UpVoteComment(
                                                        id = comment.id,
                                                    ),
                                                )
                                            }
                                        },
                                        onDownVote = if (!uiState.isLogged || isOnOtherInstance) {
                                            null
                                        } else {
                                            rememberCallback(model) {
                                                model.reduce(
                                                    UserDetailMviModel.Intent.DownVoteComment(
                                                        id = comment.id,
                                                    ),
                                                )
                                            }
                                        },
                                        onReply = if (!uiState.isLogged || isOnOtherInstance) {
                                            null
                                        } else {
                                            rememberCallback {
                                                detailOpener.openReply(
                                                    originalPost = PostModel(id = comment.postId),
                                                    originalComment = comment,
                                                )
                                            }
                                        },
                                        onOpenCommunity = rememberCallbackArgs { community, instance ->
                                            detailOpener.openCommunityDetail(
                                                community,
                                                instance
                                            )
                                        },
                                        onOpenCreator = rememberCallbackArgs { user, instance ->
                                            detailOpener.openUserDetail(user, instance)
                                        },
                                        onOpenPost = rememberCallbackArgs { post, instance ->
                                            detailOpener.openPostDetail(post, instance)
                                        },
                                        onOpenWeb = rememberCallbackArgs { url ->
                                            navigationCoordinator.pushScreen(
                                                WebViewScreen(url)
                                            )
                                        },
                                        options = buildList {
                                            add(
                                                Option(
                                                    OptionId.SeeRaw,
                                                    stringResource(MR.strings.post_action_see_raw)
                                                )
                                            )
                                            if (uiState.isLogged && !isOnOtherInstance) {
                                                add(
                                                    Option(
                                                        OptionId.Report,
                                                        stringResource(MR.strings.post_action_report)
                                                    )
                                                )
                                            }
                                        },
                                        onOptionSelected = rememberCallbackArgs { optionId ->
                                            when (optionId) {
                                                OptionId.Report -> {
                                                    navigationCoordinator.showBottomSheet(
                                                        CreateReportScreen(
                                                            commentId = comment.id
                                                        ),
                                                    )
                                                }

                                                OptionId.SeeRaw -> {
                                                    rawContent = comment
                                                }

                                                else -> Unit
                                            }
                                        },
                                    )
                                    Divider(
                                        modifier = Modifier.padding(vertical = Spacing.xxxs),
                                        thickness = 0.25.dp
                                    )
                                },
                            )
                        }

                        if (uiState.comments.isEmpty() && !uiState.loading) {
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(top = Spacing.xs),
                                    textAlign = TextAlign.Center,
                                    text = stringResource(MR.strings.message_empty_list),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    }
                    item {
                        if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                            if (settings.infiniteScrollEnabled) {
                                model.reduce(UserDetailMviModel.Intent.LoadNextPage)
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(top = Spacing.s),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Button(
                                        onClick = rememberCallback(model) {
                                            model.reduce(UserDetailMviModel.Intent.LoadNextPage)
                                        },
                                    ) {
                                        Text(
                                            text = if (uiState.section == UserDetailSection.Posts) {
                                                stringResource(MR.strings.post_list_load_more_posts)
                                            } else {
                                                stringResource(MR.strings.post_detail_load_more_comments)
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    }
                                }
                            }
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
                    item {
                        Spacer(modifier = Modifier.height(Spacing.xxl))
                    }
                }
                PullRefreshIndicator(
                    refreshing = uiState.refreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                )

                if (uiState.asyncInProgress) {
                    ProgressHud()
                }
            }
        }

        if (rawContent != null) {
            when (val content = rawContent) {
                is PostModel -> {
                    RawContentDialog(
                        title = content.title,
                        publishDate = content.publishDate,
                        updateDate = content.updateDate,
                        url = content.url,
                        text = content.text,
                        onDismiss = rememberCallback {
                            rawContent = null
                        },
                        onQuote = rememberCallbackArgs { quotation ->
                            rawContent = null
                            if (quotation != null) {
                                detailOpener.openReply(
                                    originalPost = content,
                                    initialText = buildString {
                                        append("> ")
                                        append(quotation)
                                        append("\n\n")
                                    },
                                )
                            }
                        },
                    )
                }

                is CommentModel -> {
                    RawContentDialog(text = content.text,
                        publishDate = content.publishDate,
                        updateDate = content.updateDate,
                        onDismiss = {
                            rawContent = null
                        },
                        onQuote = rememberCallbackArgs { quotation ->
                            rawContent = null
                            if (quotation != null) {
                                detailOpener.openReply(
                                    originalComment = content,
                                    initialText = buildString {
                                        append("> ")
                                        append(quotation)
                                        append("\n\n")
                                    },
                                )
                            }
                        })
                }
            }
        }
    }
}
