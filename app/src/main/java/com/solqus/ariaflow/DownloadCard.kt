package com.solqus.ariaflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CardBackground = Color(0xFFFFE4F4)
private val PrimaryText = Color(0xFF401047)
private val SecondaryText = Color(0xFF85558B)
private val ProgressTrack = Color(0xFFE8CBE1)
private val ProgressColor = Color(0xFF70246F)

@Composable
fun DownloadCard(
    item: DownloadItem,
    onPauseResume: () -> Unit
) {
    val progressPercent = (item.progress * 100).toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = ProgressTrack,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "↓",
                        color = ProgressColor,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.name,
                        color = PrimaryText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "${formatBytes(item.downloadedBytes)} / " +
                                formatBytes(item.totalBytes),
                        color = SecondaryText,
                        fontSize = 12.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${formatBytes(item.speedBytesPerSecond)}/s",
                        color = PrimaryText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "$progressPercent%",
                        color = PrimaryText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            LinearProgressIndicator(
                progress = { item.progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = ProgressColor,
                trackColor = ProgressTrack,
                drawStopIndicator = {}
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when {
                        item.downloadedBytes >= item.totalBytes ->
                            "Completed"

                        item.isPaused -> "Paused"
                        else -> "Downloading"
                    },
                    color = SecondaryText,
                    fontSize = 12.sp
                )

                TextButton(
                    onClick = onPauseResume,
                    enabled = item.downloadedBytes < item.totalBytes
                ) {
                    Text(
                        text = if (item.isPaused) "▶ Resume" else "Ⅱ Pause",
                        color = ProgressColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

private fun formatBytes(bytes: Long): String {
    val megabyte = 1024.0 * 1024.0
    val gigabyte = 1024.0 * 1024.0 * 1024.0

    return when {
        bytes >= gigabyte ->
            "%.2f GB".format(bytes / gigabyte)

        bytes >= megabyte ->
            "%.0f MB".format(bytes / megabyte)

        else -> "$bytes B"
    }
}

