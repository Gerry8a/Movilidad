package com.bancomer.bbva.bbvamovilidad.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bancomer.bbva.bbvamovilidad.data.local.dao.UserDao
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity
import javax.inject.Inject

@Database(
    entities = [UserEntity::class],
    version = 1
)
abstract class UserDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao
}