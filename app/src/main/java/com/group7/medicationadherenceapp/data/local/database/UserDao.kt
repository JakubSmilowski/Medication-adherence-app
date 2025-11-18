package com.group7.medicationadherenceapp.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.group7.medicationadherenceapp.data.local.database.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    suspend fun findByName(first: String, last: String): User?
    /**
    Inserts multiple users into the database.
     */

    //@Insert(onConflict = OnConflictStrategy.IGNORE)
    //suspend fun insertAll(vararg user: User)

    /**
    Inserts a single user into the database.
    If a user with the same primary key already exists in the database, it will be ignored.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    /** authenticates a user on username and password.
    used for the login process.
     */
    @Query("SELECT * FROM user WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): User?

    /**  finds a user by their email address.
    returns a user object if found, otherwise null.
    used for the register process   */
    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM user WHERE uid = :userId")
    suspend fun getUserById(userId: Int): User?
}
