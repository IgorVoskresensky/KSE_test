package ru.ivos.kse_test.domain.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "cars")
@Parcelize
data class Car(

    @PrimaryKey()
    @ColumnInfo(name = "model")
    var model: String,
    @ColumnInfo(name = "manufacturer")
    var manufacturer: String,
    @ColumnInfo(name = "price")
    var price: Double,
    @ColumnInfo(name = "transmission")
    var transmission: String,
    @ColumnInfo(name = "year")
    var year: Int,
    @ColumnInfo(name = "image")
    var image: Bitmap
) : Parcelable
