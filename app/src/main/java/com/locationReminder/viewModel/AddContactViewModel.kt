package com.locationReminder.viewModel

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.locationReminder.reponseModel.ContactDetail
import com.locationReminder.reponseModel.ContactDetailLocal
import com.locationReminder.roomDatabase.repository.AddContactDatabaseRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val addContactDatabaseRepository: AddContactDatabaseRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _deviceContacts = MutableStateFlow<List<ContactDetailLocal>>(emptyList())
    val deviceContacts: StateFlow<List<ContactDetailLocal>> = _deviceContacts

    fun insertRecord(locationDetail: ContactDetail) {
        viewModelScope.launch {
            addContactDatabaseRepository.insertRecord(locationDetail)
        }
    }

    fun deleteItem(item: ContactDetail) {
        viewModelScope.launch {
            addContactDatabaseRepository.deleteLocation(item)
        }
    }

    fun clearUserDB() {
        viewModelScope.launch {
            addContactDatabaseRepository.clearUserDB()
        }
    }

    fun getAllRecord(): LiveData<List<ContactDetail>> {
        return addContactDatabaseRepository.getAllRecord()
    }

    fun loadDeviceContacts() {
        val context = getApplication<Application>().applicationContext
        val contentResolver = context.contentResolver
        val contactSet = mutableSetOf<String>() // To track unique numbers
        val uniqueContacts = mutableListOf<ContactDetailLocal>()

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: ""
                val number = it.getString(numberIndex)?.replace(" ", "") ?: ""
                val normalizedNumber = number.filter { it.isDigit() } // Optional: filter only digits

                if (normalizedNumber !in contactSet) {
                    contactSet.add(normalizedNumber)
                    uniqueContacts.add(ContactDetailLocal(name, normalizedNumber))
                }
            }
        }

        _deviceContacts.value = uniqueContacts
    }

}
