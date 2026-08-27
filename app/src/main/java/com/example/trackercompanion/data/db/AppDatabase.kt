package com.example.trackercompanion.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trackercompanion.data.db.dao.CalendarWeekDao
import com.example.trackercompanion.data.db.dao.ChampionshipDao
import com.example.trackercompanion.data.db.dao.ContendershipDao
import com.example.trackercompanion.data.db.dao.MatchDao
import com.example.trackercompanion.data.db.dao.PPVEventDao
import com.example.trackercompanion.data.db.dao.ShowEpisodeDao
import com.example.trackercompanion.data.db.dao.TitleReignDao
import com.example.trackercompanion.data.db.dao.WrestlerDao
import com.example.trackercompanion.model.CalendarWeek
import com.example.trackercompanion.model.Championship
import com.example.trackercompanion.model.Contendership
import com.example.trackercompanion.model.Match
import com.example.trackercompanion.model.PPVEvent
import com.example.trackercompanion.model.ShowEpisode
import com.example.trackercompanion.model.TitleReign
import com.example.trackercompanion.model.Wrestler

@Database(entities = [
    Wrestler::class,
    Championship::class,
    Contendership::class,
    TitleReign::class,
    Match::class,
    ShowEpisode::class,
    PPVEvent::class,
    CalendarWeek::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
            CREATE TABLE Wrestler_new (
                id INTEGER PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                brand TEXT NOT NULL,
                status TEXT NOT NULL,
                type TEXT NOT NULL,
                imageUrl TEXT,
                notes TEXT NOT NULL
            )
        """.trimIndent())

                db.execSQL("""
            CREATE TABLE Championship_new (
                id INTEGER PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                titleImageUrl TEXT,
                brand TEXT
            )
        """.trimIndent())

                db.execSQL("""
            INSERT INTO Wrestler_new (id, name, brand, status, type, imageUrl, notes)
            SELECT id, name, brand, status, type,
                   'file:///android_asset/images/wrestler_placeholder.webp',
                   notes
            FROM Wrestler
        """.trimIndent())

                db.execSQL("""
            INSERT INTO Championship_new (id, title, titleImageUrl, brand)
            SELECT id, title, NULL, brand
            FROM Championship
        """.trimIndent())

                db.execSQL("DROP TABLE Wrestler")
                db.execSQL("ALTER TABLE Wrestler_new RENAME TO Wrestler")

                db.execSQL("DROP TABLE Championship")
                db.execSQL("ALTER TABLE Championship_new RENAME TO Championship")
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIGRATION_1_2).build()
                    .also { INSTANCE = it }
            }

        }
    }

    abstract fun getWrestlerDao(): WrestlerDao
    abstract fun getChampionshipDao(): ChampionshipDao
    abstract fun getTitleReignDao(): TitleReignDao
    abstract fun getContendershipDao(): ContendershipDao
    abstract fun getShowEpisodeDao(): ShowEpisodeDao
    abstract fun getPPVEventDao(): PPVEventDao
    abstract fun getMatchDao(): MatchDao
    abstract fun getCalendarWeekDao(): CalendarWeekDao
}