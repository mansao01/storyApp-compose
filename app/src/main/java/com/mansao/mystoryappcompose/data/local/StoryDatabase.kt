package com.mansao.mystoryappcompose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mansao.mystoryappcompose.data.local.dao.RemoteKeysDao
import com.mansao.mystoryappcompose.data.local.dao.StoryDao
import com.mansao.mystoryappcompose.data.local.model.StoryRemoteKeys
import com.mansao.mystoryappcompose.data.network.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class, StoryRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, StoryDatabase::class.java, "story_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}