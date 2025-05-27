package com.locationReminder.roomDatabase.repository


import androidx.lifecycle.LiveData
import com.locationReminder.model.apiUtil.serviceModel.DefaultDispatcher
import com.locationReminder.model.apiUtil.utils.BaseApiResponse
import com.locationReminder.model.localStorage.MySharedPreference
import com.locationReminder.roomDatabase.dao.UserDao
import com.locationReminder.viewModel.UserDetailResponseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDatabaseRepository @Inject constructor(
    private val userDao: UserDao,
    private val mySharedPreference: MySharedPreference,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : BaseApiResponse() {

    suspend fun insertUser(user: UserDetailResponseModel?) {
        return withContext(defaultDispatcher) {
            if (user != null) {
                userDao.insertUser(user)
            }
        }
    }
    suspend fun getUserDetail(): UserDetailResponseModel {
        return withContext(defaultDispatcher) {
            userDao.findUser()
        }
    }

    suspend fun getUserDetailLiveData(): LiveData<UserDetailResponseModel?> {
        return withContext(defaultDispatcher) {
            userDao.getUserDetailLiveData()
        }
    }

    suspend fun clearUserDB() {
        return withContext(defaultDispatcher) {
            userDao.clearUserDB()
        }
    }


}