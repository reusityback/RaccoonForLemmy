package com.github.diegoberaldin.raccoonforlemmy.unit.modlog.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.IconSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.CustomImage
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.Option
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.PostCardBody
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallback
import com.github.diegoberaldin.raccoonforlemmy.core.utils.datetime.prettifyDate
import com.github.diegoberaldin.raccoonforlemmy.core.utils.toLocalDp
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.readableName

@Composable
internal fun InnerModlogItem(
    modifier: Modifier = Modifier,
    reason: String? = null,
    autoLoadImages: Boolean = true,
    date: String? = null,
    moderator: UserModel? = null,
    postLayout: PostLayout = PostLayout.Card,
    options: List<Option> = emptyList(),
    onOpenUser: ((UserModel) -> Unit)? = null,
    onOpen: (() -> Unit)? = null,
    innerContent: (@Composable () -> Unit)? = null,
    onOptionSelected: ((OptionId) -> Unit)? = null,
) {
    Box(
        modifier = modifier.let {
            if (postLayout == PostLayout.Card) {
                it.padding(horizontal = Spacing.xs)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
                        shape = RoundedCornerShape(CornerSize.l),
                    ).padding(Spacing.s)
            } else {
                it.background(MaterialTheme.colorScheme.background)
            }
        },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            ModlogHeader(
                creator = moderator,
                autoLoadImages = autoLoadImages,
                onOpenCreator = onOpenUser,
            )
            CustomizedContent {
                if (reason != null) {
                    PostCardBody(
                        modifier = Modifier.padding(
                            horizontal = Spacing.xs,
                        ),
                        text = reason,
                    )
                }
                if (innerContent != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = Spacing.xs,
                                horizontal = Spacing.s,
                            )
                    ) {
                        innerContent()
                    }
                }
            }
            ModlogFooter(
                date = date,
                onOpen = onOpen,
                options = options,
                onOptionSelected = onOptionSelected,
            )
        }
    }
}

@Composable
private fun ModlogHeader(
    modifier: Modifier = Modifier,
    creator: UserModel? = null,
    autoLoadImages: Boolean = true,
    iconSize: Dp = IconSize.s,
    onOpenCreator: ((UserModel) -> Unit)? = null,
) {
    val creatorName = creator?.readableName.orEmpty()
    val creatorAvatar = creator?.avatar.orEmpty()
    if (creatorName.isNotEmpty()) {
        Row(
            modifier = modifier
                .onClick(
                    onClick = rememberCallback {
                        if (creator != null) {
                            onOpenCreator?.invoke(creator)
                        }
                    },
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            if (creatorAvatar.isNotEmpty() && autoLoadImages) {
                CustomImage(
                    modifier = Modifier
                        .padding(Spacing.xxxs)
                        .size(iconSize)
                        .clip(RoundedCornerShape(iconSize / 2)),
                    url = creatorAvatar,
                    quality = FilterQuality.Low,
                    contentScale = ContentScale.FillBounds,
                )
            }
            Text(
                modifier = Modifier.padding(vertical = Spacing.xs),
                text = creatorName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun ModlogFooter(
    date: String? = null,
    options: List<Option> = emptyList(),
    onOpen: (() -> Unit)? = null,
    onOptionSelected: ((OptionId) -> Unit)? = null,
) {
    val buttonModifier = Modifier.size(IconSize.m).padding(3.dp)
    var optionsExpanded by remember { mutableStateOf(false) }
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = buttonModifier,
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = ancillaryColor,
                )
                Text(
                    text = date?.prettifyDate() ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    color = ancillaryColor
                )
            }
            if (options.isNotEmpty()) {
                Icon(
                    modifier = Modifier.size(IconSize.m)
                        .padding(Spacing.xs)
                        .padding(top = Spacing.xxs)
                        .onGloballyPositioned {
                            optionsOffset = it.positionInParent()
                        }
                        .onClick(
                            onClick = rememberCallback {
                                optionsExpanded = true
                            },
                        ),
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = null,
                    tint = ancillaryColor,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (onOpen != null) {
                Image(
                    modifier = buttonModifier
                        .onClick(
                            onClick = rememberCallback {
                                onOpen.invoke()
                            },
                        ),
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                )
            }
        }
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
                            onOptionSelected?.invoke(option.id)
                        },
                    ),
                    text = option.text,
                )
            }
        }
    }
}