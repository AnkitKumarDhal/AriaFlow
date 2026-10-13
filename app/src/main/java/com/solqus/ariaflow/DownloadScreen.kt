package com.solqus.ariaflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PageBackground = Color(0xFFFFF0F8)
private val CardBackground = Color(0xFFFFE4F4)
private val PrimaryText = Color(0xFF401047)
private val SecondaryText = Color(0xFF85558B)
private val Accent = Color(0xFF9B4D96)

@Composable
fun DownloadsScreen() {
    val downloads = remember {
        mutableStateListOf(
            DownloadItem(
                id = 1,
                name = "android-studio-2026.1.tar.gz",
                totalBytes = 1800L * 1024 * 1024,
                downloadedBytes = 1116L * 1024 * 1024,
                speedBytesPerSecond = 8408L * 1024,
                isPaused = false
            ),
            DownloadItem(
                id = 2,
                name = "archlinux-iso-2026.10.13.tar.gz",
                totalBytes = 1800L * 1024 * 1024,
                downloadedBytes = 1116L * 1024 * 1024,
                speedBytesPerSecond = 8408L * 1024,
                isPaused = false
            ),
            DownloadItem(
                id = 3,
                name = "blender.2026.tar.gz",
                totalBytes = 1800L * 1024 * 1024,
                downloadedBytes = 1800L * 1024 * 1024,
                speedBytesPerSecond = 8408L * 1024,
                isPaused = false
            )
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PageBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 12.dp).statusBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            AppHeader()
            TransferOverview(downloads = downloads)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = downloads,
                    key = {download -> download.id}
                ) {
                    download -> DownloadCard(
                        item = download,
                        onPauseResume = {
                            val index = downloads.indexOfFirst {
                                it.id == download.id
                            }

                            if (index != -1) {
                                val currentDownload = downloads[index]
                                downloads[index] = currentDownload.copy(
                                    isPaused = !currentDownload.isPaused,
                                    speedBytesPerSecond = if (currentDownload.isPaused) 8400L * 1024 else 0L
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "AriaFlow",
            color = PrimaryText,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = CardBackground,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚙",
                color = PrimaryText,
                fontSize = 22.sp
            )
        }
    }
}

@Composable
private fun TransferOverview(downloads: List<DownloadItem>) {
    val activeDownloads = downloads.count {download ->
        !download.isPaused && download.downloadedBytes < download.totalBytes
    }

    val totalSpeed = downloads.sumOf { download ->
        if (!download.isPaused && download.downloadedBytes < download.totalBytes) {
            download.speedBytesPerSecond
        } else {
            0L
        }
    }

    val speedInMegabytes = totalSpeed / (1024.0 * 1024.0)

    Column(
        modifier = Modifier.fillMaxWidth().background(
            color = CardBackground,
            shape = RoundedCornerShape(30.dp)
        ).padding(24.dp)
    ) {
        Text(
            text = if (activeDownloads > 0) {
                "Downloading..."
            } else {
                "All caught up"
            },
            color = PrimaryText,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "%.1f".format(speedInMegabytes),
                color = PrimaryText,
                fontSize = 54.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = " MB/s",
                color = Accent,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Text(
            text = "$activeDownloads active downloads",
            color = SecondaryText,
            fontSize = 15.sp
        )
    }
}
