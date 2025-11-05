package com.group7.medicationadherenceapp

import android.provider.ContactsContract
import androidx.compose.ui.semantics.Role
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "username") val username: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "phone_number") val phoneNumber: String?,
)
