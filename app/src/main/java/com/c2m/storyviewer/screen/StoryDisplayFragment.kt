package com.c2m.storyviewer.screen

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.c2m.storyviewer.R
import com.c2m.storyviewer.customview.StoriesProgressView
import com.c2m.storyviewer.data.Story
import com.c2m.storyviewer.data.StoryUser
import com.c2m.storyviewer.utils.OnSwipeTouchListener
import com.c2m.storyviewer.utils.hide
import com.c2m.storyviewer.utils.show
import com.danikula.videocache.HttpProxyCacheServer
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_story_display.*
import java.util.*

class StoryDisplayFragment : Fragment(),
    StoriesProgressView.StoriesListener {

    var simpleExoPlayer: SimpleExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    private val position: Int by
    lazy { arguments?.getInt(EXTRA_POSITION) ?: 0 }

    private val storyUser: StoryUser by
    lazy {
        (arguments?.getParcelable<StoryUser>(
            EXTRA_STORY_USER
        ) as StoryUser)
    }

    private val stories: ArrayList<Story> by
    lazy { storyUser.stories }

    private var pageViewOperator: PageViewOperator? = null
    private var counter = 0
    private var pressTime = 0L
    private var limit = 500L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_story_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storyDisplayVideo.useController = false
        updateStory()
        setUpUi()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.pageViewOperator = context as PageViewOperator
    }

    override fun onStart() {
        super.onStart()
        counter = restorePosition()
    }

    override fun onResume() {
        super.onResume()
        if (counter == 0) {
            simpleExoPlayer?.playWhenReady = true
            storiesProgressView?.startStories()
        } else {
            simpleExoPlayer?.playWhenReady = true
            // restart animation
            counter = MainActivity.progressState.get(arguments?.getInt(EXTRA_POSITION) ?: 0)
            storiesProgressView?.startStories(counter)
        }
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.playWhenReady = false
        storiesProgressView?.abandon()
    }

    override fun onComplete() {
        simpleExoPlayer?.release()
        pageViewOperator?.nextPageView()
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        --counter
        savePosition(counter)
        updateStory()
    }

    override fun onNext() {
        if (stories.size <= counter + 1) {
            return
        }
        ++counter
        savePosition(counter)
        updateStory()
    }

    private fun updateStory() {
        simpleExoPlayer?.stop()
        if (stories[counter].url.contains("mp4") || stories[counter].url.contains("video", true)
        ) {
            storyDisplayVideo.show()
            storyDisplayImage.hide()
            storyDisplayVideoProgress.show()
            initializePlayer()
        } else {
            storyDisplayVideo.hide()
            storyDisplayVideoProgress.hide()
            storyDisplayImage.show()
            Glide.with(this).load(stories[counter].url).into(storyDisplayImage)
        }

        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH).apply {
            timeInMillis = stories[counter].storyDate
        }

        storyDisplayTime.text = DateFormat.format("MM-dd-yyyy HH:mm:ss", cal).toString()
    }

    private fun setUpUi() {
        val touchListener = object : OnSwipeTouchListener(activity!!) {
            override fun onSwipeTop() {
                Toast.makeText(activity, "onSwipeTop", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeBottom() {
                Toast.makeText(activity, "onSwipeBottom", Toast.LENGTH_LONG).show()
            }

            override fun onClick(view: View) {
                when (view) {
                    next -> {
                        if (counter == stories.size - 1) {
                            pageViewOperator?.nextPageView()
                        } else {
                            storiesProgressView?.skip()
                        }
                    }
                    previous -> {
                        if (counter == 0) {
                            pageViewOperator?.backPageView()
                        } else {
                            storiesProgressView?.reverse()
                        }
                    }
                }
            }

            override fun onLongClick() {
                hideStoryOverlay()
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressTime = System.currentTimeMillis()
                        pauseCurrentStory()
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        showStoryOverlay()
                        resumeCurrentStory()
                        return limit < System.currentTimeMillis() - pressTime
                    }
                }
                return false
            }
        }
        previous.setOnTouchListener(touchListener)
        next.setOnTouchListener(touchListener)

        storiesProgressView?.setStoriesCountDebug(
            stories.size, position = arguments?.getInt(EXTRA_POSITION) ?: -1
        )
        storiesProgressView?.setAllStoryDuration(4000L)
        storiesProgressView?.setStoriesListener(this)

        Glide.with(this).load(storyUser.profilePicUrl).circleCrop().into(storyDisplayProfilePicture)
        storyDisplayNick.text = storyUser.username
    }

    private fun initializePlayer() {
        if (simpleExoPlayer == null)
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
        else {
            simpleExoPlayer?.release()
            simpleExoPlayer = null
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(requireContext())
        }

        val proxyServer =
            HttpProxyCacheServer.Builder(context).maxCacheSize(1024 * 1024 * 1024).build()
        val proxyUURL = proxyServer.getProxyUrl(stories[counter].url)
        mediaDataSourceFactory = DefaultDataSourceFactory(
            requireContext(),
            Util.getUserAgent(requireContext(), getString(R.string.app_name))
        )
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
            Uri.parse(proxyUURL)
        )
        simpleExoPlayer?.prepare(mediaSource, false, false)
        simpleExoPlayer?.playWhenReady = true

        storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
        storyDisplayVideo.player = simpleExoPlayer

        simpleExoPlayer?.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException?) {
                super.onPlayerError(error)
                storyDisplayVideoProgress.visibility = View.GONE
                if (counter == stories.size.minus(1)) {
                    pageViewOperator?.nextPageView()
                } else {
                    storiesProgressView?.skip()
                }
            }

            override fun onLoadingChanged(isLoading: Boolean) {
                super.onLoadingChanged(isLoading)

                if (isLoading) {
                    storyDisplayVideoProgress.visibility = View.VISIBLE
                    pressTime = System.currentTimeMillis()
                    pauseCurrentStory()
                    hideStoryOverlay()
                } else {
                    storiesProgressView?.getProgressWithIndex(counter)?.setDuration(
                        8000L.coerceAtMost( // 8000L Max video duration
                            simpleExoPlayer?.duration
                                ?: 8000L
                        )
                    )
                    showStoryOverlay()
                    resumeCurrentStory()
                    storyDisplayVideoProgress.visibility = View.GONE
                }
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                when (playbackState) {
                    Player.STATE_READY -> {
                        showStoryOverlay()
                    }
                    Player.STATE_ENDED -> {
                        if (counter == 0) {
                            pageViewOperator?.backPageView()
                        } else {
                            storiesProgressView?.reverse()
                        }
                    }
                }
            }
        })
    }

    private fun showStoryOverlay() {
        if (storyOverlay == null || storyOverlay.alpha != 0F) return

        storyOverlay.animate()
            .setDuration(100)
            .alpha(1F)
            .start()
    }

    private fun hideStoryOverlay() {
        if (storyOverlay == null || storyOverlay.alpha != 1F) return

        storyOverlay.animate()
            .setDuration(200)
            .alpha(0F)
            .start()
    }

    private fun savePosition(pos: Int) {
        MainActivity.progressState.put(position, pos)
    }

    private fun restorePosition(): Int {
        return MainActivity.progressState.get(position)
    }

    fun pauseCurrentStory() {
        simpleExoPlayer?.playWhenReady = false
        storiesProgressView?.pause()
    }

    fun resumeCurrentStory() {
        simpleExoPlayer?.playWhenReady = true
        showStoryOverlay()
        storiesProgressView?.resume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        simpleExoPlayer?.release()
    }

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_STORY_USER = "EXTRA_STORY_USER"
        fun newInstance(position: Int, story: StoryUser): StoryDisplayFragment {
            return StoryDisplayFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putParcelable(EXTRA_STORY_USER, story)
                }
            }
        }
    }
}