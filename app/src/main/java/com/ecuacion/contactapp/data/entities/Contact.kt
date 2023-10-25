package com.ecuacion.contactapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.PropertyKey


@Entity
data class Contact(

    val firstName: String,
    val lastName: String,
    val phoneNumber: String,

    @PrimaryKey(autoGenerate = false)
    val id: Int = 0

)