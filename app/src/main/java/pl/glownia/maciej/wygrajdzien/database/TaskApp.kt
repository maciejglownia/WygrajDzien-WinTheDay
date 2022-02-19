package pl.glownia.maciej.wygrajdzien.database

import android.app.Application

/**
 * Here inside of it, we can create a DB
 *
 * For every application class, we must declare the app that
 * we have set up in the AndroidManifest as well
 * in <application add -> android:name=".database.TaskApp"
 *
 */
class TaskApp : Application() {
    // We need to set up an application which we did in TaskApp -> an application class,
    // and then we can set up the database in there to now have our database ready.
    // So here we are creating it lazily, which means that
    // IT LOADS THE NEEDED VALUE TO OUR VARIABLE WHENEVER IT IS NEEDED!
    // So not directly, but only when it's needed. That's the point about this lazy here
    // And the database will then be the instance of our history database that we have because
    // we can only have one instance -> now go to TaskDatabase.kt
    val db by lazy {
        TaskDatabase.getInstance(this)
    }
}