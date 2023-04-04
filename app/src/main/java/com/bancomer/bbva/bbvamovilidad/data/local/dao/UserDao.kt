package com.bancomer.bbva.bbvamovilidad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bancomer.bbva.bbvamovilidad.data.api.response.UserInfo
import com.bancomer.bbva.bbvamovilidad.data.local.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM USER_TABLE")
    suspend fun getUSerInfo(): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("UPDATE USER_TABLE SET codCentroTrabajo = :cod, centroTrabajoAct = :centroTrabajoAct")
    suspend fun updateWorkCenter(cod: Int, centroTrabajoAct: String)

}