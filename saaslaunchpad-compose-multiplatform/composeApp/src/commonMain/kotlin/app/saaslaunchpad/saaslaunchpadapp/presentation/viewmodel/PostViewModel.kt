package app.saaslaunchpad.saaslaunchpadapp.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import app.saaslaunchpad.saaslaunchpadapp.data.ParadigmaticDatabase
import app.saaslaunchpad.saaslaunchpadapp.domain.model.Post
import app.saaslaunchpad.saaslaunchpadapp.domain.model.PostApiRequestState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch

typealias CachedPosts = MutableState<PostApiRequestState<List<Post>>>

class PostViewModel (
    private val database: ParadigmaticDatabase
): ScreenModel {

    /* TODO-FIXME-CLEANUP
    var allPosts: CachedPosts = mutableStateOf(PostApiRequestState.Idle)
        private set
    */
    private val _allPosts: MutableState<PostApiRequestState<List<Post>>> = mutableStateOf(PostApiRequestState.Idle)
    val allPosts: State<PostApiRequestState<List<Post>>> = _allPosts

    init {
        screenModelScope.launch {
           _allPosts.value = database.getAllPosts()
        }
    }
}