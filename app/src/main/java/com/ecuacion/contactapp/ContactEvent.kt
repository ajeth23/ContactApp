package com.ecuacion.contactapp

import com.ecuacion.contactapp.data.entities.Contact

sealed interface ContactEvent {

    object SaveContact : ContactEvent

    data class SetFirstName(val firstname: String) : ContactEvent
    data class SetLastName(val lastname: String) : ContactEvent
    data class SetPhoneNumber(val phoneNumber: String) : ContactEvent

    object ShowDialog : ContactEvent
    object HideDialog : ContactEvent

    data class SortContact(val sortType: SortType) : ContactEvent
    data class DeleteContact(val contact: Contact) : ContactEvent

}