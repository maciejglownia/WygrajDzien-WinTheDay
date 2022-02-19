package pl.glownia.maciej.wygrajdzien.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Now, make sure that we set up the task ENTITY, which is generally the approach that
 * you need to have the entity, then you can set up the DAO and you can set up the DATABASE
 * and then set up the app (check Room Database Demo project if needed)
 *
 * Next step (after these three above) will be to create App, so in our case it will be TaskApp
 */
@Entity(tableName = "tasks-table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val urlImg: String = ""
) : Serializable // It allows us pass it to our intent

// If you to this Parcelable you need to implement methods and make variables nullable, so
// for example     val title: String = "" ->     val title: String = ""
// putExtra methods accept both of them
// but getSerializableExtra needs to be changed to get Parcelable

/**
 * SERIALIZABLE will bring it into a format which we can pass from one class to another.
 *
 * !!! THERE IS ALSO PARCELABLE -> it can be even 10 time faster
https://medium.com/android-news/parcelable-vs-serializable-6a2556d51538
 *
 * ALSO: https://proandroiddev.com/serializable-or-parcelable-why-and-which-one-17b274f3d3bb
1. Sending object references between Android components is not possible because by changing the
process, the object references won’t be in the new process, so we must make our objects
Parcelable or Serializable till the OS be able to save the values of the objects

2. We have two choices to transfer objects between Android components, that are making objects
Parcelable or Serializable

3. Unlike Serializable, in Parcelable reflection won’t be used so it is faster and has better performance

4. Android Parcelable is not made to save objects into the files, so if you want to save
an object in the file you must use Serializable
 */