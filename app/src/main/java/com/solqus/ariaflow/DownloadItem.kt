package com.solqus.ariaflow

data class DownloadItem(
    val id: Int,
    val name: String,
    val totalBytes: Long,
    val downloadedBytes: Long,
    val speedBytesPerSecond: Long,
    val isPaused: Boolean
) {
    val progress: Float
        get() = if (totalBytes > 0L) {
            downloadedBytes.toFloat() / totalBytes.toFloat()
        } else {
            0f
        }
}
