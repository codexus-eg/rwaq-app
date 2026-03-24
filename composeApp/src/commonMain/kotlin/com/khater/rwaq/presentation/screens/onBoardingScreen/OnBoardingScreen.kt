package com.khater.rwaq.presentation.screens.onBoardingScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khater.rwaq.designSystem.component.button.OutlinedButton
import com.khater.rwaq.designSystem.component.button.PrimaryButton
import com.khater.rwaq.designSystem.component.scaffold.RwaqScaffold
import com.khater.rwaq.designSystem.theme.theme.Theme
import com.khater.rwaq.presentation.composables.RwaqPagerIndicator
import com.khater.rwaq.presentation.navigation.Screen
import com.khater.rwaq.presentation.screens.onBoardingScreen.uiState.OnBoardingPage
import com.khater.rwaq.presentation.screens.onBoardingScreen.uiState.OnboardingData
import com.khater.rwaq.presentation.util.LocalNavigationProvider
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.create_account
import rwaq.composeapp.generated.resources.discover_purity
import rwaq.composeapp.generated.resources.fresh_start
import rwaq.composeapp.generated.resources.img_coffee_1
import rwaq.composeapp.generated.resources.img_coffee_2
import rwaq.composeapp.generated.resources.img_coffee_3
import rwaq.composeapp.generated.resources.login
import rwaq.composeapp.generated.resources.logo
import rwaq.composeapp.generated.resources.next
import rwaq.composeapp.generated.resources.on_boarding_desc_1
import rwaq.composeapp.generated.resources.on_boarding_desc_2
import rwaq.composeapp.generated.resources.on_boarding_desc_3
import rwaq.composeapp.generated.resources.onboarding_desc_1
import rwaq.composeapp.generated.resources.onboarding_desc_2
import rwaq.composeapp.generated.resources.onboarding_desc_3
import rwaq.composeapp.generated.resources.onboarding_title_1
import rwaq.composeapp.generated.resources.onboarding_title_2
import rwaq.composeapp.generated.resources.onboarding_title_3
import rwaq.composeapp.generated.resources.rwaq
import rwaq.composeapp.generated.resources.skip
import rwaq.composeapp.generated.resources.smart_purification
import rwaq.composeapp.generated.resources.start_now




