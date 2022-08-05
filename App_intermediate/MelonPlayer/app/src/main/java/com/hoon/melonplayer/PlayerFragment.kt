package com.hoon.melonplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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

/*
TODO:
 - UI 변경
 - splash activity 추가
 - player view 업데이트 -> 완료
 - ui 처리 -> 완료
    seekbar
    time
 - activity -> fragment 이동 시 자연스러움을 위해
    투명도 조절하거나
    모션레이아웃 사용하기
 - recyclerview 삽입, 삭제, 위치변경 등 구현해보기
 - favorite 페이지등 추가 구현해보기
 */

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val playListAdapter = PlayListAdapter(onClick = { play(it) })
    val musicModels = mutableListOf<MusicModel>()

    private lateinit var player: SimpleExoPlayer
    private var isWatchingPlayListView: Boolean = true
    private val traceSeekRunnable = Runnable {
        traceSeekTime()
    }

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

        initPlayer()
        initPlayListButton()
        initControlButtons()
        initSeekBar()
        initRecyclerView()

        getMusicListFromServer()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initSeekBar() {
        binding.playerSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar ?: return
                val currentMillis = (seekBar.progress * 1000).toLong()

                player.seekTo(currentMillis)
            }
        })

        binding.playListSeekBar.setOnTouchListener { v, event ->  true}
    }

    override fun onStop() {
        player.pause()
        view?.removeCallbacks { traceSeekRunnable }
        super.onStop()
    }

    override fun onDestroy() {
        player.release()
        view?.removeCallbacks { traceSeekRunnable }
        super.onDestroy()
    }

    private fun play(windowIndex: Int) {
        player.seekTo(windowIndex, 0)
        player.play()
    }

    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        player.addListener(object : Player.EventListener {

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    binding.ivPlayPause.setImageResource(R.drawable.ic_pause_48)
                } else {
                    binding.ivPlayPause.setImageResource(R.drawable.ic_play_arrow_48)
                }

                super.onIsPlayingChanged(isPlaying)
            }

            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)

                traceSeekTime()
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                // play item이 변경되었을 때 호출됨
                super.onMediaItemTransition(mediaItem, reason)
                mediaItem ?: return

                updatePlayListView(mediaItem.mediaId.toInt())
                updatePlayerView(mediaItem.mediaId.toInt())
            }
        })
    }

    private fun traceSeekTime() {
        val state = player.playbackState

        view?.removeCallbacks { traceSeekRunnable } // 여러번 호출되는것을 막기 위함
        if (state != Player.STATE_IDLE && state != Player.STATE_ENDED) {
            val duration = if (player.duration >= 0) player.duration else 0
            val currentPosition = player.currentPosition

            updateSeekTimeUI(duration, currentPosition)

            view?.postDelayed(traceSeekRunnable, 1000)
        }
    }

    // duration, currentPosition : Millis
    private fun updateSeekTimeUI(duration: Long, currentPosition: Long) {
        val duration = (duration / 1000).toInt()
        val currentPosition = (currentPosition / 1000).toInt()

        binding.playerSeekBar.max = duration
        binding.playerSeekBar.progress = currentPosition

        binding.playListSeekBar.max = duration
        binding.playListSeekBar.progress = currentPosition

        binding.tvPlayTime.text = "%02d:%02d".format(currentPosition / 60, currentPosition % 60)
        binding.tvTotalTime.text = "%02d:%02d".format(duration / 60, duration % 60)
    }

    private fun updatePlayerView(idx: Int) {
        binding.tvTitle.text = musicModels[idx].title
        binding.tvArtist.text = musicModels[idx].artist

        Glide.with(binding.root.context)
            .load(musicModels[idx].coverImageURL)
            .into(binding.coverImageView)
    }

    private fun updatePlayListView(idx: Int) {
        val newModel = musicModels.mapIndexed { index, musicModel ->
            val newItem = musicModel.copy(
                isPlayingStatus = (index == idx)
            )
            newItem
        }

        playListAdapter.submitList(newModel)
    }

    private fun initControlButtons() {
        binding.ivPlayPause.setOnClickListener {

            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }

        binding.ivSkipNext.setOnClickListener {
            var currentIdx = player.currentWindowIndex
            currentIdx = if (currentIdx + 1 >= player.mediaItemCount) 0 else currentIdx + 1

            play(currentIdx)
        }

        binding.ivSkipPrev.setOnClickListener {
            var currentIdx = player.currentWindowIndex
            currentIdx = if (currentIdx - 1 < 0) player.mediaItemCount - 1 else currentIdx - 1

            play(currentIdx)
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
            binding.playListViewGroup.isVisible = !isWatchingPlayListView

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
        player.setMediaItems(
            musicModels.mapIndexed { index, musicModel ->
                MediaItem.Builder()
                    .setMediaId(index.toString())
                    .setUri(musicModel.musicURL)
                    .build()
            })
        player.prepare()
    }

    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}