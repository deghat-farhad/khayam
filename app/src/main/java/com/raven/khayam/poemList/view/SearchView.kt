package com.raven.khayam.poemList.view

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.raven.khayam.R
import kotlinx.android.synthetic.main.view_search.view.*


class SearchView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_search, this, true)

        open_search_button.setOnClickListener { openSearch() }
        close_search_button.setOnClickListener { closeSearch() }
    }

    private fun openSearch() {
        open_search_button.setOnClickListener(null)
        search_input_text.setText("")
        search_open_view.visibility = View.VISIBLE
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            search_open_view,
            (open_search_button.right + open_search_button.left) / 2,
            (open_search_button.top + open_search_button.bottom) / 2,
            0f, width.toFloat()
        )
        circularReveal.duration = 200
        search_input_text.requestFocus()
        showKeyboard()
        circularReveal.start()
    }

    private fun closeSearch() {
        open_search_button.setOnClickListener { openSearch() }
        val circularConceal = ViewAnimationUtils.createCircularReveal(
            search_open_view,
            (open_search_button.right + open_search_button.left) / 2,
            (open_search_button.top + open_search_button.bottom) / 2,
            width.toFloat(), 0f
        )

        circularConceal.duration = 200
        closeKeyboard()
        circularConceal.start()
        circularConceal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) = Unit
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationStart(animation: Animator?) = Unit
            override fun onAnimationEnd(animation: Animator?) {
                search_open_view.visibility = View.INVISIBLE
                search_input_text.setText("")
                circularConceal.removeAllListeners()
            }
        })
    }

    private fun showKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            0
        )
    }

    private fun closeKeyboard() {
        val view = this.focusedChild
        view?.let { v ->
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}