@Composable
fun OnBoardingScreen(
) {
    val navController = LocalNavigationProvider.current
    // 1. Setup Data
    val pages = listOf(
        OnBoardingPage(Res.drawable.img_coffee_1, Res.string.onboarding_title_1, Res.string.onboarding_desc_1),
        OnBoardingPage(Res.drawable.img_coffee_2, Res.string.onboarding_title_2, Res.string.onboarding_desc_2),
        OnBoardingPage(Res.drawable.img_coffee_3, Res.string.onboarding_title_3, Res.string.onboarding_desc_3)
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLastPage by remember { derivedStateOf { pagerState.currentPage == pages.size - 1 } }

    // Define the specific Beige color from your image
    // You should probably add this to your Theme colors, but here is the hex code matching the image
    val BottomSheetColor = Color(0xFFFBF7F0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Top background is white
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .fillMaxHeight(0.65f)
         ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Logo / Name
            Text(
                text = stringResource(Res.string.rwaq),
                style = Theme.typography.headline.medium, // Adjust to your Serif font style
                color = Theme.colorScheme.primary.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // The Pager handles ONLY the Image swiping
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource( pages[page].imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                           ,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

         Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.45f), // Occupies bottom 45%
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = BottomSheetColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top= 32.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
             ) {

                AnimatedContent(
                    targetState = pagerState.currentPage,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) + slideInVertically { it / 4 })
                            .togetherWith(fadeOut(animationSpec = tween(300)) + slideOutVertically { -it / 4 })
                    },
                    label = "TextAnim"
                ) { index ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val rawTitle = stringResource(pages[index].titleRes)

                        // 2. Convert to AnnotatedString with your specific Highlight Color
                        // (e.g., Use Brown/Brand color for the highlighted word)
                        val styledTitle = getHighlightedString(
                            text = rawTitle,
                            highlightColor = Color(0xFFC79C63) // Or Theme.colorScheme.primary
                        )

                        Text(
                            text = styledTitle,
                            style = Theme.typography.headline.large.copy(
                                fontSize = 24.sp
                            ),
                            textAlign = TextAlign.Start,
                            color = Theme.colorScheme.primary.primary,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource( pages[index].descRes),
                            style = Theme.typography.body.medium.copy(
                                fontSize = 18.sp
                            ),
                            textAlign = TextAlign.Start,
                            color = Color(0xFF5B5B5B),
                            modifier = Modifier.fillMaxWidth()

                        )
                    }
                }

                // 2. Indicators
                RwaqPagerIndicator(indicatorSize = pages.size,pagerState = pagerState, modifier = Modifier.padding(top = 24.dp))

                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AnimatedContent(
                        targetState = isLastPage,
                        label = "ButtonAnim"
                    ) { isLast ->
                        if (isLast) {
                            // --- Last Page: Login & Create Account (Vertical Stack) ---
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                        PrimaryButton(
                                    text = stringResource(Res.string.login),
                                    onClick = {navController.navigate(Screen.LoginScreen)},
                                            modifier = Modifier.fillMaxWidth(),

                                 )

//                                // To match the White button in your image,
//                                // we can customize PrimaryButton or use OutlinedButton with white bg
//                                PrimaryButton(
//                                    text = stringResource(Res.string.create_account),
//                                    onClick = {navController.navigate(Screen.RegisterScreen)},
//                                    modifier = Modifier.fillMaxWidth(),
//                                    containerColor = Color.White,
//                                    contentColor = Color.Black
//                                )
                            }
                        } else {
                            // --- Normal Pages: Skip & Next (Horizontal Row) ---
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Skip (Transparent/Text only)
                                PrimaryButton(
                                    text = stringResource(Res.string.next),
                                    onClick = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                   contentColor = Color.White
                                )
                                Spacer(modifier = Modifier.width(16.dp))

                                OutlinedButton(
                                    text = stringResource(Res.string.skip),
                                    onClick = {navController.navigate(Screen.LoginScreen)},
                                    modifier = Modifier.weight(1f),
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF474747),
                                    buttonHeight =  52.dp
                                    // Hack: If OutlinedButton forces a border, we might need 
                                    // to adjust the component or pass transparent border color
                                    // Assuming your component allows color overrides via Theme or modifier
                                )



                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getHighlightedString(
    text: String,
    highlightColor: Color = Theme.colorScheme.brand.brand // Your generic brand color
): AnnotatedString {
    return buildAnnotatedString {
        // Split by "*". Even indices are normal text, Odd indices are highlighted.
        val parts = text.split("*")

        parts.forEachIndexed { index, part ->
            if (index % 2 == 1) {
                // This is the text INSIDE the asterisks -> Apply Color
                withStyle(style = SpanStyle(color = highlightColor)) {
                    append(part)
                }
            } else {
                // This is the text OUTSIDE -> Default style
                append(part)
            }
        }
    }
}


