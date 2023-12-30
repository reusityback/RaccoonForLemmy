package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
actual fun CustomImage(
    modifier: Modifier,
    url: String,
    autoload: Boolean,
    loadButtonContent: @Composable (() -> Unit)?,
    contentDescription: String?,
    quality: FilterQuality,
    contentScale: ContentScale,
    alignment: Alignment,
    contentAlignment: Alignment,
    alpha: Float,
    colorFilter: ColorFilter?,
    onLoading: @Composable (BoxScope.(Float?) -> Unit)?,
    onFailure: @Composable (BoxScope.(Throwable) -> Unit)?,
) {
    var shouldBeRendered by remember(autoload) { mutableStateOf(autoload) }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        if (shouldBeRendered) {
            var painterState: AsyncImagePainter.State by remember {
                mutableStateOf(AsyncImagePainter.State.Empty)
            }
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = url,
                contentDescription = contentDescription,
                filterQuality = quality,
                contentScale = contentScale,
                alignment = alignment,
                alpha = alpha,
                colorFilter = colorFilter,
                onLoading = {
                    painterState = it
                },
                onError = {
                    painterState = it
                },
                onSuccess = {
                    painterState = it
                }
            )

            when (val state = painterState) {
                AsyncImagePainter.State.Empty -> Unit
                is AsyncImagePainter.State.Error -> {
                    onFailure?.invoke(this, state.result.throwable)
                }

                is AsyncImagePainter.State.Loading -> {
                    onLoading?.invoke(this, null)
                }

                else -> Unit
            }
        } else {
            Button(
                onClick = {
                    shouldBeRendered = true
                },
            ) {
                if (loadButtonContent != null) {
                    loadButtonContent.invoke()
                } else {
                    Text(
                        text = stringResource(MR.strings.button_load),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}