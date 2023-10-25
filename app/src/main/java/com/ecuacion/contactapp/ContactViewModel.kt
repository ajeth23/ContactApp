package com.ecuacion.contactapp

import android.provider.ContactsContract.Contacts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecuacion.contactapp.data.ContactDao
import com.ecuacion.contactapp.data.entities.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactViewModel(
    private val dao: ContactDao
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)
    private val _contacts = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.FIRST_NAME -> dao.getContactsOrderedbyFirstName()
                SortType.LATt_NAME -> dao.getOrderedbyLastName()
                SortType.PHONE_NUMBER -> dao.getContactsOrderedbyPhoneNumber()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ContactState())

    val state = combine(_state, _sortType, _contacts) { state, sortType, contacts ->
        state.copy(
            contacts = contacts,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    dao.deleteContact(event.contact)
                }

            }

            ContactEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = false
                    )
                }
            }

            ContactEvent.SaveContact -> {

                val firstName = state.value.firstName
                val lastName = state.value.lastNmae
                val phoneNumber = state.value.phoneNumber

                if (firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()) {
                    return
                }

                val contact = Contact(
                    firstName = firstName,
                    lastName = lastName,
                    phoneNumber = phoneNumber
                )

                viewModelScope.launch {
                    dao.upsertContact(contact)
                }

                _state.update {
                    it.copy(
                        isAddingContact = false,
                        firstName = "",
                        lastNmae = "",
                        phoneNumber = ""
                    )
                }


            }

            is ContactEvent.SetFirstName -> {
                _state.update {
                    it.copy(
                        firstName = event.firstname
                    )
                }
            }

            is ContactEvent.SetLastName -> {
                _state.update {
                    it.copy(
                        lastNmae = event.lastname
                    )
                }
            }

            is ContactEvent.SetPhoneNumber -> {
                _state.update {
                    it.copy(
                        phoneNumber = event.phoneNumber
                    )
                }
            }

            ContactEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = true
                    )
                }
            }

            is ContactEvent.SortContact -> {
                _sortType.value = event.sortType
            }
        }
    }
}