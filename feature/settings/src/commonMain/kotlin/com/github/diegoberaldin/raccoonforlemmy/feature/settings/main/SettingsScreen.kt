package com.github.diegoberaldin.raccoonforlemmy.feature.settings.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Explicit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.toSize
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.FontScale
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.UiTheme
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.scaleFactor
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getColorSchemeProvider
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.SettingsHeader
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.SettingsSwitchRow
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.DurationBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.InboxTypeSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.LanguageBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.ListingTypeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.PostBodyMaxLinesBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.PostLayoutBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.SliderBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.ThemeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.VoteFormatBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.TabNavigationSection
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallback
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallbackArgs
import com.github.diegoberaldin.raccoonforlemmy.core.utils.datetime.getPrettyDuration
import com.github.diegoberaldin.raccoonforlemmy.core.utils.toLanguageFlag
import com.github.diegoberaldin.raccoonforlemmy.core.utils.toLanguageName
import com.github.diegoberaldin.raccoonforlemmy.core.utils.toLocalDp
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toInt
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.ui.SettingsTab
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.ui.components.SettingsColorRow
import com.github.diegoberaldin.raccoonforlemmy.feature.settings.ui.components.SettingsMultiColorRow
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import com.github.diegoberaldin.raccoonforlemmy.resources.di.getLanguageRepository
import com.github.diegoberaldin.raccoonforlemmy.resources.di.staticString
import com.github.diegoberaldin.raccoonforlemmy.unit.about.AboutDialog
import com.github.diegoberaldin.raccoonforlemmy.unit.accountsettings.AccountSettingsScreen
import com.github.diegoberaldin.raccoonforlemmy.unit.choosecolor.ColorBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.unit.choosecolor.CommentBarThemeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.unit.choosecolor.VoteThemeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.unit.choosefont.FontFamilyBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.unit.choosefont.FontScaleBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsScreen
import com.github.diegoberaldin.raccoonforlemmy.unit.manageban.ManageBanScreen
import dev.icerock.moko.resources.compose.stringResource
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class SettingsScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<SettingsMviModel>()
        model.bindToLifecycle(SettingsTab.key)
        val uiState by model.uiState.collectAsState()
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val notificationCenter = remember { getNotificationCenter() }
        val drawerCoordinator = remember { getDrawerCoordinator() }
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val scrollState = rememberScrollState()
        val languageRepository = remember { getLanguageRepository() }
        val lang by languageRepository.currentLanguage.collectAsState()
        var uiFontSizeWorkaround by remember { mutableStateOf(true) }
        val themeRepository = remember { getThemeRepository() }
        val colorSchemeProvider = remember { getColorSchemeProvider() }
        val defaultTheme = if (isSystemInDarkTheme()) {
            UiTheme.Dark
        } else {
            UiTheme.Light
        }
        var infoDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(themeRepository) {
            themeRepository.uiFontScale.drop(1).onEach {
                uiFontSizeWorkaround = false
                delay(50)
                uiFontSizeWorkaround = true
            }.launchIn(this)
        }
        LaunchedEffect(Unit) {
            navigationCoordinator.onDoubleTabSelection.onEach { section ->
                if (section == TabNavigationSection.Settings) {
                    scrollState.scrollTo(0)
                    topAppBarState.heightOffset = 0f
                    topAppBarState.contentOffset = 0f
                }
            }.launchIn(this)
        }
        LaunchedEffect(notificationCenter) {
            notificationCenter.subscribe(NotificationCenterEvent.CloseDialog::class).onEach {
                infoDialogOpened = false
            }.launchIn(this)
        }

        if (!uiFontSizeWorkaround) {
            return
        }

        var screenWidth by remember { mutableStateOf(0f) }
        Scaffold(
            modifier = Modifier.onGloballyPositioned {
                screenWidth = it.size.toSize().width
            }.padding(Spacing.xxs),
            topBar = {
                val title by remember(lang) {
                    mutableStateOf(staticString(MR.strings.navigation_settings.desc()))
                }
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        Image(
                            modifier = Modifier.onClick(
                                onClick = rememberCallback {
                                    scope.launch {
                                        drawerCoordinator.toggleDrawer()
                                    }
                                },
                            ),
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        )
                    },
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                )
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    SettingsHeader(
                        icon = Icons.Default.Style,
                        title = stringResource(MR.strings.settings_section_appearance),
                    )

                    // language
                    SettingsRow(
                        title = stringResource(MR.strings.settings_language),
                        annotatedValue = buildAnnotatedString {
                            with(uiState.lang) {
                                append(toLanguageFlag())
                                append("  ")
                                append(toLanguageName())
                            }
                        },
                        onTap = rememberCallback {
                            val sheet = LanguageBottomSheet()
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // theme
                    SettingsRow(
                        title = stringResource(MR.strings.settings_ui_theme),
                        value = uiState.uiTheme.toReadableName(),
                        onTap = rememberCallback {
                            val sheet = ThemeBottomSheet()
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // dynamic colors
                    if (uiState.supportsDynamicColors) {
                        SettingsSwitchRow(
                            title = stringResource(MR.strings.settings_dynamic_colors),
                            value = uiState.dynamicColors,
                            onValueChanged = rememberCallbackArgs(model) { value ->
                                model.reduce(
                                    SettingsMviModel.Intent.ChangeDynamicColors(value)
                                )
                            },
                        )
                    }

                    // custom scheme seed color
                    SettingsColorRow(
                        title = stringResource(MR.strings.settings_custom_seed_color),
                        value = uiState.customSeedColor ?: colorSchemeProvider.getColorScheme(
                            theme = uiState.uiTheme ?: defaultTheme,
                            dynamic = uiState.dynamicColors,
                        ).primary,
                        onTap = rememberCallback {
                            val sheet = ColorBottomSheet()
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // action colors
                    if (uiState.isLogged) {
                        SettingsColorRow(
                            title = stringResource(MR.strings.settings_upvote_color),
                            value = uiState.upVoteColor ?: MaterialTheme.colorScheme.primary,
                            onTap = rememberCallback {
                                val screen = VoteThemeBottomSheet(
                                    actionType = 0,
                                )
                                navigationCoordinator.showBottomSheet(screen)
                            },
                        )
                        SettingsColorRow(
                            title = stringResource(MR.strings.settings_downvote_color),
                            value = uiState.downVoteColor ?: MaterialTheme.colorScheme.tertiary,
                            onTap = rememberCallback {
                                val screen = VoteThemeBottomSheet(
                                    actionType = 1,
                                )
                                navigationCoordinator.showBottomSheet(screen)
                            },
                        )
                        SettingsColorRow(
                            title = stringResource(MR.strings.settings_reply_color),
                            value = uiState.replyColor ?: MaterialTheme.colorScheme.secondary,
                            onTap = rememberCallback {
                                val screen = VoteThemeBottomSheet(
                                    actionType = 2,
                                )
                                navigationCoordinator.showBottomSheet(screen)
                            },
                        )
                        SettingsColorRow(
                            title = stringResource(MR.strings.settings_save_color),
                            value = uiState.saveColor
                                ?: MaterialTheme.colorScheme.secondaryContainer,
                            onTap = rememberCallback {
                                val screen = VoteThemeBottomSheet(
                                    actionType = 3,
                                )
                                navigationCoordinator.showBottomSheet(screen)
                            },
                        )
                    }

                    // comment bar theme
                    val commentBarColors =
                        themeRepository.getCommentBarColors(uiState.commentBarTheme)
                    SettingsMultiColorRow(
                        title = stringResource(MR.strings.settings_comment_bar_theme),
                        values = commentBarColors,
                        onTap = rememberCallback {
                            val screen = CommentBarThemeBottomSheet()
                            navigationCoordinator.showBottomSheet(screen)
                        }
                    )

                    // font family
                    SettingsRow(
                        title = stringResource(MR.strings.settings_ui_font_family),
                        value = uiState.uiFontFamily.toReadableName(),
                        onTap = rememberCallback {
                            val sheet = FontFamilyBottomSheet()
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )
                    SettingsRow(
                        title = stringResource(MR.strings.settings_content_font_family),
                        value = uiState.contentFontFamily.toReadableName(),
                        onTap = rememberCallback {
                            val sheet = FontFamilyBottomSheet(content = true)
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )
                    // font scale
                    SettingsRow(
                        title = stringResource(MR.strings.settings_ui_font_scale),
                        value = uiState.uiFontScale.toReadableName(),
                        onTap = rememberCallback {
                            val sheet = FontScaleBottomSheet(
                                values = listOf(
                                    FontScale.Large,
                                    FontScale.Normal,
                                    FontScale.Small,
                                ).map { it.scaleFactor },
                                content = false,
                            )
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )
                    SettingsRow(
                        title = stringResource(MR.strings.settings_content_font_scale),
                        value = uiState.contentFontScale.toReadableName(),
                        onTap = rememberCallback {
                            val sheet = FontScaleBottomSheet(content = true)
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // post layout
                    SettingsRow(
                        title = stringResource(MR.strings.settings_post_layout),
                        value = uiState.postLayout.toReadableName(),
                        onTap = rememberCallback {
                            val sheet = PostLayoutBottomSheet()
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // body max lines in full layout
                    if (uiState.postLayout == PostLayout.Full) {
                        SettingsRow(
                            title = stringResource(MR.strings.settings_post_body_max_lines),
                            value = if (uiState.postBodyMaxLines == null) {
                                stringResource(MR.strings.settings_post_body_max_lines_unlimited)
                            } else {
                                uiState.postBodyMaxLines.toString()
                            },
                            onTap = rememberCallback {
                                val screen = PostBodyMaxLinesBottomSheet()
                                navigationCoordinator.showBottomSheet(screen)
                            },
                        )
                    }

                    // vote format
                    SettingsRow(
                        title = stringResource(MR.strings.settings_vote_format),
                        value = uiState.voteFormat.toReadableName(),
                        onTap = {
                            val sheet = VoteFormatBottomSheet()
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // full height images
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_full_height_images),
                        value = uiState.fullHeightImages,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeFullHeightImages(value)
                            )
                        },
                    )

                    // navigation bar titles
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_navigation_bar_titles_visible),
                        value = uiState.navBarTitlesVisible,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeNavBarTitlesVisible(value)
                            )
                        },
                    )

                    SettingsHeader(
                        icon = Icons.Default.Dashboard,
                        title = stringResource(MR.strings.settings_section_feed),
                    )

                    // default listing type
                    SettingsRow(
                        title = stringResource(MR.strings.settings_default_listing_type),
                        value = uiState.defaultListingType.toReadableName(),
                        onTap = rememberCallback {
                            val sheet = ListingTypeBottomSheet(
                                sheetKey = key,
                                isLogged = uiState.isLogged,
                            )
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // default post sort type
                    SettingsRow(
                        title = stringResource(MR.strings.settings_default_post_sort_type),
                        value = uiState.defaultPostSortType.toReadableName(),
                        onTap = rememberCallback {
                            val sheet = SortBottomSheet(
                                sheetKey = key,
                                values = uiState.availableSortTypesForPosts.map { it.toInt() },
                                expandTop = true,
                                comments = false,
                            )
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // default comment sort type
                    SettingsRow(
                        title = stringResource(MR.strings.settings_default_comment_sort_type),
                        value = uiState.defaultCommentSortType.toReadableName(),
                        onTap = rememberCallback {
                            val sheet = SortBottomSheet(
                                sheetKey = key,
                                comments = true,
                                values = uiState.availableSortTypesForComments.map { it.toInt() },
                            )
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // default inbox type
                    SettingsRow(
                        title = stringResource(MR.strings.settings_default_inbox_type),
                        value = if (uiState.defaultInboxUnreadOnly) {
                            stringResource(MR.strings.inbox_listing_type_unread)
                        } else {
                            stringResource(MR.strings.inbox_listing_type_all)
                        },
                        onTap = rememberCallback {
                            val sheet = InboxTypeSheet()
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    SettingsHeader(
                        icon = Icons.Default.SettingsApplications,
                        title = stringResource(MR.strings.settings_section_behaviour),
                    )

                    // edge to edge
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_edge_to_edge),
                        value = uiState.edgeToEdge,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeEdgeToEdge(value)
                            )
                        },
                    )

                    // infinite scrolling
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_infinite_scroll_disabled),
                        value = uiState.infiniteScrollDisabled,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeInfiniteScrollDisabled(value)
                            )
                        },
                    )

                    // mark as read while scrolling
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_mark_as_read_while_scrolling),
                        value = uiState.markAsReadWhileScrolling,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeMarkAsReadWhileScrolling(value)
                            )
                        },
                    )

                    // zombie mode interval
                    SettingsRow(
                        title = stringResource(MR.strings.settings_zombie_mode_interval),
                        value = uiState.zombieModeInterval.getPrettyDuration(
                            secondsLabel = stringResource(MR.strings.post_second_short),
                            minutesLabel = stringResource(MR.strings.post_minute_short),
                            hoursLabel = stringResource(MR.strings.post_hour_short),
                        ),
                        onTap = rememberCallback {
                            val sheet = DurationBottomSheet()
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // zombie scroll amount
                    SettingsRow(
                        title = stringResource(MR.strings.settings_zombie_mode_scroll_amount),
                        value = buildString {
                            val pt = uiState.zombieModeScrollAmount.toLocalDp().value.roundToInt()
                            append(pt)
                            append(stringResource(MR.strings.settings_points_short))
                        },
                        onTap = rememberCallback {
                            val sheet = SliderBottomSheet(
                                min = 0f,
                                max = screenWidth,
                                initial = uiState.zombieModeScrollAmount,
                            )
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // swipe actions
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_enable_swipe_actions),
                        value = uiState.enableSwipeActions,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeEnableSwipeActions(value)
                            )
                        },
                    )
                    if (uiState.isLogged) {
                        SettingsRow(
                            title = stringResource(MR.strings.settings_configure_swipe_actions),
                            disclosureIndicator = true,
                            onTap = rememberCallback {
                                val screen = ConfigureSwipeActionsScreen()
                                navigationCoordinator.pushScreen(screen)
                            },
                        )
                    }

                    // double tap
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_enable_double_tap),
                        value = uiState.enableDoubleTapAction,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeEnableDoubleTapAction(value)
                            )
                        },
                    )

                    // bottom navigation hiding
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_hide_navigation_bar),
                        value = uiState.hideNavigationBarWhileScrolling,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeHideNavigationBarWhileScrolling(value)
                            )
                        },
                    )

                    // URL open
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_open_url_external),
                        value = uiState.openUrlsInExternalBrowser,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeOpenUrlsInExternalBrowser(value)
                            )
                        },
                    )

                    // auto-expand comments
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_auto_expand_comments),
                        value = uiState.autoExpandComments,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeAutoExpandComments(value)
                            )
                        },
                    )

                    // image loading
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_auto_load_images),
                        value = uiState.autoLoadImages,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeAutoLoadImages(value)
                            )
                        },
                    )

                    // search posts only in title
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_search_posts_title_only),
                        value = uiState.searchPostTitleOnly,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeSearchPostTitleOnly(value)
                            )
                        },
                    )

                    if (uiState.isLogged) {
                        SettingsHeader(
                            icon = Icons.Default.AdminPanelSettings,
                            title = stringResource(MR.strings.settings_section_account),
                        )

                        // web preferences
                        SettingsRow(
                            title = stringResource(MR.strings.settings_web_preferences),
                            disclosureIndicator = true,
                            onTap = rememberCallback {
                                val screen = AccountSettingsScreen()
                                navigationCoordinator.pushScreen(screen)
                            },
                        )

                        // bans and filters
                        SettingsRow(
                            title = stringResource(MR.strings.settings_manage_ban),
                            disclosureIndicator = true,
                            onTap = rememberCallback {
                                val screen = ManageBanScreen()
                                navigationCoordinator.pushScreen(screen)
                            },
                        )
                    }

                    SettingsHeader(
                        icon = Icons.Default.Explicit,
                        title = stringResource(MR.strings.settings_section_nsfw),
                    )

                    // NSFW options
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_include_nsfw),
                        value = uiState.includeNsfw,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(SettingsMviModel.Intent.ChangeIncludeNsfw(value))
                        })
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_blur_nsfw),
                        value = uiState.blurNsfw,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(SettingsMviModel.Intent.ChangeBlurNsfw(value))
                        },
                    )

                    SettingsHeader(
                        icon = Icons.Default.BugReport,
                        title = stringResource(MR.strings.settings_section_debug),
                    )

                    // enable crash report
                    SettingsSwitchRow(
                        title = stringResource(MR.strings.settings_enable_crash_report),
                        value = uiState.crashReportEnabled,
                        onValueChanged = rememberCallbackArgs(model) { value ->
                            model.reduce(SettingsMviModel.Intent.ChangeCrashReportEnabled(value))
                        },
                    )

                    // about
                    SettingsRow(
                        title = stringResource(MR.strings.settings_about),
                        value = "",
                        onTap = rememberCallback {
                            infoDialogOpened = true
                        },
                    )

                    Spacer(modifier = Modifier.height(Spacing.xxxl))
                }
            }
        }

        if (infoDialogOpened) {
            AboutDialog().Content()
        }
    }
}
