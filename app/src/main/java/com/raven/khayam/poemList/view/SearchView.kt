package com.raven.khayam.poemList.view

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView.OnEditorActionListener
import com.raven.khayam.databinding.ViewSearchBinding


class SearchView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var _binding: ViewSearchBinding? = null
    private val binding get() = _binding!!

    var isOpen = false
        private set

    var listener: Listener? = null

    private var searchPhrase = ""

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        _binding = ViewSearchBinding.inflate(inflater, this, true)

        binding.openSearchButton.setOnClickListener { openSearch() }
        binding.closeSearchButton.setOnClickListener { closeSearch() }

        binding.executeSearchButton.setOnClickListener { performSearch() }

        binding.searchInputText.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun performSearch() {
        if (searchPhrase != binding.searchInputText.text.toString()) {
            searchPhrase = binding.searchInputText.text.toString()
            listener?.onSearchListener(searchPhrase)
        }
        closeKeyboard()
    }

    fun openSearch() {
        isOpen = true
        binding.openSearchButton.setOnClickListener(null)
        binding.searchInputText.setText("")
        binding.searchOpenView.visibility = View.VISIBLE
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            binding.searchOpenView,
            (binding.openSearchButton.right + binding.openSearchButton.left) / 2,
            (binding.openSearchButton.top + binding.openSearchButton.bottom) / 2,
            0f, width.toFloat()
        )
        binding.searchInputText.requestFocus()
        showKeyboard()
        circularReveal.duration = 200
        circularReveal.start()
    }

    fun closeSearch() {
        listener?.onSearchCloseListener()
        isOpen = false
        binding.openSearchButton.setOnClickListener { openSearch() }
        val circularConceal = ViewAnimationUtils.createCircularReveal(
            binding.searchOpenView,
            (binding.openSearchButton.right + binding.openSearchButton.left) / 2,
            (binding.openSearchButton.top + binding.openSearchButton.bottom) / 2,
            width.toFloat(), 0f
        )
        closeKeyboard()
        circularConceal.duration = 200
        circularConceal.start()
        circularConceal.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) = Unit
            override fun onAnimationCancel(animation: Animator) = Unit
            override fun onAnimationStart(animation: Animator) = Unit
            override fun onAnimationEnd(animation: Animator) {
                binding.searchOpenView.visibility = View.INVISIBLE
                binding.searchInputText.setText("")
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

    interface Listener {
        fun onSearchListener(searchPhrase: String)
        fun onSearchCloseListener()
    }
}