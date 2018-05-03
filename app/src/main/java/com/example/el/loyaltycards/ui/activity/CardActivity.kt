package com.example.el.loyaltycards.ui.activity

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.el.loyaltycards.App
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.presentation.presenter.CardPresenter
import com.example.el.loyaltycards.presentation.view.CardView
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

class CardActivity : MvpAppCompatActivity(), CardView {

    @InjectPresenter
    lateinit var presenter: CardPresenter

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        App.component.inject(this)

        presenter.onCreate(intent.extras)
    }

    override fun onResume() {
        navigatorHolder.setNavigator(navigator)
        super.onResume()
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
