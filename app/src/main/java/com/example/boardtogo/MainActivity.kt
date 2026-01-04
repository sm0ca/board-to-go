package com.example.boardtogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boardtogo.ui.theme.BoardToGOTheme

const val HEADER_ROW_FONT_SIZE = 12
const val TRIP_ROW_FONT_SIZE = 16

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BoardToGOTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                            Snackbar(
                                snackbarData = snackbarData,
                                shape = RoundedCornerShape(4.dp),
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                    }
                ) { innerPadding ->
                    UnionBoard(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = DeparturesViewModel(),
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnionBoard(
    modifier: Modifier = Modifier,
    viewModel: DeparturesViewModel,
    snackbarHostState: SnackbarHostState
) {

    val message = viewModel.snackbarMessage

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.resetSnackbar()
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(24.dp),
    ) {
        HeaderRow()

        viewModel.allTrips?.let { allTrips ->
            PullToRefreshBox(
                isRefreshing = viewModel.isRefreshing,
                onRefresh = viewModel::refreshDepartures,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = if (allTrips.isEmpty())
                        Arrangement.Center else Arrangement.Top,
                ) {
                    if (allTrips.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_trips_placeholder),
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier,
                        )
                    } else {
                        TripRows(allTrips)
                    }
                }
            }
        } ?: Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

@Composable
private fun HeaderRow() {
    Column {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.railway_departure),
                contentDescription = "Railway departure icon",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(R.string.union_go_departures_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.time_title),
                fontSize = HEADER_ROW_FONT_SIZE.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = stringResource(R.string.service_line_title),
                fontSize = HEADER_ROW_FONT_SIZE.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.platform_title),
                fontSize = HEADER_ROW_FONT_SIZE.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
            )

        }

        HorizontalDivider()
    }

}

@Composable
private fun TripRows(allTrips: List<Trip>) {
    allTrips.forEachIndexed { idx, item ->
        val serviceLine = ServiceLine.fromFullName(item.service)
        if (idx != 0) {
            HorizontalDivider()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = item.time.parseTimeStamp(),
                fontSize = TRIP_ROW_FONT_SIZE.sp,
                fontWeight = FontWeight.Bold,
//                style = LocalTextStyle.current.copy(fontFeatureSettings = "tnum"),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = serviceLine.bgColor,
                        shape = RoundedCornerShape(size = 4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = serviceLine.abbreviation,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = serviceLine.fullName,
                fontSize = TRIP_ROW_FONT_SIZE.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = item.platform.parsePlatform(),
                fontSize = TRIP_ROW_FONT_SIZE.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnionBoardPreview() {
    BoardToGOTheme {
        UnionBoard(
            modifier = Modifier,
            viewModel = DeparturesViewModel(),
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

fun String?.parseTimeStamp() = this
    ?.split(' ')
    ?.last()?.dropLast(3) // Drop the seconds from the time val
    ?: "00:00"
fun String?.parsePlatform() = this
    ?.split('/')[0]?.trim()
    ?: "..."
