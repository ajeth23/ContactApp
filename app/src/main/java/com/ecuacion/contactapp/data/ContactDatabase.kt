package com.ecuacion.contactapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ecuacion.contactapp.data.entities.Contact


@Database(
    entities = [Contact::class],
    version = 1
)
abstract class ContactDatabase : RoomDatabase() {

    abstract val dao: ContactDao


}