package com.droidknights.app2023.feature.tvsession

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidknights.app2023.core.designsystem.component.NetworkImage
import com.droidknights.app2023.core.designsystem.component.TextChip
import com.droidknights.app2023.core.designsystem.theme.DarkGray
import com.droidknights.app2023.core.designsystem.theme.KnightsTheme
import com.droidknights.app2023.core.designsystem.theme.LightGray
import com.droidknights.app2023.core.model.Level
import com.droidknights.app2023.core.model.Room
import com.droidknights.app2023.core.model.Session
import com.droidknights.app2023.core.model.Speaker
import com.droidknights.app2023.core.model.Tag
import com.droidknights.app2023.core.model.Video
import com.droidknights.app2023.feature.tvsession.component.KnightsCard
import kotlinx.datetime.LocalDateTime

@Composable
internal fun SessionCard(
    session: Session,
    modifier: Modifier = Modifier,
    onSessionClick: (Session) -> Unit = { },
) {
    if (session.video.isReady) {
        KnightsCard(
            modifier = modifier,
            onClick = { onSessionClick(session) }
        ) {
            SessionCardContent(session = session)
        }
    } else {
        KnightsCard(
            modifier = modifier
        ) {
            SessionCardContent(session = session)
        }
    }
}

@Composable
private fun SessionCardContent(
    session: Session,
) {
    Column(
        modifier = Modifier.padding(CardContentPadding)
    ) {
        // 카테고리
        Row(verticalAlignment = Alignment.CenterVertically) {
            CategoryChip()
            session.tags.forEach { tag ->
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tag.name,
                    style = KnightsTheme.typography.labelLargeM,
                    color = DarkGray,
                )
            }
        }

        // 제목
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = session.title,
            style = KnightsTheme.typography.titleLargeB,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(end = 50.dp)
        )

        // 트랙
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            TrackChip(room = session.room)
            Spacer(modifier = Modifier.width(8.dp))
            TimeChip(dateTime = session.startTime)
        }

        // 발표자
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                session.speakers.forEach { speaker ->
                    Text(
                        text = speaker.name,
                        style = KnightsTheme.typography.titleLargeB,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
            Row(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                session.speakers.forEach { speaker ->
                    NetworkImage(
                        imageUrl = speaker.imageUrl,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        placeholder = painterResource(id = com.droidknights.app2023.core.ui.R.drawable.placeholder_speaker),
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChip() {
    TextChip(
        text = stringResource(id = R.string.session_category),
        containerColor = DarkGray,
        labelColor = LightGray,
    )
}

private val CardContentPadding =
    PaddingValues(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 24.dp)

@Preview
@Composable
private fun SessionCardPreview() {
    val fakeSession = Session(
        id = "1",
        title = "Jetpack Compose에 있는 것, 없는 것",
        content = "",
        speakers = listOf(
            Speaker(
                name = "안성용",
                introduction = "안드로이드 개발자",
                imageUrl = "https://picsum.photos/200",
            ),
        ),
        level = Level.BASIC,
        tags = listOf(
            Tag("효율적인 코드베이스")
        ),
        startTime = LocalDateTime(2023, 9, 12, 16, 10, 0),
        endTime = LocalDateTime(2023, 9, 12, 16, 45, 0),
        room = Room.TRACK1,
        video = Video.None,
        isBookmarked = true,
    )

    KnightsTheme {
        SessionCard(fakeSession)
    }
}
