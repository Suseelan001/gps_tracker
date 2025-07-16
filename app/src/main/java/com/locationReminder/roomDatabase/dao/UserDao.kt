package com.locationReminder.roomDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.locationReminder.viewModel.UserDetailResponseModel


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(product: UserDetailResponseModel)

    @Query("SELECT * FROM user")
    fun findUser(): UserDetailResponseModel

    @Query("DELETE FROM user")
    fun clearUserDB()

}