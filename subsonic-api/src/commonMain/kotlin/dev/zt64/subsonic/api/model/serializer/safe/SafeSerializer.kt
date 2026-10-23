/*
 * Navic, a Subsonic music streaming app for Android
 * Copyright (c) 2026 paige
 * SPDX-License-Identifier: GPL-3.0-only
 */

package dev.zt64.subsonic.api.model.serializer.safe

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public open class SafeSerializer<T>(
    private val delegate: KSerializer<T>,
    private val fallback: T
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun deserialize(decoder: Decoder): T {
        return try {
            delegate.deserialize(decoder)
        } catch (_: Exception) {
            fallback
        }
    }

    override fun serialize(encoder: Encoder, value: T) {
        delegate.serialize(encoder, value)
    }
}
