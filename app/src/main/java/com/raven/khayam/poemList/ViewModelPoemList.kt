package com.raven.khayam.poemList

import androidx.lifecycle.ViewModel
import com.farhad.editableprofile.utils.SingleLiveEvent
import com.raven.khayam.domain.model.Poem
import com.raven.khayam.domain.usecase.base.DefaultObserver
import com.raven.khayam.domain.usecase.getPoems.GetPoems
import com.raven.khayam.mapper.PoemItemMapper
import com.raven.khayam.model.PoemItem
import javax.inject.Inject

class ViewModelPoemList @Inject constructor(private val getPoems: GetPoems, private val poemItemMapper: PoemItemMapper):ViewModel() {

    val poemList = arrayListOf<PoemItem>()

    val showPoems : SingleLiveEvent<Unit> by lazy { SingleLiveEvent<Unit>() }

    fun viewIsReady(){
        loadPoems()
    }

    private fun loadPoems(){
        val observer = object : DefaultObserver<List<Poem>>(){
            override fun onNext(t: List<Poem>) {
                super.onNext(t)
                poemList.addAll(poemItemMapper.mapToPresentation(t) )
                showPoems.call()
            }
        }
        getPoems.execute(observer)
    }
}