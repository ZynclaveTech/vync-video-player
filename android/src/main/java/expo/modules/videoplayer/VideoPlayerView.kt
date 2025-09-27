package expo.modules.videoplayer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView
import expo.modules.video.ProgressTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

@UnstableApi
class VideoPlayerView(
    context: Context,
    appContext: AppContext,
) : ExpoView(context, appContext) {
    private var playerScope: CoroutineScope? = null

    private lateinit var playerView: PlayerView
    private lateinit var thumbnailImageView: ImageView
    var player: ExoPlayer? = null

    private var progressTracker: ProgressTracker? = null

    var url: Uri? = null
    var autoplay = false
    var beginMuted = true
    var ignoreAutoplay = false
    
    // Proximity-based autoplay
    var enableProximityAutoplay: Boolean = false
    var proximityThreshold: Double = 0.3

    // Thumbnail support
    var thumbnailUrl: String? = null
    var showThumbnailWhileLoading: Boolean = false
    var showThumbnailWhenInactive: Boolean = false
    
    // Memory management
    private var isNearby: Boolean = false
    private var isInProximityMode: Boolean = false

    var isFullscreen: Boolean = false
        private set(value) {
            field = value
            this.playerView.useController = value

            onStatusChange(
                mapOf(
                    "isFullscreen" to value,
                )
            )
        }

    private var isPlaying: Boolean = false
        set(value) {
            field = value
            onStatusChange(
                mapOf(
                    "status" to if (value) "playing" else "paused",
                ),
            )
        }

    private var isMuted: Boolean = true
        set(value) {
            field = value
            onMutedChange(
                mapOf(
                    "isMuted" to value,
                ),
            )
        }

    private var isLoading: Boolean = false
        set(value) {
            field = value
            onLoadingChange(
                mapOf(
                    "isLoading" to value,
                ),
            )
            updateThumbnailVisibility()
        }

    private var isViewActive: Boolean = false
        set(value) {
            field = value
            onActiveChange(
                mapOf(
                    "isActive" to value,
                ),
            )
            updateThumbnailVisibility()
        }

    var forceTakeover: Boolean = false
        set(value) {
            if (value == field) {
                return
            }

            field = value
            if (value) {
                ViewManager.setActiveView(this)
            }
        }

    private val onActiveChange by EventDispatcher()
    private val onLoadingChange by EventDispatcher()
    private val onMutedChange by EventDispatcher()
    private val onPlayerPress by EventDispatcher()
    private val onStatusChange by EventDispatcher()
    private val onTimeRemainingChange by EventDispatcher()
    private val onFullscreenChange by EventDispatcher()
    private val onError by EventDispatcher()
    private val onPictureInPictureChange by EventDispatcher()

    private var enteredFullscreenMuteState = true

    // Picture-in-Picture support
    var enablePictureInPicture: Boolean = false
    var pipDimensions: Map<String, Double> = emptyMap()
    private var isPictureInPicture: Boolean = false
        set(value) {
            field = value
            onPictureInPictureChange(
                mapOf(
                    "isPictureInPicture" to value,
                )
            )
        }

    init {
        this.playerView = PlayerView(context).apply {
            setBackgroundColor(Color.BLACK)
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            useController = false
            setOnClickListener { _ ->
                onPlayerPress(mapOf())
            }
        }
        
        this.thumbnailImageView = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundColor(Color.BLACK)
            visibility = android.view.View.GONE
        }
        
        // Create a FrameLayout to hold both views
        val container = android.widget.FrameLayout(context)
        
        // Add player view to container
        container.addView(
            playerView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            ),
        )
        
        // Add thumbnail view to container
        container.addView(
            thumbnailImageView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            ),
        )
        

        
        // Add container to this view
        this.addView(
            container,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            ),
        )
        

        
        // Views are already assigned above
        
        // Initial thumbnail visibility check
        this.post {
            this.updateThumbnailVisibility()
        }
    }

    // Lifecycle

    private fun setup() {
        // We shouldn't encounter this scenario, but would rather be safe than sorry here and just
        // skip setup if we do.
        if (this.player != null) {
            return
        }

        this.isLoading = true

        val player = this.createExoPlayer()
        this.player = player
        this.playerView.player = player

        val playerScope = CoroutineScope(Job() + Dispatchers.Main)
        playerScope.launch {
            val mediaItem = createMediaItem()
            player.setMediaItem(mediaItem)
            player.prepare()
        }
        this.playerScope = playerScope
    }

    private fun destroy() {
        val player = this.player ?: return

        this.isLoading = false
        this.ignoreAutoplay = false

        // Exit proximity mode if active
        if (this.isInProximityMode) {
            this.exitProximityMode()
        }

        this.pause()

        player.release()
        this.player = null
        this.playerView.player = null
        this.playerScope?.cancel()
        this.playerScope = null
        
        // Keep thumbnail visible when video is paused
        this.updateThumbnailVisibility()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ViewManager.addView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ViewManager.removeView(this)
        // Only destroy when view is actually being removed from window
        this.destroy()
    }

    // Thumbnail support

    private fun updateThumbnailVisibility() {
        val shouldShowThumbnail = (
            (this.isLoading && this.showThumbnailWhileLoading) ||
            (!this.isViewActive && this.showThumbnailWhenInactive)
        )
        
        if (shouldShowThumbnail && this.thumbnailUrl != null) {
            this.loadThumbnailImage()
            this.thumbnailImageView.visibility = android.view.View.VISIBLE
        } else {
            this.thumbnailImageView.visibility = android.view.View.GONE
        }
    }

    private fun loadThumbnailImage() {
        val thumbnailUrl = this.thumbnailUrl ?: return
        
        Glide.with(this.context)
            .load(thumbnailUrl)
            .into(this.thumbnailImageView)
    }

    // Controls

    private fun play() {
        this.addProgressTracker()
        this.player?.play()
        this.isPlaying = true
    }

    private fun pause() {
        this.player?.pause()
        this.removeProgressTracker()
        this.isPlaying = false
    }

    fun togglePlayback() {
        if (this.isPlaying) {
            this.pause()
        } else {
            if (this.player == null) {
                ViewManager.setActiveView(this)
                this.ignoreAutoplay = true
                this.setup()
            } else {
                this.play()
            }
        }
    }

    fun mute() {
        this.player?.volume = 0f
        this.isMuted = true
        VideoPlayerModule.audioFocusManager.abandonAudioFocus()
    }

    private fun unmute() {
        VideoPlayerModule.audioFocusManager.requestAudioFocus()
        this.player?.volume = 1f
        this.isMuted = false
    }

    fun toggleMuted() {
        if (this.isMuted) {
            this.unmute()
        } else {
            this.mute()
        }
    }
    
    // Proximity mode methods
    
    private fun enterProximityMode() {
        if (!this.enableProximityAutoplay || this.isInProximityMode || this.isViewActive) {
            return
        }
        
        this.isInProximityMode = true
        
        // Add a small delay to reduce simultaneous video preparations
        this.postDelayed({
            if (this.isInProximityMode && this.player == null) {
                this.setup()
            }
        }, 100) // 100ms delay
    }
    
    private fun exitProximityMode() {
        if (!this.isInProximityMode) {
            return
        }
        
        this.isInProximityMode = false
        
        // Only pause if we're not becoming active (to avoid pause-then-play stuttering)
        if (!this.isViewActive) {
            this.pause()
        }
    }
    
    private fun configureAudioForProximity(shouldPlay: Boolean) {
        if (!this.isInProximityMode || this.isViewActive) {
            return
        }
        
        if (shouldPlay) {
            // Start playing muted when reaching threshold
            this.mute()
            this.play()
        } else {
            // Pause when below threshold but keep prepared
            this.pause()
        }
    }

    // Fullscreen handling

    fun enterFullscreen(keepDisplayOn: Boolean) {
        val currentActivity = this.appContext.currentActivity ?: return

        this.enteredFullscreenMuteState = this.isMuted

        // We always want to start with unmuted state and playing. Fire those from here so the
        // event dispatcher gets called
        this.unmute()
        if (!this.isPlaying) {
            this.play()
        }

        // Remove the player from this view, but don't null the player!
        this.playerView.player = null

        // create the intent and give it a view
        val intent = Intent(context, FullscreenActivity::class.java)
        intent.putExtra("keepDisplayOn", keepDisplayOn)
        FullscreenActivity.asscVideoView = WeakReference(this)

        // fire the fullscreen event and launch the intent
        this.isFullscreen = true
        currentActivity.startActivity(intent)
    }

    fun onExitFullscreen() {
        this.isFullscreen = false
        if (this.enteredFullscreenMuteState) {
            this.mute()
        }
        if (this.autoplay) {
            this.play()
        } else {
            this.pause()
        }
        this.playerView.player = this.player
    }

    // Picture-in-Picture methods
    fun enterPictureInPicture() {
        if (!enablePictureInPicture || isPictureInPicture) {
            return
        }

        val currentActivity = this.appContext.currentActivity ?: return

        // Store current state
        this.isPictureInPicture = true

        // Remove player from current view
        this.playerView.player = null

        // Create intent for PiP activity
        val intent = Intent(context, PictureInPictureActivity::class.java)
        PictureInPictureActivity.associatedVideoView = WeakReference(this)

        // Start PiP activity
        currentActivity.startActivity(intent)
    }

    fun exitPictureInPicture() {
        if (!isPictureInPicture) {
            return
        }

        this.isPictureInPicture = false
        this.playerView.player = this.player
    }

    fun onExitPictureInPicture() {
        this.isPictureInPicture = false
        this.playerView.player = this.player
    }

    // Visibility

    fun setIsCurrentlyActive(isActive: Boolean): Boolean {
        if (this.isFullscreen) {
            return false
        }

        this.isViewActive = isActive
        if (isActive) {
            if (this.autoplay || this.forceTakeover) {
                this.setup()
            }
            
            // Exit proximity mode when becoming active
            val wasInProximityMode = this.isInProximityMode
            if (this.isInProximityMode) {
                this.exitProximityMode()
            }
            
            // Ensure video is playing when becoming active, respecting beginMuted setting
            if (this.player != null) {
                if (!this.beginMuted) {
                    this.unmute()
                }
                // Only call play() if we weren't already playing from proximity mode
                if (!wasInProximityMode) {
                    this.play()
                }
            }
        } else if (this.isNearby) {
            this.pause()
        } else {
            this.destroy()
        }
        return true
    }
    
    fun setIsNearby(nearby: Boolean) {
        this.isNearby = nearby
        
        // Handle proximity-based autoplay
        if (this.enableProximityAutoplay && nearby && !this.isViewActive) {
            val visibilityPercentage = this.calculateVisibilityPercentage()
            if (visibilityPercentage > 0) {
                // Start preparing immediately when video comes into view
                this.enterProximityMode()
                
                // Configure audio based on threshold
                if (visibilityPercentage >= this.proximityThreshold) {
                    this.configureAudioForProximity(shouldPlay = true)
                } else {
                    this.configureAudioForProximity(shouldPlay = false)
                }
            } else {
                this.exitProximityMode()
            }
        } else if (!nearby && this.isInProximityMode) {
            this.exitProximityMode()
        }
    }

    // Visibility

    fun getPositionOnScreen(): Rect? {
        if (!this.isShown) {
            return null
        }

        val screenPosition = intArrayOf(0, 0)
        this.getLocationInWindow(screenPosition)
        
        // Get the actual screen bounds
        val displayMetrics = this.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        
        // Calculate the intersection with the screen
        val viewRect = Rect(
            screenPosition[0],
            screenPosition[1],
            screenPosition[0] + this.width,
            screenPosition[1] + this.height,
        )
        
        val screenRect = Rect(0, 0, screenWidth, screenHeight)
        
        // Return the intersection of view and screen
        val intersection = Rect()
        if (intersection.setIntersect(viewRect, screenRect)) {
            return intersection
        }
        
        return null
    }

    fun isViewableEnough(): Boolean {
        val positionOnScreen = this.getPositionOnScreen() ?: return false
        val visibleArea = positionOnScreen.width() * positionOnScreen.height()
        val totalArea = this.width * this.height
        return visibleArea >= 0.5 * totalArea
    }

    // Setup helpers

    private suspend fun createMediaItem(): MediaItem =
        withContext(Dispatchers.IO) {
            MediaItem
                .Builder()
                .setUri(url.toString())
                .build()
        }

    private fun createExoPlayer(): ExoPlayer =
        ExoPlayer
            .Builder(context)
            .apply {
                setLooper(context.mainLooper)
                setSeekForwardIncrementMs(5000)
                setSeekBackIncrementMs(5000)
                
                // Adaptive streaming for HLS
                val trackSelector = DefaultTrackSelector(context).apply {
                    setParameters(
                        buildUponParameters()
                            .setMaxVideoSizeSd() // Start with SD, adapt up
                            .setAllowVideoMixedMimeTypeAdaptiveness(true)
                            .setAllowAudioMixedMimeTypeAdaptiveness(true)
                    )
                }
                setTrackSelector(trackSelector)
                
                // Performance optimizations - reasonable buffer settings
                setLoadControl(
                    DefaultLoadControl.Builder()
                        .setBufferDurationsMs(
                            5000, // Min buffer duration (5s)
                            15000, // Max buffer duration (15s)
                            2500, // Buffer for playback (2.5s)
                            5000 // Buffer for playback after rebuffer (5s)
                        )
                        .build()
                )
            }.build()
            .apply {
                repeatMode = ExoPlayer.REPEAT_MODE_ALL
                playWhenReady = false
                volume = 0f
                addListener(
                    object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            super.onPlaybackStateChanged(playbackState)
                            when (playbackState) {
                                ExoPlayer.STATE_READY -> {
                                    val view = this@VideoPlayerView
                                    view.isLoading = false
                                    
                                    // Apply beginMuted setting regardless of autoplay
                                    if (view.beginMuted) {
                                        view.mute()
                                    } else {
                                        view.unmute()
                                    }
                                    
                                    if (view.autoplay || view.ignoreAutoplay) {
                                        view.play()
                                    }
                                }
                            }
                        }
                    },
                )
            }

    private fun addProgressTracker() {
        val player = this.playerView.player ?: return
        this.progressTracker = ProgressTracker(player, onTimeRemainingChange)
    }

    private fun removeProgressTracker() {
        this.progressTracker?.remove()
        this.progressTracker = null
    }

    // Layout Hack
    // Code is borrowed from expo-video, which borrows it from react native...
    // https://github.com/expo/expo/blob/f81c18237c3cd5f0aa2b4db31fdf5b865281cb71/packages/expo-video/android/src/main/java/expo/modules/video/VideoView.kt#L125
    // https://github.com/facebook/react-native/blob/d19afc73f5048f81656d0b4424232ce6d69a6368/ReactAndroid/src/main/java/com/facebook/react/views/toolbar/ReactToolbar.java#L166
    //
    // Essentially, the resize mode does not work without this hack, and will sometimes simply show
    // the video in a poorly scaled way.
    private val mLayoutRunnable =
        Runnable {
            measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY),
            )
            layout(left, top, right, bottom)
        }

    override fun requestLayout() {
        super.requestLayout()
        post(mLayoutRunnable)
    }
}
