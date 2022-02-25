package pl.glownia.maciej.wygrajdzien.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Here is an abstract class which inherit from RoomDatabase (from androidx.room)
 *
 * Remember that annotation is important -> @Database
 *
 * Version -> if for example we change the amount of properties that we have in the table,
 * then we have to change the version and then we would have to go into data migration, which
 * would make it super difficult in some cases, because you need the new property in your project
 * for it to work, but you don't have it for most uses, then you would have to alter, generate
 * it and so forth
 */
@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    // Now we need to connect the database to our DAO
    // Create an abstract function called task DAO
    // And this will return our task DAO -> this interface created before (step 2/4)
    abstract fun taskDao(): TaskDao

    // Next we need to define a companion object, which allows us to add functions on the task
    // database class,
    // For example, classes can call task database, get instance context to instantiate
    // a new task database
    /**
     * Explanation after create TaskApp (after those: Entity, Dao, Database)
     *
     * We have this companion object where we had this instance get instance method, which is
     * a singleton that only exists once in the entire application.
     * So there's only one instance of this instance object at once.
     *
     * So now we can go ahead (check TaskApp) and get the instance by passing
     * >this< as the application. Because getInstance (method below) need a context and we are passing
     * the application context as the context and we need to set up the application accordingly
     * inside of our application tag of our AndroidManifest
     */
    companion object {
        // So this will keep a reference to any database returned via get instance
        // This will help us to avoid repeatedly initializing the database, which is expensive
        // in terms of performance
        // The value of a volatile variable will never be cached, and all writes and reads
        // will be done to and from the main memory
        // It means the changes made by one thread to share data are visible to other threads also,
        // and we need to add the add volatile keyword here
        // So this will be a volatile variable.
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        // Then we need to have a helper function to get the database
        // So if a database has already been retrieved, the previous database will be returned
        // Otherwise, we're going to create a new database
        // This function is going to be thread safe, and should cache the results for
        // multiple database calls to avoid overhead
        // This is an example of a simple singleton pattern that takes another singleton
        // as an argument in Kotlin
        /**
         * SINGLETON
         * So the singleton pattern is a software design pattern that restricts the instantiation
         * of a class to one single instance
         *
         * This is useful when exactly one object is needed to coordinate actions across the system,
         * so you don't want to have multiple different instances of the same object
         *
         * Therefore, you used a singleton pattern where if the object already exists,
         * delete the object and create a new instance of the object or skip the creation
         * of the object if you have the object already
         */

        // Add get a instance with the context -> pass the context and import with TaskDatabase
        fun getInstance(context: Context): TaskDatabase {
            // Need to synchronized
            // Here, multiple threads can ask for the database at the same time and to ensure we
            // only initialize at once. By using this synchronized function, only one thread may
            // enter a synchronized block at a time
            synchronized(this) {
                // Copy the current value of instance, to a local variable
                // So Kotlin can SmartCast, so SmartCast is only available to local variables
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder( // building the database
                        context.applicationContext, // use context
                        TaskDatabase::class.java, // pass TaskDatabase
                        "places-database" // say which DB we want to use
                    ).fallbackToDestructiveMigration() //  it wipes and rebuilds instead of
                        // migrating, if no migration object exists
                        .build()

                    INSTANCE = instance // assign the instance to our instance object
                }
                return instance
            }
        }
    }
}