package com.github.diegoberaldin.raccoonforlemmy.unit.choosecolor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.CommentBarTheme
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toDownVoteColor
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toReplyColor
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toSaveColor
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toUpVoteColor
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.BottomSheetHandle
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallback
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

class VoteThemeBottomSheet(
    val actionType: Int,
) : Screen {

    @Composable
    override fun Content() {
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val notificationCenter = remember { getNotificationCenter() }
        var customPickerDialogOpened by remember { mutableStateOf(false) }
        val settingsRepository = remember { getSettingsRepository() }
        val defaultUpvoteColor = MaterialTheme.colorScheme.primary
        val defaultReplyColor = MaterialTheme.colorScheme.secondary
        val defaultDownvoteColor = MaterialTheme.colorScheme.tertiary

        Column(
            modifier = Modifier.padding(
                top = Spacing.s,
                start = Spacing.s,
                end = Spacing.s,
                bottom = Spacing.m,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BottomSheetHandle()
                Text(
                    modifier = Modifier.padding(
                        start = Spacing.s,
                        top = Spacing.s,
                        end = Spacing.s,
                    ),
                    text = when (actionType) {
                        3 -> stringResource(MR.strings.settings_save_color)
                        2 -> stringResource(MR.strings.settings_reply_color)
                        1 -> stringResource(MR.strings.settings_downvote_color)
                        else -> stringResource(MR.strings.settings_upvote_color)
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            val customText = stringResource(MR.strings.settings_color_custom)
            val values: List<CommentBarTheme?> = listOf(
                CommentBarTheme.Blue,
                CommentBarTheme.Green,
                CommentBarTheme.Red,
                CommentBarTheme.Rainbow,
                null,
                null,
            )
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Spacing.xxxs),
            ) {
                values.forEachIndexed { idx, value ->
                    val text = if (idx == values.lastIndex) {
                        stringResource(MR.strings.button_reset)
                    } else {
                        value.toReadableName()
                    }
                    val isChooseCustom = text == customText
                    Row(
                        modifier = Modifier.padding(
                            horizontal = Spacing.s,
                            vertical = Spacing.s,
                        ).fillMaxWidth().onClick(
                            onClick = rememberCallback {
                                if (!isChooseCustom) {
                                    notificationCenter.send(
                                        NotificationCenterEvent.ChangeActionColor(
                                            color = when (actionType) {
                                                3 -> {
                                                    value?.toSaveColor() ?: defaultReplyColor
                                                }

                                                2 -> {
                                                    value?.toReplyColor() ?: defaultReplyColor
                                                }

                                                1 -> {
                                                    value?.toDownVoteColor() ?: defaultDownvoteColor
                                                }

                                                else -> {
                                                    value?.toUpVoteColor() ?: defaultUpvoteColor
                                                }
                                            },
                                            actionType = actionType,
                                        )
                                    )
                                    navigationCoordinator.hideBottomSheet()
                                } else {
                                    customPickerDialogOpened = true
                                }
                            },
                        ),
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        if (!isChooseCustom) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = when (actionType) {
                                            3 -> value.toSaveColor()
                                            2 -> value.toReplyColor()
                                            1 -> value.toDownVoteColor()
                                            else -> value.toUpVoteColor()
                                        },
                                        shape = CircleShape
                                    )
                            )
                        } else {
                            Image(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                            )
                        }
                    }
                }
            }
        }

        if (customPickerDialogOpened) {
            val current = when (actionType) {
                3 -> {
                    settingsRepository.currentSettings.value.saveColor?.let { Color(it) }
                }

                2 -> {
                    settingsRepository.currentSettings.value.replyColor?.let { Color(it) }
                }

                1 -> {
                    settingsRepository.currentSettings.value.downVoteColor?.let { Color(it) }
                }

                else -> {
                    settingsRepository.currentSettings.value.upVoteColor?.let { Color(it) }
                }
            }
            ColorPickerDialog(
                initialValue = current ?: MaterialTheme.colorScheme.primary,
                onClose = {
                    customPickerDialogOpened = false
                },
                onSubmit = { color ->
                    notificationCenter.send(
                        NotificationCenterEvent.ChangeActionColor(
                            color = color,
                            actionType = actionType,
                        )
                    )
                    navigationCoordinator.hideBottomSheet()
                }
            )
        }
    }
}
