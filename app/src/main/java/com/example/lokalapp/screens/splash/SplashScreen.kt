package com.example.lokalapp.screens.splash

import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lokalapp.R
import com.example.lokalapp.navigation.LokalScreens
import com.example.lokalapp.utils.AppColor
import com.example.lokalapp.utils.LetterSpace
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {

    LaunchedEffect(Unit) {
        delay(500L)
        navController.navigate(LokalScreens.JobScreen.name) {
            popUpTo(0)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ivoryWhite)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.lokal), contentDescription = "splash",
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.Center)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 70.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.security), contentDescription = null,
                    tint = AppColor.green,
                )

                Text(
                    modifier = Modifier
                        .padding(start = 5.dp),
                    text = "Trusted by 4 Cr+ Indians",
                    style = TextStyle(
                        color = Color.DarkGray,
                        letterSpacing = LetterSpace.mid
                    )
                )

            }

        }

    }

}