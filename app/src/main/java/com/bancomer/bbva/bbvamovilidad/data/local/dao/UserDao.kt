package com.bancomer.bbva.bbvamovilidad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM USER_TABLE")
    suspend fun getUSerInfo(): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)
}