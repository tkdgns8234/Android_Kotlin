package com.hoon.melonplayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.hoon.melonplayer.databinding.FragmentPlayerBinding
import com.hoon.melonplayer.model.MusicModel
import com.hoon.melonplayer.service.MusicDTO
import com.hoon.melonplayer.service.MusicService
import com.hoon.melonplayer.util.mapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlayerFragment : Fragment() {
    private lateinit var _binding: FragmentPlayerBinding
    private val binding get() = _binding!!

    private val playListAdapter = PlayListAdapter(onClick = { index ->
        Log.e("test", "enter onclick")
        updatePlayListView(index)
        play(index)
    })

    private var player: SimpleExoPlayer? = null

    var musicModels = mutableListOf<MusicModel>()
    var isWatchingPlayListView: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPlayerView()
        initPlayListButton()
        initControlButtons()
        initRecyclerView()
        getMusicListFromServer()
    }

    private fun initPlayerView() {
        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }
        binding.playerView.player = player
        player?.addListener(object : Player.EventListener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                mediaItem ?: return
                Log.e("test", "test")

                updatePlayListView(mediaItem.mediaId.toInt())
            }
        })
    }

    private fun updatePlayListView(i: Int) {
        val newModel = musicModels.mapIndexed { index, musicModel ->
            val newItem = musicModel.copy(
                isPlayingStatus = index == i
            )
            newItem
        }

        playListAdapter.submitList(newModel)
    }

    private fun play(windowIndex: Int) {
        player?.seekTo(windowIndex, 0)
        player?.play()
    }

    private fun initControlButtons() {
        binding.ivPlayPause.setOnClickListener {
            player?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    binding.ivPlayPause.setImageResource(R.drawable.ic_play_arrow_48)
                } else {
                    player.play()
                    binding.ivPlayPause.setImageResource(R.drawable.ic_pause_48)
                }
            }
        }

        binding.ivSkipNext.setOnClickListener {

        }

        binding.ivSkipPrev.setOnClickListener {

        }
    }

    private fun initRecyclerView() {
        binding.rvPlayList.adapter = playListAdapter
        binding.rvPlayList.layoutManager = LinearLayoutManager(context)
    }

    private fun initPlayListButton() {
        binding.ivPlayList.setOnClickListener {
            // TODO: 서버에서 가져온 데이터가 없는 경우
            binding.playerViewGroup.isVisible = isWatchingPlayListView
            binding.playListViewGroup.isVisible = isWatchingPlayListView

            isWatchingPlayListView = !isWatchingPlayListView
        }
    }

    private fun getMusicListFromServer() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(MusicService::class.java).also {
            it.getMusicList()
                .enqueue(object : Callback<MusicDTO> {
                    override fun onResponse(call: Call<MusicDTO>, response: Response<MusicDTO>) {
                        if (response.isSuccessful.not()) {
                            Log.e("PlayerFragment", "music 데이터 load 실패")
                            return
                        }

                        response.body()?.let { musicDTO ->
                            musicDTO.musics.forEachIndexed { index, musicEntity ->
                                val musicModel = musicEntity.mapper(index)
                                musicModels.add(musicModel)
                            }

                            playListAdapter.submitList(musicModels.toList())
                            setMusicItems()
                        }
                    }

                    override fun onFailure(call: Call<MusicDTO>, t: Throwable) {}
                })
        }
    }

    private fun setMusicItems() {
        player?.setMediaItems(
            musicModels.mapIndexed { index, musicModel ->
                MediaItem.Builder()
                    .setMediaId(index.toString())
                    .setUri(musicModel.musicURL)
                    .build()
            })
        player?.prepare()
    }

    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}