package com.bancomer.bbva.bbvamovilidad.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bancomer.bbva.bbvamovilidad.utils.Dictionary.USER_TABLE

@Entity(tableName = USER_TABLE)
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 1,
    @ColumnInfo(name = "userMail")
    var userMail: String? = null,
    @ColumnInfo(name = "userm")
    var userm: String? = null,
    @ColumnInfo(name = "nombres")
    var nombres: String? = null,
    @ColumnInfo(name = "codCentroTrabajo")
    var codCentroTrabajo: Int? = null,
    @ColumnInfo(name = "fhAceptaTerminos")
    var fhAceptaTerminos: String? = null
)
