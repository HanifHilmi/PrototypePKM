package com.pkm.PrototypePKM.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pkm.PrototypePKM.R
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun SplashScreenContent(onSplashDone: () -> Unit={}) {

    val durationMillis = 1000L

    val visibleState = remember { mutableStateOf(true) }
    val alpha by animateFloatAsState(
        targetValue = if (visibleState.value) 1f else 0f,
        animationSpec = tween(200), label = ""
    )

    LaunchedEffect(true) {
        delay(durationMillis)
        onSplashDone()

    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.logo_pkm),
            contentDescription = "Logo",
            modifier = Modifier
                .width(250.dp)
                .alpha(alpha)

        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Di dukung oleh:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row{
                Image(
                    painter = painterResource(R.drawable.logo_bmkg),
                    contentDescription = "BMKG Logo",
                    modifier = Modifier
                        .size(62.dp)
                        .padding(top = 8.dp)
                        .offset(x = 8.dp)
                )
                Image(
                    painter = painterResource(R.drawable.logo_stmkg),
                    contentDescription = "STMKG Logo",
                    modifier = Modifier
                        .size(62.dp)
                        .padding(top = 8.dp)
                        .offset(x = 8.dp)
                )
            }
        }
    }
}