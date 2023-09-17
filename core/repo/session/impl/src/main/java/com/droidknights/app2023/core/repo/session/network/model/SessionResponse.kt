package com.droidknights.app2023.core.repo.session.network.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
internal data class SessionResponse(
    val id: String,
    val title: String,
    val content: String,
    val speakers: List<SpeakerResponse>,
    val level: LevelResponse,
    val tags: List<String>,
    val room: RoomResponse?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
)
