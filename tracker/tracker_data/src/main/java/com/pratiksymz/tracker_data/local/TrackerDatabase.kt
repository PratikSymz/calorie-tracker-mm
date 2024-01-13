package com.pratiksymz.tracker_data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pratiksymz.tracker_data.local.entity.TrackedFoodEntity

/**
 * Abstract class for Room to generate the necessary files (classes)
 */
@Database(
    entities = [TrackedFoodEntity::class],
    version = 1
)
abstract class TrackerDatabase: RoomDatabase() {

    abstract val dao: TrackerDao
}