package dev.zt64.subsonic.api.model

import kotlinx.serialization.Serializable

@Serializable
public data class TranscodeDecision internal constructor(
    val canDirectPlay: Boolean,
    val canTranscode: Boolean,
    val errorReason: String? = null,
    val transcodeParams: String? = null,
    val sourceStream: StreamDetails? = null,
    val transcodeStream: StreamDetails? = null
)

@Serializable
public enum class MediaType {
    SONG,
    PODCAST
}