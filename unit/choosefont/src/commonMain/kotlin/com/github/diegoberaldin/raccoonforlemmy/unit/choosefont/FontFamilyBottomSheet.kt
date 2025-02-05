package com.github.diegoberaldin.raccoonforlemmy.unit.choosefont

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import cafe.adriel.voyager.core.screen.Screen
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.UiFontFamily
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toInt
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toReadableName
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.toUiFontFamily
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.BottomSheetHandle
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallback
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.fontFamilyResource
import dev.icerock.moko.resources.compose.stringResource

private val defaultChoices: List<Int> = listOf(
    UiFontFamily.Poppins,
    UiFontFamily.TitilliumWeb,
    UiFontFamily.NotoSans,
    UiFontFamily.CharisSIL,
    UiFontFamily.Comfortaa,
    UiFontFamily.Default,
).map { it.toInt() }



class FontFamilyBottomSheet(
    private val values: List<Int> = defaultChoices,
    private val content: Boolean = false,
) : Screen {

    @Composable
    override fun Content() {
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val notificationCenter = remember { getNotificationCenter() }
        Column(
            modifier = Modifier
                .padding(
                    top = Spacing.s,
                    start = Spacing.s,
                    end = Spacing.s,
                    bottom = Spacing.m,
                ),
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BottomSheetHandle()
                Text(
                    modifier = Modifier.padding(
                        start = Spacing.s,
                        top = Spacing.s,
                        end = Spacing.s,
                    ),
                    text = stringResource(MR.strings.settings_ui_font_family),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxxs),
                ) {
                    for (value in values) {
                        val family = value.toUiFontFamily()
                        Row(
                            modifier = Modifier.padding(
                                horizontal = Spacing.s,
                                vertical = Spacing.m,
                            )
                                .fillMaxWidth()
                                .onClick(
                                    onClick = rememberCallback {
                                        val event = if (content) {
                                            NotificationCenterEvent.ChangeContentFontFamily(family)
                                        } else {
                                            NotificationCenterEvent.ChangeFontFamily(family)
                                        }
                                        notificationCenter.send(event)
                                        navigationCoordinator.hideBottomSheet()
                                    },
                                ),
                        ) {
                            val fontFamily = when (family) {
                                UiFontFamily.CharisSIL -> fontFamilyResource(MR.fonts.CharisSIL.regular)
                                UiFontFamily.NotoSans -> fontFamilyResource(MR.fonts.NotoSans.regular)
                                UiFontFamily.Comfortaa -> fontFamilyResource(MR.fonts.Comfortaa.regular)
                                UiFontFamily.Poppins -> fontFamilyResource(MR.fonts.Poppins.regular)
                                UiFontFamily.TitilliumWeb -> fontFamilyResource(MR.fonts.TitilliumWeb.regular)
                                UiFontFamily.Default -> FontFamily.Default
                            }
                            Text(
                                text = family.toReadableName(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = fontFamily,
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }
        }
    }
}