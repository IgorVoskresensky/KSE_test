package ru.ivos.kse_test.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ivos.kse_test.domain.model.Car

@Dao
interface AppDao {

    @Query("SELECT * FROM cars")
    suspend fun getCars(): List<Car>

    @Query("SELECT * FROM cars WHERE model = :model")
    suspend fun getCar(model: String): Car

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car)

    @Query("DELETE FROM cars WHERE model = :model")
    suspend fun deleteCar(model: String)
}