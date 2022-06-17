package ar.teamrocket.duelosmeli

import android.app.Application
import android.content.Context
import androidx.room.Room
import ar.teamrocket.duelosmeli.data.database.DuelosMeliDb
import ar.teamrocket.duelosmeli.data.database.PlayerDao
import ar.teamrocket.duelosmeli.data.preferences.Prefs
import ar.teamrocket.duelosmeli.data.database.UserPreferences
import ar.teamrocket.duelosmeli.data.repository.MeliRepository
import ar.teamrocket.duelosmeli.data.repository.PlayersRepository
import ar.teamrocket.duelosmeli.data.repository.impl.MeliRepositoryImpl
import ar.teamrocket.duelosmeli.data.repository.impl.PlayersRepositoryImpl
import ar.teamrocket.duelosmeli.domain.GameFunctions
import ar.teamrocket.duelosmeli.domain.impl.GameFunctionsImpl
import ar.teamrocket.duelosmeli.ui.multiplayerActivities.viewModels.MultiplayerGamePartialResultActivityViewModel
import ar.teamrocket.duelosmeli.ui.multiplayerActivities.viewModels.MultiplayerGameReadyViewModel
import ar.teamrocket.duelosmeli.ui.multiplayerActivities.viewModels.MultiplayerGameViewModel
import ar.teamrocket.duelosmeli.ui.multiplayerActivities.viewModels.NewMultiplayerGameViewModel
import ar.teamrocket.duelosmeli.ui.singleplayerActivities.viewModels.GameViewModel
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication : Application() {
    private val appModule = module {
        single<PlayerDao> { getDatabase(get()).playerDao() }

        viewModel { GameViewModel(get()) }
        viewModel { MultiplayerGamePartialResultActivityViewModel(get()) }
        viewModel { MultiplayerGameReadyViewModel(get()) }
        viewModel { NewMultiplayerGameViewModel(get()) }
        viewModel { MultiplayerGameViewModel(get(), get()) }

        single<MeliRepository> { MeliRepositoryImpl() }
        single<GameFunctions> { GameFunctionsImpl() }
        single<PlayersRepository> { PlayersRepositoryImpl(get()) }
    }


    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }

    private fun getDatabase(context: Context) = Room.databaseBuilder(
        context,
        DuelosMeliDb::class.java,
        "duelosmeli-db"
    )
        .allowMainThreadQueries().build()
}

