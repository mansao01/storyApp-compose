package com.example.mystoryappcompose.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.mystoryappcompose.data.local.StoryDatabase
import com.example.mystoryappcompose.data.local.model.StoryRemoteKeys
import com.example.mystoryappcompose.data.network.ApiService
import com.example.mystoryappcompose.data.network.response.ListStoryItem

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) : RemoteMediator<Int, ListStoryItem>() {

    private val storyDao = storyDatabase.storyDao()
    private val remoteKeysDao = storyDatabase.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
//        return try {
//            val currentPage = when (loadType) {
//                LoadType.REFRESH -> {
//
//                }
//
//                LoadType.PREPEND -> {
//
//                }
//
//                LoadType.APPEND -> {
//
//                }
//
//            }
//            MediatorResult.Success()
//        } catch (e: Exception) {
//            return MediatorResult.Error(e)
//        }
        TODO()
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ListStoryItem>
    ): StoryRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeysId(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstTime(
        state: PagingState<Int, ListStoryItem>
    ): StoryRemoteKeys? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { story ->
                remoteKeysDao.getRemoteKeysId(id = story.id)
            }
    }

    private suspend fun getRemoteKeyForLastTime(
        state: PagingState<Int, ListStoryItem>
    ): StoryRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { story ->
                remoteKeysDao.getRemoteKeysId(id = story.id)
            }
    }
}