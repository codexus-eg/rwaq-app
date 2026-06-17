package com.khater.rwaq.presentation.screens.profileScreen.composables


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khater.rwaq.designSystem.component.text.Text
import com.khater.rwaq.designSystem.theme.theme.Theme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import qrgenerator.qrkitpainter.PatternType
import qrgenerator.qrkitpainter.QrBallType
import qrgenerator.qrkitpainter.QrFrameType
import qrgenerator.qrkitpainter.QrKitBrush
import qrgenerator.qrkitpainter.QrKitColors
import qrgenerator.qrkitpainter.QrKitLogo
import qrgenerator.qrkitpainter.QrKitLogoKitShape
import qrgenerator.qrkitpainter.QrKitLogoPadding
import qrgenerator.qrkitpainter.QrKitShapes
import qrgenerator.qrkitpainter.QrPixelType
import qrgenerator.qrkitpainter.createCircle
import qrgenerator.qrkitpainter.customBrush
import qrgenerator.qrkitpainter.getSelectedFrameShape
import qrgenerator.qrkitpainter.getSelectedPattern
import qrgenerator.qrkitpainter.getSelectedPixel
import qrgenerator.qrkitpainter.getSelectedQrBall
import qrgenerator.qrkitpainter.rememberQrKitPainter
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.cropped_circle_image
import rwaq.composeapp.generated.resources.ic_apple
import rwaq.composeapp.generated.resources.ic_cancle
import rwaq.composeapp.generated.resources.ic_lock
import rwaq.composeapp.generated.resources.ic_play
import rwaq.composeapp.generated.resources.ic_time
import rwaq.composeapp.generated.resources.logo
import rwaq.composeapp.generated.resources.rwaq_logo

// Assuming you have your QrKit imports here...

@Composable
fun MyGigstersPromoScreen(
    url: String,
    onDismissRequest: () -> Unit
) {
    Surface(
        color = Color(0xFFF2F3F8),
        shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2F3F8)) // Light grayish-blue background
        )
        {
            // NOTE: You can add Canvas() or Image() components here positioned at
            // the corners (TopStart, CenterStart, BottomEnd) to render the abstract background blobs.

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                com.khater.rwaq.designSystem.component.icon.Icon(
                    painter = painterResource(Res.drawable.ic_cancle),
                    contentDescription = "Cancel",
                    modifier = Modifier.size(28.dp)
                        .clickable(
                            onClick = onDismissRequest
                        )
                        .align(Alignment.End)
                )

                // --- TOP LOGO ROW ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    // Replace with your actual MyGigsters logo resource
                    Image(
                        painter = painterResource( Res.drawable.logo),
                        contentDescription = "Rwaq Logo",
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Rwaq app",
                        style = Theme.typography.headline.medium,
                        color = Theme.colorScheme.primary.primary
                    )
                }

                // --- SUBTITLE TEXT ---
                Text(
                    text = "The best coffee app ever! Smarter shop. Better mode",
                    style = Theme.typography.title.large,
                    color = Theme.colorScheme.shadePrimary,
                    modifier = Modifier.padding(vertical = 30.dp),
                    textAlign = TextAlign.Center
                )

                // --- QR CODE ---
                // Wrapped in a slight background if needed, but here it's integrated directly.
                ShareWithQRCode(
                    url = url,
                    modifier = Modifier.size(200.dp) // Scaled up to match the proportions in the design
                )

                // --- BOTTOM SECTION ---
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                ) {
                    Text(
                        text = "Download Rwaq today for Android or iOS",
                        style = Theme.typography.body.small,
                        color = Theme.colorScheme.shadePrimary,                )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- APP STORE BUTTONS ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AppStoreButton(
                            iconRes = Res.drawable.ic_apple, // Replace with your Apple icon
                            text = "App Store"
                        )
                        AppStoreButton(
                            iconRes = Res.drawable.ic_play, // Replace with your Play Store icon
                            text = "Play Store"
                        )
                    }
                }
            }
        }
    }

}

// Reusable App Store Button Composable
@Composable
fun AppStoreButton(iconRes: DrawableResource, text: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        modifier = Modifier
            .height(50.dp)
            .width(150.dp),
        shadowElevation = 2.dp // Adds that subtle drop shadow from the design
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                tint = Theme.colorScheme.brand.brand // Retains the original colors of your icons
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = Theme.typography.title.small,
                color = Theme.colorScheme.brand.brand // Muted blue text color
            )
        }
    }
}
@Composable
fun ShareWithQRCode(
    url: String,
    modifier: Modifier = Modifier
){
    val centerLogo = painterResource(Res.drawable.cropped_circle_image)

    val color = Theme.colorScheme.brand.brand
    val painter = rememberQrKitPainter(url) {
        shapes = QrKitShapes(
            ballShape = getSelectedQrBall(QrBallType.CircleQrBall()),
            darkPixelShape = getSelectedPixel(QrPixelType.CirclePixel()),
            frameShape = getSelectedFrameShape(QrFrameType.CircleFrame()),
            codeShape = getSelectedPattern(PatternType.SquarePattern),
        )
        colors = QrKitColors(
            darkBrush = QrKitBrush.customBrush {
                Brush.linearGradient(
                    0f to color,
                    1f to  color,
                    end = Offset(it, it)
                )
            }
        )

        logo = QrKitLogo(centerLogo,shape =  QrKitLogoKitShape.createCircle(), size = 0.3f, padding = QrKitLogoPadding.Natural(0.2f))
    }
    Image(
        painter = painter, contentDescription = null, modifier = modifier
    )
}

@Composable
fun CaptureComposable(
    modifier: Modifier = Modifier,
    onCaptured: suspend (ImageBitmap) -> Unit,
    content: @Composable () -> Unit
) {
    val graphicsLayer = rememberGraphicsLayer()

    Box(
        modifier = modifier.drawWithContent {
            graphicsLayer.record {
                this@drawWithContent.drawContent()
            }

            drawLayer(graphicsLayer)
        }
    ) {
        content()
    }

    LaunchedEffect(Unit) {
        val image = graphicsLayer.toImageBitmap()

        onCaptured(
            image
        )
    }
}