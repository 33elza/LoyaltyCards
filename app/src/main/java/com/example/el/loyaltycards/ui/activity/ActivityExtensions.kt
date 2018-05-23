package com.example.el.loyaltycards.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.presenter.CARD_KEY
import com.example.el.loyaltycards.presentation.presenter.ImageModule
import com.example.el.loyaltycards.presentation.presenter.MODULE_KEY
import com.example.el.loyaltycards.presentation.presenter.Module
import com.example.el.loyaltycards.ui.fragment.BarcodeFragment
import com.example.el.loyaltycards.ui.fragment.CardDetailsFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace

const val CARD_LIST_MODULE = "CARD_LIST_MODULE"
const val CARD_BARCODE_MODULE = "CARD_BARCODE_MODULE"
const val CARD_DETAILS_MODULE = "CARD_DETAILS_MODULE"
const val CARD_CONTAINER_MODULE = "CARD_CONTAINER_MODULE"
const val FULLSCREEN_MODULE = "FULLSCREEN_MODULE"

const val URI = "URI"
const val SHARED_ELEMENT = "SHARED_ELEMENT"

val AppCompatActivity.navigator: Navigator
    get() {
        return object : SupportAppNavigator(this, this.supportFragmentManager, R.id.container) {
            override fun createActivityIntent(context: Context, screenKey: String, data: Any?): Intent? {
                return when (screenKey) {
                    CARD_CONTAINER_MODULE -> Intent(context, CardActivity::class.java).apply {
                        if (data != null) {
                            data as Module
                            putExtra(CARD_KEY, data.card)
                            putExtra(MODULE_KEY, data.ordinal)
                        }
                    }
                    CARD_LIST_MODULE -> Intent(context, CardsActivity::class.java)
                    FULLSCREEN_MODULE -> Intent(context, ShowImageFullscreenActivity::class.java).apply {
                        if (data != null) {
                            data as ImageModule
                            putExtra(URI, data.uri)
                            putExtra(SHARED_ELEMENT, data.ordinal)
                        }
                    }
                    else -> null
                }
            }

            override fun createFragment(screenKey: String, data: Any?): Fragment? {
                return when (screenKey) {
                    CARD_DETAILS_MODULE -> CardDetailsFragment.newInstance().apply {
                        if (data != null) {
                            arguments = Bundle().apply {
                                putParcelable(CARD_KEY, data as Card)
                            }
                        }
                    }
                    CARD_BARCODE_MODULE -> BarcodeFragment.newInstance().apply {
                        if (data != null) {
                            arguments = Bundle().apply {
                                putParcelable(CARD_KEY, data as Card)
                            }
                        }
                    }
                    else -> null
                }
            }

            override fun createStartActivityOptions(command: Command?, activityIntent: Intent?): Bundle? {

                if (activityIntent?.extras?.get(SHARED_ELEMENT) != null) {
                    when (ImageModule.values()[activityIntent.extras?.get(SHARED_ELEMENT) as Int]) {
                        ImageModule.FRONT -> {
                            return ActivityOptionsCompat.makeSceneTransitionAnimation(this@navigator,
                                    this@navigator.findViewById(R.id.cardFrontPhoto),
                                    "imageView").toBundle()
                        }
                        ImageModule.BACK -> {
                            return ActivityOptionsCompat.makeSceneTransitionAnimation(this@navigator,
                                    this@navigator.findViewById(R.id.cardBackPhoto),
                                    "imageView").toBundle()
                        }
                        else -> return super.createStartActivityOptions(command, activityIntent)
                    }
                } else {
                    return super.createStartActivityOptions(command, activityIntent)
                }
            }

            override fun setupFragmentTransactionAnimation(command: Command?, currentFragment: Fragment?, nextFragment: Fragment?, fragmentTransaction: FragmentTransaction?) {
                super.setupFragmentTransactionAnimation(command, currentFragment, nextFragment, fragmentTransaction)

                if (command is Replace) {
                    fragmentTransaction?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }

            override fun showSystemMessage(message: String?) {
                showMessage(message)
            }
        }
    }

fun Activity.showMessage(message: String?) {
    if (message.isNullOrEmpty()) return

    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}