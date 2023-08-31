package com.droidknights.app2023.core.data.repository

import com.droidknights.app2023.core.data.api.GithubRawApi
import com.droidknights.app2023.core.data.mapper.toData
import com.droidknights.app2023.core.datastore.PlaybackPreferencesDataSource
import com.droidknights.app2023.core.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class DefaultSessionRepository @Inject constructor(
    private val githubRawApi: GithubRawApi,
    private val preferencesDataSource: PlaybackPreferencesDataSource
) : SessionRepository {
    private var cachedSessions: List<Session> = emptyList()

    /**
     * TODO : 북마크 아이디가 앱이 종료된 이후에도 유지되도록 한다
     */
    private val bookmarkIds: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())

    override suspend fun getSessions(): List<Session> {
        return githubRawApi.getSessions()
            .map { it.toData() }
            .also { cachedSessions = it }
    }

    override suspend fun getSession(sessionId: String): Session {
        val cachedSession = cachedSessions.find { it.id == sessionId }
        if (cachedSession != null) {
            return cachedSession
        }

        return getSessions().find { it.id == sessionId }
            ?: error("Session not found with id: $sessionId")
    }

    override suspend fun getBookmarkedSessionIds(): Flow<Set<String>> {
        return bookmarkIds.filterNotNull()
    }

    override suspend fun bookmarkSession(sessionId: String, bookmark: Boolean) {
        bookmarkIds.update { ids ->
            if (bookmark) {
                ids + sessionId
            } else {
                ids - sessionId
            }
        }
    }

    override fun getCurrentPlayingSessionId(): Flow<String?> =
        preferencesDataSource.playbackData.map { it?.currentSessionId }

    override suspend fun updateCurrentPlayingSessionId(sessionId: String) {
        preferencesDataSource.updateCurrentSessionId(sessionId)
    }
}
