package com.example.el.loyaltycards.ui.activity

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.example.el.loyaltycards.App
import com.example.el.loyaltycards.R
import com.example.el.loyaltycards.entity.Card
import com.example.el.loyaltycards.presentation.presenter.CardsPresenter
import com.example.el.loyaltycards.presentation.view.CardsView
import com.example.el.loyaltycards.ui.adapter.CardListAdapter
import com.example.el.loyaltycards.ui.adapter.CardsItemDecoration
import kotlinx.android.synthetic.main.activity_cards.*
import kotlinx.android.synthetic.main.bottom_sheet_card_dialog.view.*
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

private const val GRID_SIZE_PORTRAIT = 2
private const val GRID_SIZE_LANDSCAPE = 3

class CardsActivity : MvpAppCompatActivity(), CardsView {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var presenter: CardsPresenter

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    lateinit var cardListAdapter: CardListAdapter

    private var gridSize = GRID_SIZE_PORTRAIT

    private val bottomSheetDialog by lazy {
        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.bottom_sheet_card_dialog, null).apply {
            this.navView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.editMenuItem -> {
                        dialog.hide()
                        presenter.onEditCard()
                        true
                    }
                    R.id.deleteMenuItem -> {
                        dialog.hide()
                        presenter.onDeleteCard()
                        true
                    }
                    else -> false
                }
            }
        }
        dialog.setContentView(view)

        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(android.support.design.R.id.design_bottom_sheet) as FrameLayout?
            BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED
        }
        dialog
    }

    // LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)
        setSupportActionBar(toolbar)

        App.component.inject(this)

        fab.setOnClickListener { presenter.onAddFabListener() }

        cardListAdapter = CardListAdapter(presenter.cards, presenter.getAdapterListener())

        gridSize = defineGridSize()

        cardListRecyclerView.layoutManager = GridLayoutManager(this, gridSize)
        cardListRecyclerView.adapter = cardListAdapter
        cardListRecyclerView.setHasFixedSize(true)
        cardListRecyclerView.addItemDecoration(CardsItemDecoration(gridSize))

        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun defineGridSize(): Int {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            return GRID_SIZE_PORTRAIT
        else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            return GRID_SIZE_LANDSCAPE
        else return GRID_SIZE_PORTRAIT
    }

    // CardsView
    override fun setCards(cards: List<Card>) {
        cardListAdapter.cards = cards
        cardListAdapter.notifyDataSetChanged()
    }

    override fun showDialog() {
        bottomSheetDialog.show()
    }

    override fun notifyCardDeleted(selectedIndex: Int) {
        cardListAdapter.notifyItemRemoved(selectedIndex)
        val mod = selectedIndex % gridSize
        cardListAdapter.notifyItemRangeChanged(selectedIndex - mod, cardListAdapter.itemCount - selectedIndex + mod)
    }
}