//@Composable
//fun OnBoardingScreen() {
//
//    val navController = LocalNavigationProvider.current
//
//    // Define your pages here
//    val pages = listOf(
//        OnBoardingPage(
//            imageRes = Res.drawable.img_coffee_1, // Replace with your drawable IDs
//            titleRes = Res.string.onboarding_title_1,
//            descRes = Res.string.onboarding_desc_1
//        ),
//        OnBoardingPage(
//            imageRes = Res.drawable.img_coffee_2,
//            titleRes = Res.string.onboarding_title_2,
//            descRes = Res.string.onboarding_desc_2
//        ),
//        OnBoardingPage(
//            imageRes = Res.drawable.img_coffee_3,
//            titleRes = Res.string.onboarding_title_3,
//            descRes = Res.string.onboarding_desc_3
//        )
//    )
//
//    val pagerState = rememberPagerState(pageCount = { pages.size })
//    val scope = rememberCoroutineScope()
//
//    // Logic to determine if we are on the last page
//    val isLastPage by remember { derivedStateOf { pagerState.currentPage == pages.size - 1 } }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White) // Background color matches image
//            .padding(vertical = 24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        // --- Top Section: Header ---
//        Text(
//            text = "On Boarding", // Or localized app name "Rwaq"
//            style = Theme.typography.label.medium,
//            color = Color.Gray,
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(horizontal = 24.dp)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // --- Middle Section: Pager (Images) ---
//        HorizontalPager(
//            state = pagerState,
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxWidth()
//        ) { page ->
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier.padding(horizontal = 32.dp)
//            ) {
//                // Main Image
//                Image(
//                    painter = painterResource(pages[page].imageRes),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(0.8f), // Adjust aspect ratio based on your assets
//                    contentScale = ContentScale.Fit
//                )
//            }
//        }
//
//        // --- Lower Section: Text & Buttons ---
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.padding(horizontal = 24.dp)
//        ) {
//
//            // Animated Text Switching
//            // This creates the "Cool" effect where text fades/slides vertically
//            AnimatedContent(
//                targetState = pagerState.currentPage,
//                transitionSpec = {
//                    (fadeIn() + slideInVertically { it / 2 }).togetherWith(
//                        fadeOut() + slideOutVertically { -it / 2 }
//                    )
//                },
//                label = "TextAnimation"
//            ) { pageIndex ->
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(
//                        text = stringResource(pages[pageIndex].titleRes),
//                        style = Theme.typography.title.large, // Use your Theme font
//                        textAlign = TextAlign.Center,
//                        color = Color.Black // Or Theme.colorScheme.text
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = stringResource(pages[pageIndex].descRes),
//                        style = Theme.typography.body.medium, // Use your Theme font
//                        textAlign = TextAlign.Center,
//                        color = Color.Gray
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Page Indicators
//            Row(
//                horizontalArrangement = Arrangement.Center,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                repeat(pages.size) { iteration ->
//                    val color = if (pagerState.currentPage == iteration)
//                        Theme.colorScheme.brand.brand // Active color
//                    else
//                        Theme.colorScheme.disabled // Inactive color
//
//                    Box(
//                        modifier = Modifier
//                            .padding(4.dp)
//                            .clip(CircleShape)
//                            .background(color)
//                            .size(if (pagerState.currentPage == iteration) 12.dp else 8.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // --- Dynamic Buttons Section ---
//            // Animates between the Row (Next/Skip) and the Column (Login/Create)
//            AnimatedContent(
//                targetState = isLastPage,
//                label = "ButtonAnimation"
//            ) { isLast ->
//                if (isLast) {
//                    // LAST PAGE LAYOUT: Column of buttons
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        PrimaryButton(
//                            text = stringResource(Res.string.login),
//                            modifier = Modifier.fillMaxWidth(),
//                            onClick = {navController.navigate(Screen.LoginScreen)}
//                        )
//                        OutlinedButton(
//                            text = stringResource(Res.string.create_account),
//                            modifier = Modifier.fillMaxWidth(),
//                            onClick = {navController.navigate(Screen.RegisterScreen)}
//                        )
//                    }
//                } else {
//                    // NORMAL PAGES LAYOUT: Row of buttons
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        // Skip Button (Outlined)
//                        OutlinedButton(
//                            text = stringResource(Res.string.skip),
//                            modifier = Modifier.weight(1f),
//                            shape = CircleShape, // Matches image
//                            onClick = {navController.navigate(Screen.LoginScreen)} // Skip goes to login?
//                        )
//
//                        Spacer(modifier = Modifier.width(16.dp))
//
//                        // Next Button (Primary)
//                        PrimaryButton(
//                            text = stringResource(Res.string.next),
//                            modifier = Modifier.weight(1f),
//                            shape = CircleShape, // Matches image
//                            onClick = {
//                                scope.launch {
//                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
//                                }
//                            }
//                        )
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(16.dp))
//        }
//    }
//}
//








data class OnboardingPageData(
    val data: OnboardingData,
    val buttonText: String? = null,
    val customButtons: (@Composable () -> Unit)? = null
)



//@Composable
//fun OnBoardingScreen() {
//    val navController = LocalNavigationProvider.current
//    val coroutine = rememberCoroutineScope()
//
//    val pages = listOf(
//        OnboardingPageData(
//            data = OnboardingData(
//                image = painterResource(Res.drawable.logo),
//                title = stringResource(Res.string.discover_purity),
//                description = stringResource(Res.string.on_boarding_desc_1)
//            ),
//            buttonText = stringResource(Res.string.next)
//        ),
//        OnboardingPageData(
//            data = OnboardingData(
//                image = painterResource(Res.drawable.logo),
//                title = stringResource(Res.string.fresh_start),
//                description = stringResource(Res.string.on_boarding_desc_2)
//            ),
//            buttonText = stringResource(Res.string.start_now)
//        ),
//        OnboardingPageData(
//            data = OnboardingData(
//                image = painterResource(Res.drawable.logo),
//                title = stringResource(Res.string.smart_purification),
//                description = stringResource(Res.string.on_boarding_desc_3)
//            ),
//            customButtons = {
//
//                CustomAuthButtons(
//                    onLoginClicked = { navController.navigate(Screen.LoginScreen) },
//                    onRegisterClicked = { navController.navigate(Screen.RegisterScreen) },
//                    onGoogleLoginClicked = { /* TODO: Google login */ }
//                )
//
//            }
//        )
//    )
//
//    val pagerState = rememberPagerState(
//        initialPage = 0,
//        pageCount = pages::size
//    )
//
//    RwaqScaffold(content = {
//        Column(
//            modifier = Modifier
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            HorizontalPager(
//                state = pagerState,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            ) { index ->
//                val pageData = pages[index]
//                val isLastPage = index == pages.lastIndex
//
//                OnboardingPage(
//                    data = pageData.data,
//                    buttonText = pageData.buttonText,
//                    customButtons = pageData.customButtons,
//                    showIndicator = !isLastPage,
//                    pagerState = pagerState,
//                    indicatorSize = pages.size,
//                    onButtonClick = {
//                        if (!isLastPage) {
//                            coroutine.launch {
//                                pagerState.animateScrollToPage(index + 1)
//                            }
//                        }
//                    }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(40.dp))
//        }
//    }
//    )
//
//}

@Composable
fun CustomAuthButtons(
    onLoginClicked:()-> Unit,
    onRegisterClicked:()-> Unit,
    onGoogleLoginClicked:()-> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

//        OutlinedButton(
//            text = stringResource(Res.string.continue_with_google),
//            onClick = onGoogleLoginClicked,
//            modifier = Modifier.fillMaxWidth(),
//            trailingIcon = painterResource(Res.drawable.google)
//        )
        OutlinedButton(
            text = stringResource(Res.string.create_account),
            onClick = onRegisterClicked,
            modifier = Modifier.fillMaxWidth().height(56.dp),

            )
        PrimaryButton(
            text = stringResource(Res.string.login),
            onClick = onLoginClicked,
            modifier = Modifier.fillMaxWidth(),
        )

    }
}

@Composable
fun OnboardingPage(
    data: OnboardingData,
    buttonText: String? = null,
    customButtons: (@Composable () -> Unit)? = null,
    showIndicator: Boolean = false,
    pagerState: PagerState? = null,
    indicatorSize: Int = 0,
    onButtonClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(
            painter = data.image,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 93.dp)
                .size(224.dp)
        )

         Spacer(modifier = Modifier.weight(1f))
         if (showIndicator && pagerState != null) {
            RwaqPagerIndicator(
                modifier = Modifier.padding(vertical = Theme.spacing._24),
                indicatorSize = indicatorSize.minus(1),
                pagerState = pagerState,
            )
         }
        Text(
            text = data.title,
            style = Theme.typography.headline.large,
            textAlign = TextAlign.Center,
            color = Theme.colorScheme.primary.primary,
            modifier = Modifier.padding(vertical = Theme.spacing._8)
        )



        Text(
            text = data.description,
            style = Theme.typography.title.large,
            textAlign = TextAlign.Center,
            color = Theme.colorScheme.secondary.secondaryText,
            modifier = Modifier
                .padding(start =Theme.spacing._8, end = Theme.spacing._8, bottom = Theme.spacing._32)
        )


         if (customButtons != null) {
            customButtons()
        } else if (buttonText != null) {
            PrimaryButton(
                text = buttonText,
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
            )

        }

        Spacer(modifier = Modifier.height(82.dp))
    }
}