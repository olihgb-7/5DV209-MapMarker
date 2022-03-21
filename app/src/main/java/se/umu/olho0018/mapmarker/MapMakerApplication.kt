package se.umu.olho0018.mapmarker

import android.app.Application
import se.umu.olho0018.mapmarker.database.PostRepository

class MapMakerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // Setup of database repository
        PostRepository.initialize(this)
    }
}