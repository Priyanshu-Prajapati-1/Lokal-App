package com.example.lokalapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.lokalapp.screens.bookmarks.BookMarkScreen
import com.example.lokalapp.screens.jobs.JobsScreen
import com.example.lokalapp.utils.AppBottomBarItem
import com.example.lokalapp.utils.AppColor
import com.example.lokalapp.viewmodels.JobViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LokalApp(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    jobViewModel: JobViewModel = hiltViewModel(),
) {

    val isOnline = jobViewModel.isOnline.collectAsState(initial = false)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.LightGray
    ) {

        val scope = rememberCoroutineScope()

        val bottomBarItem = listOf(
            AppBottomBarItem("Jobs", ImageVector.vectorResource(id = R.drawable.email)),
            AppBottomBarItem(
                "Bookmarks",
                ImageVector.vectorResource(id = R.drawable.bookmarks)
            )
        )
        val bottomPagerState = rememberPagerState(pageCount = { bottomBarItem.size })

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            bottomBar = { LokalBottomBar(bottomBarItem, bottomPagerState, scope) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(),
                    visible = !isOnline.value
                ) {
                    Text(
                        text = "No internet connection",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray),
                        style = TextStyle(
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                        )
                    )
                }

                HorizontalPager(
                    state = bottomPagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { index ->
                    when (index) {
                        0 -> JobsScreen(
                            navController = navController,
                            jobViewModel = jobViewModel,
                        )

                        1 -> BookMarkScreen(
                            navController = navController,
                        )
                    }
                }
            }
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LokalBottomBar(
    bottomBarItem: List<AppBottomBarItem>,
    bottomPagerState: PagerState,
    scope: CoroutineScope,
) {
    Column {
        BottomAppBar(
            containerColor = AppColor.ivoryWhite,
            modifier = Modifier
                .height(58.dp)
        ) {
            bottomBarItem.forEachIndexed { index, item ->
                BottomBarItem(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    iconText = item.label,
                    selectedIcon = item.icon,
                    index = index,
                    selectedItemIndex = bottomPagerState.currentPage
                ) {
                    scope.launch {
                        bottomPagerState.animateScrollToPage(index)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarItem(
    modifier: Modifier = Modifier,
    index: Int = 0,
    selectedItemIndex: Int = 0,
    selectedIcon: ImageVector,
    iconText: String,
    onClick: () -> Unit = {},
) {
    val isSelected = index == selectedItemIndex
    val color = if (isSelected) Color.DarkGray else Color.Gray

    val size by animateIntAsState(
        targetValue = if (isSelected) 28 else 20,
        label = ""
    )

    Column(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .background(AppColor.ivoryWhite)
            .padding(horizontal = 30.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) AppColor.yellow else Color.Transparent),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = selectedIcon,
            contentDescription = null,
            modifier = Modifier.size(size.dp),
            tint = color
        )
        Text(
            text = iconText,
            color = color,
            style = TextStyle(fontSize = 12.sp)
        )
    }
}