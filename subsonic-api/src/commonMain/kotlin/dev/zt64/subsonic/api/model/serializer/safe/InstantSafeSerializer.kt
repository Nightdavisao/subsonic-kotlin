/*
 * Navic, a Subsonic music streaming app for Android
 * Copyright (c) 2026 paige
 * SPDX-License-Identifier: GPL-3.0-only
 */

package dev.zt64.subsonic.api.model.serializer.safe

import kotlinx.serialization.builtins.serializer
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
public object InstantSafeSerializer : SafeSerializer<Instant>(
    delegate = Instant.serializer(),
    fallback = Instant.DISTANT_PAST
)
