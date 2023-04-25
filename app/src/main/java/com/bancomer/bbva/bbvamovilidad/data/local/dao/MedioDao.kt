package com.bancomer.bbva.bbvamovilidad.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bancomer.bbva.bbvamovilidad.data.UIState
import com.bancomer.bbva.bbvamovilidad.data.local.entities.MedioEntity
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.MEDIO_TABLE

@Dao
interface MedioDao {

    @Query("SELECT * FROM MEDIO_TABLE")
    suspend fun getListMedios(): MutableList<MedioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedio(medioEntity: MedioEntity)

    @Query("DELETE FROM MEDIO_TABLE")
    suspend fun deleteMedio()
}