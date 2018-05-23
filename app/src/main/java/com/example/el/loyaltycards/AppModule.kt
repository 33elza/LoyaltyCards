package com.example.el.loyaltycards

import android.arch.persistence.room.Room
import android.content.Context
import android.support.annotation.NonNull
import com.example.el.loyaltycards.db.DataBase
import com.example.el.loyaltycards.repository.CardsRepository
import com.example.el.loyaltycards.repository.ICardsRepository
import com.example.el.loyaltycards.utils.CardValidator
import com.example.el.loyaltycards.utils.ICardValidator
import dagger.Module
import dagger.Provides
import me.dm7.barcodescanner.zxing.ZXingScannerView
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    private val cicerone by lazy {
        Cicerone.create()
    }

    @Provides
    @Singleton
    fun router(): Router = cicerone.router

    @Provides
    @Singleton
    fun navigationHolder(): NavigatorHolder = cicerone.navigatorHolder

    @Provides
    @Singleton
    @NonNull
    fun db(): DataBase = Room.databaseBuilder(context, DataBase::class.java, "database.db").build()

    @Provides
    @Singleton
    @NonNull
    fun cardsRepository(db: DataBase): ICardsRepository = CardsRepository(db)

    @Provides
    @NonNull
    fun cardValidator(): ICardValidator = CardValidator()

    @Provides
    fun scannerView(): ZXingScannerView = ZXingScannerView(context)

}