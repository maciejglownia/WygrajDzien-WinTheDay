package pl.glownia.maciej.wd.database;

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Dao MUST BE an interface and annotated by @Dao
 *
 * DAO -> Data Access Object and is an interface that contains the necessary query to be
performed for any of the operations required by the app

There are available convenient methods like @Insert @Fetch @Update @Delete actually,
and there is also @Query that allows you to write custom simple statements and
also note that you must annotate the interface with @Dao
 */
@Dao
interface TaskDao {
    @Insert
    suspend fun insert(taskEntity: TaskEntity)

    @Update
    suspend fun update(taskEntity: TaskEntity)

    @Delete
    suspend fun delete(taskEntity: TaskEntity)

    /**
     *  Flow is a part of the coroutine class used to hold values that can always change at runtime
    That's because it automatically omits value more like a life update.
    With Flow, all you need is to collect the value from the variable or method without needing
    to always repeat codes to update the user interface
    The collect method keeps omitting data as it changes

    basic methods:
     * collectLatest -> it returns the last value from an update and forgets the previous ones
     * collectedIndexed -> you can get an index of an element with its value
     * combined -> you can transform flows and return its values automatically when they change
     */

    /**
     * Just like the suspense keyword, it runs the operation on a different thread, but with
     * an additional feature of meeting updates automatically when they occur
     */

    /**
     * IMPORTANT -> not use suspend keyword here because Flow is taking care of coroutine stuff
     */
    @Query("SELECT * FROM `tasks-table`")
    // Make sure Flow is an interface kotlinx.coroutines.flow
    fun fetchAllTasks(): Flow<List<TaskEntity>>

    // Need to pass the ID that I want to specifically fetch and I'm going to select start from
    // And here I need to define where the ID is equal to our ID
    // ! This will allow us to get an individual task by the uniquely identifiable ID
    @Query("SELECT * FROM `tasks-table` WHERE id =:id")
    fun fetchAllTasksById(id: Int): Flow<TaskEntity>
}
