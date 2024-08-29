package com.example.lokalapp.screens.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lokalapp.model.JobListModel
import com.example.lokalapp.navigation.LokalScreens
import com.example.lokalapp.response.Resource
import com.example.lokalapp.screens.jobs.ErrorMessage
import com.example.lokalapp.screens.jobs.LoadImage
import com.example.lokalapp.screens.jobs.Loader
import com.example.lokalapp.utils.AppColor
import com.example.lokalapp.viewmodels.BookMarkViewModel
import kotlinx.coroutines.delay

@Composable
fun BookMarkScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bookMarkViewModel: BookMarkViewModel = hiltViewModel(),
) {

    val data = bookMarkViewModel.jobList.collectAsState()

    LaunchedEffect(Unit) {
        bookMarkViewModel.getStoreJob()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.ivoryWhite)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                AppBar("Bookmarks")

                when (data.value) {
                    is Resource.Error -> {
                        ErrorMessage(message = "Something went wrong")
                    }

                    is Resource.Loading -> {
                        Loader()
                    }

                    is Resource.Success -> {
                        if ((data.value as Resource.Success<List<JobListModel>>).data.isEmpty()) {
                            ErrorMessage(message = "No Bookmarks")
                        } else {

                            BookMarkJobList(
                                navController = navController,
                                jobList = (data.value as Resource.Success<List<JobListModel>>).data
                            ) { id ->
                                bookMarkViewModel.deleteJob(id)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(
    title: String = "",
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(horizontal = 15.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 20.sp
            )
        )
    }
}

@Composable
fun BookMarkJobList(
    jobList: List<JobListModel>,
    navController: NavHostController,
    onDeleteClick: (Int) -> Unit = {},
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 12.dp)
    ) {
        items(jobList) {
            BookMarkJobCardView(
                data = it,
                navController = navController
            ) {
                onDeleteClick(it.id ?: 0)
            }
        }
    }
}

@Composable
fun BookMarkJobCardView(
    data: JobListModel,
    navController: NavHostController,
    onDeleteClick: () -> Unit = {},
) {

    var isCardClickable by remember { mutableStateOf(true) }

    LaunchedEffect(
        key1 = isCardClickable
    ) {
        delay(3000)
        isCardClickable = true
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable(enabled = isCardClickable) {
                    if (isCardClickable) {
                        navController.navigate(route = LokalScreens.JobDetailScreen.name + "/${data.id}")
                        isCardClickable = false
                    }
                }
        ) {
            LoadImage(image = data.imageUrl.toString())
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = data.jobRole.toString(),
                            style = TextStyle(
                                color = AppColor.skyBlue,
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
                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable {
                                onDeleteClick()
                            }
                    )
                }
                Text(
                    text = data.company.toString(),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = data.salary.toString(),
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )

            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp),
            color = Color.LightGray
        )
    }
}
