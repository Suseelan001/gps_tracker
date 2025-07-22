package com.locationReminder

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.locationReminder.ui.theme.Hex36374b
import com.locationReminder.view.appNavigation.NavigationRoute
import kotlinx.coroutines.launch
import com.locationReminder.viewModel.SharedPreferenceVM
import kotlinx.coroutines.coroutineScope


@OptIn(ExperimentalPagerApi::class)
@Composable
fun SplashScreen(
    navHostController: NavHostController?,sharedPreferenceVM:SharedPreferenceVM) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    println("CHECK_TAG_SPLASHSCREEN__ " + " SplashScreen " )

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            count = 5,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> WelcomeScreen1 ( pagerState)
                1 -> WelcomeScreen2(R.drawable.splashscreen_two, "Drop a Pin to Begin", "Tap on map to drop pin at the location", pagerState)
                2 -> WelcomeScreen3(R.drawable.splashscreen_three, "Mark your spots", "Tab the +icon to set entry and exit point", pagerState)
                3 -> WelcomeScreen4(R.drawable.splashscreen_four, "Label your pin", "Customize radius and title for your marked location", pagerState)
                4 -> WelcomeScreen5 {
                    sharedPreferenceVM.setNewUser(false)
                  redirectFromLoginScreen(navHostController,NavigationRoute.SPLASHSCREEN.path)

                }
            }
        }


    }





}


fun redirectFromLoginScreen(navHostController: NavHostController?, currentStack:String) {
    navHostController?.navigate(NavigationRoute.HOMESCREEN.path){
        popUpTo(currentStack){
            inclusive=true
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen1(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF36374B))) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)) {
            Image(
                painter = painterResource(id = R.drawable.splashscreen_one),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 438.dp)
            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
            .background(Color(0xFF222227))) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(102.dp))

                Text("Welcome to Alert Go", fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Set location and get alarm when you reach", fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.height(32.dp))
                CircularArrowButton(
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(1) }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen2(imageRes: Int, title: String, subtitle: String, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF000000))) {
        Column(modifier = Modifier
            .fillMaxWidth()) {
            Image(painter = painterResource(id = imageRes), contentDescription = null, modifier = Modifier.fillMaxWidth())
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 438.dp)
            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
            .background(Color(0xFF222227))) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(102.dp))

                Text(title, fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(subtitle, fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.height(32.dp))




                CircularArrowButton(arcLength=144f,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(2) }
                    })

            }
        }
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen3(imageRes: Int, title: String, subtitle: String, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF000000))) {
        Column(modifier = Modifier
            .fillMaxWidth()) {
            Image(painter = painterResource(id = imageRes), contentDescription = null, modifier = Modifier.fillMaxWidth())
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 438.dp)
            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
            .background(Color(0xFF222227))) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(102.dp))

                Text(title, fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(subtitle, fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.height(32.dp))

                CircularArrowButton(arcLength=216f,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(3) }
                    })


            }
        }
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen4(imageRes: Int, title: String, subtitle: String, pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF000000))) {
        Column(modifier = Modifier
            .fillMaxWidth()) {
            Image(painter = painterResource(id = imageRes), contentDescription = null, modifier = Modifier.fillMaxWidth())
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 438.dp)
            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
            .background(Color(0xFF222227))) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(102.dp))

                Text(title, fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))
                Text(subtitle, fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.height(32.dp))


                CircularArrowButton(arcLength=288f,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(4) }
                    })

            }
        }
    }
}

@Composable
fun WelcomeScreen5(onGetStartedClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Hex36374b)) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp)) {
            Image(
                painter = painterResource(id = R.drawable.splashscreen_five),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(top = 438.dp)
            .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
            .background(Color(0xFF222227))) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(132.dp))

                Text("You're Ready to Go", fontSize = 24.sp, color = Color.White)
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onGetStartedClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEEF267))
                ) {
                    Text("GET STARTED", color = Color.Black)
                }

            }
        }
    }
}

@Composable
fun CircularArrowButton(
    startAngle: Float=270f,
    arcLength: Float=72f,
    onClick: () -> Unit = {}
) {
    val strokeWidth = 3.dp

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(70.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = size.minDimension
            val arcInset = strokeWidth.toPx() / 2f + 6f

            drawArc(
                color = Color(0xFFEEF267),
                startAngle = startAngle,
                sweepAngle = arcLength,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                size = Size(canvasSize - arcInset * 2, canvasSize - arcInset * 2),
                topLeft = Offset(arcInset, arcInset)
            )
        }

        // Inner white circle with arrow
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onClick() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Arrow",
                tint = Color(0xFF2C2B33),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
