package com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.IconSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.CustomImage
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PlaceholderImage
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallback
import com.github.diegoberaldin.raccoonforlemmy.core.utils.toLocalPixel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.readableName
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun CommunityAndCreatorInfo(
    modifier: Modifier = Modifier,
    iconSize: Dp = IconSize.l,
    indicatorExpanded: Boolean? = null,
    autoLoadImages: Boolean = true,
    community: CommunityModel? = null,
    creator: UserModel? = null,
    distinguished: Boolean = false,
    featured: Boolean = false,
    locked: Boolean = false,
    isFromModerator: Boolean = false,
    isOp: Boolean = false,
    onOpenCommunity: ((CommunityModel) -> Unit)? = null,
    onOpenCreator: ((UserModel) -> Unit)? = null,
    onToggleExpanded: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
) {
    val communityName = community?.readableName.orEmpty()
    val communityIcon = community?.icon.orEmpty()
    val creatorName = creator?.readableName.orEmpty()
    val creatorAvatar = creator?.avatar.orEmpty()
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s)
    ) {
        if (communityIcon.isNotEmpty()) {
            if (autoLoadImages) {
                CustomImage(
                    modifier = Modifier
                        .onClick(
                            onClick = rememberCallback {
                                if (community != null) {
                                    onOpenCommunity?.invoke(community)
                                }
                            },
                            onDoubleClick = onDoubleClick ?: {},
                        )
                        .padding(Spacing.xxxs)
                        .size(iconSize)
                        .clip(RoundedCornerShape(iconSize / 2)),
                    url = communityIcon,
                    quality = FilterQuality.Low,
                    contentScale = ContentScale.FillBounds,
                )
            } else {
                PlaceholderImage(
                    modifier = Modifier.onClick(
                        onClick = rememberCallback {
                            if (community != null) {
                                onOpenCommunity?.invoke(community)
                            }
                        },
                        onDoubleClick = onDoubleClick ?: {},
                    ),
                    size = IconSize.l,
                    title = communityName,
                )
            }
        } else if (creatorAvatar.isNotEmpty()) {
            if (autoLoadImages) {
                CustomImage(
                    modifier = Modifier
                        .onClick(
                            onClick = rememberCallback {
                                if (creator != null) {
                                    onOpenCreator?.invoke(creator)
                                }
                            },
                            onDoubleClick = onDoubleClick ?: {},
                        )
                        .padding(Spacing.xxxs)
                        .size(iconSize)
                        .clip(RoundedCornerShape(iconSize / 2)),
                    url = creatorAvatar,
                    quality = FilterQuality.Low,
                    contentScale = ContentScale.FillBounds,
                )
            } else {
                PlaceholderImage(
                    modifier = Modifier.onClick(
                        onClick = rememberCallback {
                            if (creator != null) {
                                onOpenCreator?.invoke(creator)
                            }
                        },
                        onDoubleClick = onDoubleClick ?: {},
                    ),
                    size = iconSize,
                    title = creatorName,
                )
            }
        }
        Column(
            modifier = Modifier.padding(vertical = Spacing.xxxs),
        ) {
            if (community != null) {
                Text(
                    modifier = Modifier
                        .onClick(
                            onClick = rememberCallback {
                                onOpenCommunity?.invoke(community)
                            },
                            onDoubleClick = onDoubleClick ?: {},
                            onLongClick = onLongClick ?: {},
                        ),
                    text = communityName,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (creator == null) ancillaryColor else fullColor,
                )
            }
            if (creator != null) {
                val translationAmount = 3.dp.toLocalPixel()
                Text(
                    modifier = Modifier
                        .graphicsLayer {
                            if (communityName.isNotEmpty()) {
                                translationY = -translationAmount
                            }
                        }
                        .onClick(
                            onClick = rememberCallback {
                                onOpenCreator?.invoke(creator)
                            },
                            onDoubleClick = onDoubleClick ?: {},
                            onLongClick = onLongClick ?: {},
                        ),
                    text = creatorName,
                    style = MaterialTheme.typography.bodySmall,
                    color = ancillaryColor,
                )
            }
        }
        if (isOp) {
            OpIndicator(
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        val buttonModifier = Modifier.size(IconSize.m).padding(3.5.dp)
        if (isFromModerator) {
            Icon(
                modifier = buttonModifier,
                imageVector = Icons.Default.LocalPolice,
                contentDescription = null,
            )
        }
        if (distinguished) {
            Icon(
                modifier = buttonModifier,
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
            )
        } else if (featured) {
            Icon(
                modifier = buttonModifier,
                imageVector = Icons.Default.PushPin,
                contentDescription = null,
            )
        }
        if (locked) {
            Icon(
                modifier = buttonModifier,
                imageVector = Icons.Default.Lock,
                contentDescription = null,
            )
        }
        if (indicatorExpanded != null) {
            val expandedModifier = Modifier
                .padding(end = Spacing.xs)
                .onClick(
                    onClick = rememberCallback {
                        onToggleExpanded?.invoke()
                    },
                )
            if (indicatorExpanded) {
                Icon(
                    modifier = expandedModifier,
                    imageVector = Icons.Default.ExpandLess,
                    contentDescription = null,
                )
            } else {
                Icon(
                    modifier = expandedModifier,
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun OpIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(
                color = MaterialTheme.colorScheme.onBackground,
                width = Dp.Hairline,
                shape = RoundedCornerShape(CornerSize.m)
            )
            .padding(
                vertical = Spacing.xxxs,
                horizontal = Spacing.xs,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "OP",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 8.sp,
            fontFamily = fontFamilyResource(MR.fonts.TitilliumWeb.regular),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}