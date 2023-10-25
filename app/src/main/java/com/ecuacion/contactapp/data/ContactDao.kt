package com.ecuacion.contactapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ecuacion.contactapp.data.entities.Contact


@Dao
interface ContactDao {


    @Upsert
    suspend fun upsertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)


    @Query("SELECT * FROM contact ORDER BY firstName ASC")
    fun getContactsOrderedbyFirstName(): kotlinx.coroutines.flow.Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY lastName ASC")
    fun getOrderedbyLastName(): kotlinx.coroutines.flow.Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY phoneNumber ASC")
    fun getContactsOrderedbyPhoneNumber(): kotlinx.coroutines.flow.Flow<List<Contact>>


}