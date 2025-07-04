package com.locationReminder.roomDatabase.repository


import androidx.lifecycle.LiveData
import com.locationReminder.model.apiUtil.serviceModel.DefaultDispatcher
import com.locationReminder.model.apiUtil.utils.BaseApiResponse
import com.locationReminder.reponseModel.LocationDetail
import com.locationReminder.roomDatabase.dao.UserDao
import com.locationReminder.viewModel.UserDetailResponseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDatabaseRepository @Inject constructor(
    private val userDao: UserDao,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : BaseApiResponse() {

    suspend fun insertUser(user: UserDetailResponseModel?) {
        return withContext(defaultDispatcher) {
            if (user != null) {
                userDao.insertUser(user)
            }
        }
    }

    fun getUserDetail(): UserDetailResponseModel {
        return userDao.findUser()


    }


    suspend fun clearUserDB() {
        return withContext(defaultDispatcher) {
            userDao.clearUserDB()
        }
    }


}