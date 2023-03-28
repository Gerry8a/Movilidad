package com.bancomer.bbva.bbvamovilidad.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bancomer.bbva.bbvamovilidad.data.local.dao.UserDao
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.DATABASE_VERSION
import javax.inject.Inject

@Database(
    entities = [UserEntity::class],
    version = DATABASE_VERSION
)
abstract class UserDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao
}