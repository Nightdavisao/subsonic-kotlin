package dev.zt64.subsonic.api.model

import kotlinx.serialization.Serializable


/**
 * Codec profile
 * @property limitations List of limitations
 * @property name Profile name
 * @property type Codec type ("AudioCodec"?)
 */
@Serializable
public data class CodecProfile(
    val limitations: List<Limitation>,
    val name: String,
    val type: String
)

/**
 * Direct play profile
 * @property audioCodecs List of supported audio codecs
 * @property containers List of supported containers
 * @property maxAudioChannels Maximum amount of audio channels
 * @property protocols Supported transport protocols (HTTP, HLS...)
 */
@Serializable
public data class DirectPlayProfile(
    val audioCodecs: List<String>,
    val containers: List<String>,
    val maxAudioChannels: Int,
    val protocols: List<String>
)

/**
 * Transcoding profile
 * @property audioCodec Audio codec
 * @property container Container for the codec
 * @property maxAudioChannels Amount of maximum audio channels
 * @property protocol Supported transport protocol (HTTP, HLS...)
 */
@Serializable
public data class TranscodingProfile(
    val audioCodec: String,
    val container: String,
    val maxAudioChannels: Int,
    val protocol: String
)

/**
 * Limitation
 * @property comparison Comparison
 * @property name Limitation name
 * @property required Whether is it required
 * @property values Values
 */
@Serializable
public data class Limitation(
    val comparison: String,
    val name: String,
    val required: Boolean,
    val values: List<String>
)

/**
 * Client information, generally used for providing information about supported codecs and containers for the server.
 * `getTranscodeDecision` requires this information for yielding direct play and transcode profiles to the client.
 * @property codecProfiles Codec profiles
 * @property directPlayProfiles Direct play profiles
 * @property maxAudioBitrate Maximum audio bitrate
 * @property name Client name
 * @property platform Platform name
 * @property transcodingProfiles Transcoding profiles
 */
@Serializable
public data class ClientInfo(
    val codecProfiles: List<CodecProfile>,
    val directPlayProfiles: List<DirectPlayProfile>,
    val maxAudioBitrate: Int,
    val maxTranscodingAudioBitrate: Int,
    val name: String,
    val platform: String,
    val transcodingProfiles: List<TranscodingProfile>
)