package com.example.lokalapp.screens.jobs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.lokalapp.R
import com.example.lokalapp.model.JobListModel
import com.example.lokalapp.navigation.LokalScreens
import com.example.lokalapp.response.Resource
import com.example.lokalapp.screens.bookmarks.AppBar
import com.example.lokalapp.utils.AppColor
import com.example.lokalapp.utils.Constants
import com.example.lokalapp.utils.LetterSpace
import com.example.lokalapp.viewmodels.JobViewModel
import kotlinx.coroutines.delay

@Composable
fun JobsScreen(
    navController: NavHostController,
    jobViewModel: JobViewModel,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ivoryWhite)
    ) {

        val jobs = jobViewModel.jobs.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            AppBar("Jobs")

            when (val jobsResource = jobs.value) {
                is Resource.Error -> {
                    ErrorMessage("Something went wrong")
                }

                is Resource.Loading -> {
                    Loader()
                }

                is Resource.Success -> {
                    if (jobsResource.data.isEmpty()) {
                        ErrorMessage("No jobs found")
                    } else {
                        JobList(
                            navController = navController,
                            jobList = jobsResource.data
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Loader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = AppColor.yellow
        )
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 25.dp),
            text = message,
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
fun EmptyMessage(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 25.dp),
            text = message,
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp
            )
        )
    }
}


@Composable
fun JobList(
    navController: NavHostController,
    jobList: List<JobListModel>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(jobList) {
            if (!it.jobRole.isNullOrEmpty()) {
                JobCardView(
                    job = it,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun JobCardView(
    job: JobListModel,
    navController: NavHostController,
) {

    /*val current = LocalDensity.current
    var imageWidth by remember { mutableStateOf(0.dp) }*/

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    var isCardClickable by remember { mutableStateOf(true) }

    LaunchedEffect(
        key1 = isCardClickable
    ) {
        delay(3000)
        isCardClickable = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable(enabled = isCardClickable) {
                if (isCardClickable) {
                    navController.navigate(route = LokalScreens.JobDetailScreen.name + "/${job.id}")
                    isCardClickable = false
                }
            }

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, top = 7.dp, end = 10.dp)
                .clip(RoundedCornerShape(15.dp))
                .border(0.5.dp, Color.Gray.copy(0.5f), RoundedCornerShape(15.dp))
                .padding(10.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = job.jobRole.toString(),
                        style = TextStyle(
                            color = AppColor.skyBlue,
                            fontWeight = FontWeight.W500,
                            fontSize = 16.sp
                        )
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "arrow",
                        tint = AppColor.skyBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = job.company.toString(),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = job.title.toString(),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 13.sp,
                        letterSpacing = LetterSpace.min
                    )
                )
                if (!job.salary.isNullOrEmpty()) {
                    Text(
                        text = job.salary,
                        style = TextStyle(
                            color = AppColor.green,
                            fontSize = 13.sp,
                            letterSpacing = LetterSpace.min
                        )
                    )
                }

            }
            Text(
                text = "${job.vacancies} Vacancies",
                modifier = Modifier
                    .padding(top = 10.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                style = TextStyle(
                    color = Color.DarkGray,
                    fontSize = 12.sp
                )
            )
            HorizontalDivider(
                color = Color.LightGray,
                modifier = Modifier.padding(top = 10.dp, bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            job.whatsappLink?.let { link ->
                                uriHandler.openUri(link)
                            }
                        }
                        .padding(end = 8.dp)
                        .border(
                            width = 1.dp,
                            color = AppColor.yellow,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.whatsapp),
                        contentDescription = "arrow",
                        tint = AppColor.green,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Chat",
                        modifier = Modifier.padding(start = 5.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    )
                }
                Text(
                    text = job.buttonText.toString(),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(5.dp))
                        .background(AppColor.yellow)
                        .clickable {
                            val iCall = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse(job.customLink)
                            }
                            context.startActivity(iCall)
                        }
                        .padding(vertical = 10.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
        LoadImage(
            modifier = Modifier,
            startPadding = 12,
            image = job.imageUrl ?: Constants.LOKAL_IMAGE,
            size = 70,
        )
    }
}

@Composable
fun LoadImage(
    modifier: Modifier = Modifier,
    startPadding: Int = 0,
    image: String,
    size: Int = 70,
    weight: Float = 0f,
) {
    val imageSize = size.dp
    val imageShape = RoundedCornerShape(10.dp)

    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .padding(start = startPadding.dp)
                .size(imageSize)
                .then(
                    if (weight != 0f) Modifier.weight(weight) else Modifier // Apply weight conditionally
                )
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = imageShape
                )
                .clip(imageShape)
                .background(AppColor.oilyWhite),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 15.dp)
            )
            SubcomposeAsyncImage(
                model = image,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                loading = {
                    CircularProgressIndicator(
                        color = AppColor.yellow,
                        modifier = Modifier
                            .size(25.dp)
                            .padding(15.dp),
                        strokeWidth = 1.5.dp
                    )
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}
