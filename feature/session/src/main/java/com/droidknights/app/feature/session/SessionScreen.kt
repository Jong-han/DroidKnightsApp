package com.droidknights.app.feature.session

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidknights.app.core.designsystem.theme.KnightsTheme
import com.droidknights.app.core.model.Room
import com.droidknights.app.core.model.Session
import com.droidknights.app.feature.session.component.SessionItem
import com.droidknights.app.feature.session.component.SessionTopAppBar
import com.droidknights.app.feature.session.model.SessionState
import com.droidknights.app.feature.session.model.SessionUiState
import com.droidknights.app.feature.session.model.rememberSessionState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun SessionScreen(
    onBackClick: () -> Unit,
    onSessionClick: (Session) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    sessionViewModel: SessionViewModel = hiltViewModel(),
) {
    val sessionUiState by sessionViewModel.uiState.collectAsStateWithLifecycle()
    val sessionState = (sessionUiState as? SessionUiState.Sessions)?.sessions
        ?.let { sessions ->
            rememberSessionState(sessions = sessions) // SessionUiState.Sessions
        }
        ?: rememberSessionState(sessions = persistentListOf()) // SessionUiState.Loading, SessionUiState.Error

    LaunchedEffect(Unit) {
        sessionViewModel.errorFlow.collectLatest { throwable -> onShowErrorSnackBar(throwable) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SessionTopAppBar(
            sessionState = sessionState,
            onBackClick = onBackClick,
        )
        SessionList(
            sessionState = sessionState,
            modifier = Modifier
                .systemBarsPadding()
                .padding(top = 48.dp)
                .fillMaxSize(),
            onSessionClick = onSessionClick,
        )
    }
}

@Composable
private fun SessionList(
    sessionState: SessionState,
    onSessionClick: (Session) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        state = sessionState.listState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        sessionState.groups.forEachIndexed { index, group ->
            val topPadding = if (index == 0) {
                SessionTopSpace
            } else {
                SessionGroupSpace
            }
            sessionItems(
                room = group.room,
                items = group.sessions,
                topPadding = topPadding,
                isLastGroup = index == sessionState.groups.size - 1,
                onItemClick = onSessionClick,
            )
        }
    }
}

private val SessionTopSpace = 16.dp
private val SessionGroupSpace = 100.dp

private fun LazyListScope.sessionItems(
    room: Room,
    items: PersistentList<Session>,
    topPadding: Dp,
    isLastGroup: Boolean,
    onItemClick: (Session) -> Unit,
) {
    itemsIndexed(items) { index, item ->
        SessionItem(
            index = index,
            item = item,
            room = room,
            topPadding = topPadding,
            onItemClick = onItemClick
        )
        if (isLastGroup && index == items.size - 1) {
            DroidKnightsFooter()
        }
    }
}

@Composable
private fun DroidKnightsFooter() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 56.dp, bottom = 80.dp),
        text = stringResource(id = R.string.footer_text),
        style = KnightsTheme.typography.labelMediumR,
        color = Color.LightGray,
        textAlign = TextAlign.Center
    )
}
