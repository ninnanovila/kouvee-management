package com.example.kouveemanagement.persistent

import androidx.room.*
import retrofit2.http.DELETE

@Entity(tableName = "current_user")
data class CurrentUser(
    @ColumnInfo(name="user_id") var user_id: String,
    @ColumnInfo(name="user_name") var user_name: String,
    @ColumnInfo(name="user_role") var user_role: String,
    @ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) var id: Long = 0
)

@Dao
interface CurrentUserDao {

    @Query("SELECT * FROM current_user")
    fun getCurrentuser(): CurrentUser

    @Insert
    fun insertCurrentUser(vararg currentUsers: CurrentUser)

    @Delete
    fun deleteCurrentUser(vararg currentUser: CurrentUser)
}

@Database(entities = [(CurrentUser::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currentUserDao(): CurrentUserDao
}