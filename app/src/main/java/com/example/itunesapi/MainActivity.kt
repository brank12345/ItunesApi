package com.example.itunesapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { getViewModel<MainViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.apply {
            addItemDecoration(VerticalMarginItemDecoration(0, dp(8), 0, dp(8)))
            adapter = MusicAdapter {
                viewModel.onMusicItemClick(it)
            }
        }
        editor.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchText = s?.toString() ?: ""
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })
        searchIcon.setOnClickListener {
            viewModel.search()
            closeKeyboard(this, editor)
        }
        viewModel.observeData()
    }

    private fun MainViewModel.observeData() {
        observe(musicList) {
            it ?: return@observe
            (recyclerView.adapter as MusicAdapter?)?.setData(it)
        }

        observe(errorMessage) {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
        }

        observe(isLoading) {
            progressBar.visibility = if (it == true) View.VISIBLE else View.GONE
        }
    }
}