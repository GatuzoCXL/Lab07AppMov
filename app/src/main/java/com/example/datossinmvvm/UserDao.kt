package com.example.datossinmvvm

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>

    @Insert
    suspend fun insert(user: User)

    //agregando la funcion eliminar
    @Delete
    suspend fun delete(user: User)

    //getLastUser
    @Query("SELECT * FROM User ORDER BY uid DESC LIMIT 1")
    suspend fun getLastUser(): User?
}

