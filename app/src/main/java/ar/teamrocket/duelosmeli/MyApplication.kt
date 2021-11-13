package ar.teamrocket.duelosmeli

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import ar.teamrocket.duelosmeli.data.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.data.database.PlayerDao
import ar.teamrocket.duelosmeli.ui.viewmodels.GameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.KoinContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {
    private val appModule = module {
        single<PlayerDao> { getDatabase(get()).playerDao() }
        viewModel { GameViewModel() }

    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }

    private fun getDatabase(context: Context) = Room.databaseBuilder(
        context,
        DuelosMeliDb::class.java,
        "duelosmeli-db")
        .allowMainThreadQueries().build()
}

