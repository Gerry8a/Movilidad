package com.bancomer.bbva.bbvamovilidad.data.local.dao

import androidx.room.Entity
import androidx.room.Insert

interface BaseDao<T> {

    @Insert
    suspend fun insert(entity: T)


}