package com.droidknights.app2023.core.data.repository

import com.droidknights.app2023.core.data.api.fake.FakeGithubRawApi
import com.droidknights.app2023.core.model.Level
import com.droidknights.app2023.core.model.Room
import com.droidknights.app2023.core.model.Session
import com.droidknights.app2023.core.model.Speaker
import com.droidknights.app2023.core.model.Tag
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime

internal class DefaultSessionRepositoryTest : StringSpec() {

    init {
        val repository: SessionRepository = DefaultSessionRepository(
            githubRawApi = FakeGithubRawApi()
        )
        "역직렬화 테스트" {
            val expected = listOf(
                Session(
                    title = "Keynote",
                    content = listOf(),
                    speakers = listOf(),
                    level = Level("기타"),
                    tags = listOf(Tag("키노트")),
                    room = Room.TRACK1,
                    startTime = LocalDateTime(2023, 9, 12, 13, 0),
                    endTime = LocalDateTime(2023, 9, 12, 13, 20),
                ),
                Session(
                    title = "Jetpack Compose로 Android UI 개발하기",
                    content = listOf("Jetpack Compose는 네이티브 UI를 빌드하기 위한 Android의 최신 권장 도구 키트입니다."),
                    speakers = listOf(
                        Speaker(
                            name = "김컴포즈",
                            imageUrl = "https://developer.android.com/static/courses/android-basics-compose/images/hero-assets/unit-logo.svg",
                        )
                    ),
                    level = Level("기타"),
                    tags = listOf(
                        Tag("Jetpack"),
                        Tag("UI"),
                    ),
                    room = Room.TRACK2,
                    startTime = LocalDateTime(2023, 9, 12, 13, 25),
                    endTime = LocalDateTime(2023, 9, 12, 13, 55),
                ),
            )
            val actual = repository.getSessions()
            actual shouldBe expected
        }
    }
}
