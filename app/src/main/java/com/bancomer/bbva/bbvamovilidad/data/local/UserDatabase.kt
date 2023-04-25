package com.bancomer.bbva.bbvamovilidad.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bancomer.bbva.bbvamovilidad.data.local.dao.MedioDao
import com.bancomer.bbva.bbvamovilidad.data.local.dao.UserDao
import com.bancomer.bbva.bbvamovilidad.data.local.entities.MedioEntity
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.DATABASE_VERSION

@Database(
    entities = [
        UserEntity::class,
        MedioEntity::class
    ],
    version = DATABASE_VERSION
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getMediosDao(): MedioDao
}