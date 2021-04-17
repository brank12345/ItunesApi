package com.example.itunesapi

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val musicList = MutableLiveData<List<MusicUIData>>()
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    var searchText: String = ""
    private var currentPlayingMusicId: Int? = null
    private val mediaPlayer = MediaPlayer()

    fun search() {
        viewModelScope.launch {
            isLoading.value = true
            musicList.value = emptyList()
            val result = safeApiCall {
                Api.create().search(searchText, "US", "musicTrack", "musicTrack", 25).getResult()
            }
            isLoading.value = false

            when (result) {
                is Result.Success -> {
                    musicList.value = mutableListOf<MusicUIData>().apply {
                        result.data.results.forEach { music ->
                            add(MusicUIData(music, false))
                        }
                    }
                }
                is Result.Error -> errorMessage.value = result.exception.message
            }
        }
    }

    fun onMusicItemClick(id: Int) {
        if (musicList.value?.find { it.music.id == id }?.isPlaying == true) {
            stopMusicItem(id)
        } else {
            playMusicItem(id)
        }
    }

    private fun stopMusicItem(stopId: Int) {
        musicList.value = musicList.value?.apply {
            find { it.music.id == stopId }?.apply {
                try {
                    mediaPlayer.stop()
                    //mediaPlayer.prepare()
                    currentPlayingMusicId = null
                    isPlaying = false
                } catch (e: Exception) {
                    isPlaying = false
                    currentPlayingMusicId = null
                    errorMessage.value = "Something went wrong when stopping music"
                }
            }
        }
    }

    private fun playMusicItem(id: Int) {
        musicList.value = musicList.value?.apply {
            find { it.isPlaying }?.apply {
                try {
                    mediaPlayer.stop()
                    mediaPlayer.prepare()
                } catch (e: Exception) {
                    errorMessage.value = "Something went wrong when stopping music"
                }
                isPlaying = false
            }

            find { it.music.id == id }?.apply {
                setUpMediaPlayer()
            }
        }
    }

    private fun MusicUIData.setUpMediaPlayer() {
        try {
            mediaPlayer.apply {
                reset()
                setDataSource(music.previewUrl)
                setOnCompletionListener {
                    onCurrentPlayingMusicComplete()
                }
                prepare()
                start()
            }
            currentPlayingMusicId = music.id
            isPlaying = true
        } catch (e: Exception) {
            isPlaying = false
            currentPlayingMusicId = null
            errorMessage.value = "Something went wrong when set up media player"
        }
    }

    private fun onCurrentPlayingMusicComplete() {
        musicList.value = musicList.value?.apply {
            find { it.music.id == currentPlayingMusicId }?.apply {
                isPlaying = false
                currentPlayingMusicId = null
            }
        }
    }
}

data class MusicUIData(val music: Music, var isPlaying: Boolean)