package com.example.newsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.data.model.Article
import com.example.newsapp.util.Constants.DATABASE_NAME

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(ArticlesTypeConverters::class)
abstract class ArticlesDatabase : RoomDatabase() {

    abstract val articlesDao: ArticlesDao

    companion object {
        @Volatile
        private var articlesDatabase: ArticlesDatabase? = null

        @Synchronized
        fun getArticlesDatabaseInstance(context: Context): ArticlesDatabase {
            synchronized(this) {
                if (articlesDatabase == null) {
                    articlesDatabase = Room.databaseBuilder(
                        context,
                        ArticlesDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                        .build()
                }

                return articlesDatabase as ArticlesDatabase
            }
        }
    }
}