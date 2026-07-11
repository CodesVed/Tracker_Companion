package com.example.trackercompanion.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
    CalendarWeek::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).build()
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