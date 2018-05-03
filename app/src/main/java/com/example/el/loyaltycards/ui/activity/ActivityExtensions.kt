package com.example.el.loyaltycards.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.presenter.CARD_KEY
import com.example.el.loyaltycards.presentation.presenter.MODULE_KEY
import com.example.el.loyaltycards.presentation.presenter.Module
import com.example.el.loyaltycards.ui.fragment.BarcodeFragment
import com.example.el.loyaltycards.ui.fragment.CardDetailsFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

const val CARD_LIST_MODULE = "CARD_LIST_MODULE"
const val CARD_BARCODE_MODULE = "CARD_BARCODE_MODULE"
const val CARD_DETAILS_MODULE = "CARD_DETAILS_MODULE"
const val CARD_CONTAINER_MODULE = "CARD_CONTAINER_MODULE"

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

            override fun setupFragmentTransactionAnimation(command: Command?, currentFragment: Fragment?, nextFragment: Fragment?, fragmentTransaction: FragmentTransaction?) {
                super.setupFragmentTransactionAnimation(command, currentFragment, nextFragment, fragmentTransaction)

//                if (command is Replace) {
//                    fragmentTransaction?.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//                }
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