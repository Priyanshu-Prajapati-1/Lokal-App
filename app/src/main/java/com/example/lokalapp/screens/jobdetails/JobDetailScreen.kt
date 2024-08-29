package com.example.lokalapp.screens.jobdetails

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lokalapp.R
import com.example.lokalapp.model.Result
import com.example.lokalapp.response.Resource
import com.example.lokalapp.screens.jobs.ErrorMessage
import com.example.lokalapp.screens.jobs.LoadImage
import com.example.lokalapp.screens.jobs.Loader
import com.example.lokalapp.utils.AppColor
import com.example.lokalapp.utils.Constants
import com.example.lokalapp.utils.LetterSpace
import com.example.lokalapp.viewmodels.BookMarkViewModel
import com.example.lokalapp.viewmodels.JobDetailViewModel
import kotlinx.coroutines.delay

@Composable
fun JobDetailScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    jobId: String,
    jobDetailViewModel: JobDetailViewModel = hiltViewModel(),
    bookMarkViewModel: BookMarkViewModel = hiltViewModel(),
) {

    val current = LocalDensity.current

    val isStoreInDB = bookMarkViewModel.isStoreInDB.collectAsState()
    val jobDetail = jobDetailViewModel.jobs.collectAsState()
    var bottomComposeHeight by remember { mutableStateOf(0.dp) }

    val isReflectDb = remember {
        mutableStateOf(false)
    }

    val data: MutableState<Result?> = remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = true) {
        delay(300)
        jobDetailViewModel.getJobs()
    }

    LaunchedEffect(key1 = isReflectDb.value) {
        bookMarkViewModel.getJobById(jobId.toInt())
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ivoryWhite)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomComposeHeight),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            DetailHeader(
                navController = navController,
                isStoreInDB = isStoreInDB
            ) {
                if (isStoreInDB.value) {
                    bookMarkViewModel.deleteJob(data.value?.id ?: 0)
                } else {
                    jobDetailViewModel.insertJob(data.value?.id ?: 0)
                }
                isReflectDb.value = !isReflectDb.value
            }

            when (val jobsResource = jobDetail.value) {
                is Resource.Error -> {
                    ErrorMessage(jobsResource.message)
                }

                is Resource.Loading -> {
                    Loader()
                }

                is Resource.Success -> {
                    data.value = jobsResource.data.results.find { it.id.toString() == jobId }
                    data.value?.let { JobDetailView(data = it) }
                }
            }


        }
        data.value?.let {
            BottomAction(
                data = it,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 10.dp)
                    .onGloballyPositioned { coordinates ->
                        bottomComposeHeight =
                            with(current) { coordinates.size.height.toDp() }
                    }
            )
        }
    }
}

@Composable
private fun DetailHeader(
    navController: NavHostController,
    isStoreInDB: State<Boolean>,
    onClick: () -> Unit,
) {
    val icon = if (isStoreInDB.value) {
        R.drawable.bookmark
    } else {
        R.drawable.bookmark_border
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = "Job Details",
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 20.sp,
            )
        )
        IconButton(onClick = {
            onClick()
        }) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.DarkGray
            )
        }
    }
}

@Composable
fun JobDetailView(data: Result) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val imageFile = data.creatives?.firstOrNull()?.file ?: Constants.LOKAL_IMAGE
                    LoadImage(image = imageFile, size = 200, weight = 1f)
                }
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = data.job_role,
                    style = TextStyle(
                        color = AppColor.skyBlue,
                        fontWeight = FontWeight.W400,
                        fontSize = 20.sp
                    )
                )

                Text(
                    text = data.company_name,
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = data.title,
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = "${data.openings_count} Vacancies",
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
            }

            HorizontalDivider(
                color = AppColor.yellow,
                modifier = Modifier.padding(15.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 10.dp)
            ) {
                Text(text = "Job details")
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        JobDetailRow(
                            tag = "Native place",
                            icon = Icons.Default.LocationOn,
                            value = data.primary_details?.Place.toString()
                        )
                        JobDetailRow(
                            tag = "Experience",
                            icon = ImageVector.vectorResource(id = R.drawable.experience),
                            value = data.primary_details?.Experience ?: "No experience"
                        )
                        JobDetailRow(
                            tag = "job Hours",
                            icon = ImageVector.vectorResource(id = R.drawable.time),
                            value = data.job_hours
                        )
                        JobDetailRow(
                            tag = "Gender",
                            icon = Icons.Default.AccountCircle,
                            value = "Any gender"
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        JobDetailRow(
                            tag = "Salary",
                            icon = ImageVector.vectorResource(id = R.drawable.salary),
                            value = data.primary_details?.Salary.toString()
                        )
                        JobDetailRow(
                            tag = "Qualifications",
                            icon = ImageVector.vectorResource(id = R.drawable.qualification),
                            value = data.primary_details?.Qualification ?: "No qualification"
                        )
                        JobDetailRow(
                            tag = "Shift time",
                            icon = ImageVector.vectorResource(id = R.drawable.shift),
                            value = data.shift_timing.toString()
                        )
                        JobDetailRow(
                            tag = "Openings",
                            icon = ImageVector.vectorResource(id = R.drawable.openings),
                            value = data.openings_count.toString()
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
                text = data.other_details.trimIndent(),
                style = TextStyle(
                    color = Color.DarkGray,
                    fontSize = 13.sp
                )
            )
        }
    }
}

@Composable
fun JobDetailRow(
    tag: String = "",
    icon: ImageVector? = null,
    value: String = "",
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top = 15.dp, start = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon!!, contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(15.dp)
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = tag,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray,
                    letterSpacing = LetterSpace.mid
                ),
            )
        }
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = value,
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.DarkGray,
                letterSpacing = LetterSpace.mid
            ),
        )
    }
}

@Composable
private fun BottomAction(
    data: Result,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    data.contact_preference?.whatsapp_link?.let { link ->
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
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        Text(
            text = data.button_text.toString(),
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(5.dp))
                .clickable {
                    val iCall = Intent(Intent.ACTION_DIAL)
                    iCall.setData(Uri.parse(data.custom_link))
                    context.startActivity(iCall)
                }
                .background(AppColor.yellow)
                .padding(vertical = 10.dp),
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}
