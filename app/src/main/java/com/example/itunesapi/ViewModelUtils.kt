package com.example.itunesapi

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BaseViewModelFactory<T>(val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return creator() as T
    }
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)
}

inline fun <T> AppCompatActivity.observe(data: LiveData<T>, crossinline action: (T?) -> Unit) {
    data.observe(this, Observer { action(it) })
}