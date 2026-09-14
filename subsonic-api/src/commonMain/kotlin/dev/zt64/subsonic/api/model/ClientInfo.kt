package dev.zt64.subsonic.api.model


public data class CodecProfile internal constructor(
    val limitations: List<Limitation>,
    val name: String,
    val type: String
)

public data class DirectPlayProfile internal constructor(
    val audioCodecs: List<String>,
    val containers: List<String>,
    val maxAudioChannels: Int,
    val protocols: List<String>
)

public data class TranscodingProfile internal constructor(
    val audioCodec: String,
    val container: String,
    val maxAudioChannels: Int,
    val protocol: String
)

public data class Limitation internal constructor(
    val comparison: String,
    val name: String,
    val required: Boolean,
    val values: List<String>
)

public data class ClientInfo internal constructor(
    val codecProfiles: List<CodecProfile>,
    val directPlayProfiles: List<DirectPlayProfile>,
    val maxAudioBitrate: Int,
    val maxTranscodingAudioBitrate: Int,
    val name: String,
    val platform: String,
    val transcodingProfiles: List<TranscodingProfile>
)