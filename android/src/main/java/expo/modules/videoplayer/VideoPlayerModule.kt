package expo.modules.videoplayer

import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

@UnstableApi
class VideoPlayerModule : Module() {
    companion object {
        lateinit var audioFocusManager: AudioFocusManager
    }

    private var wasPlayingPlayer: Player? = null

    override fun definition() =
        ModuleDefinition {
            Name("VideoPlayer")

            OnCreate {
                appContext.reactContext?.let { context ->
                    audioFocusManager = AudioFocusManager(context)
                }
            }

            OnActivityEntersForeground {
                wasPlayingPlayer?.play()
                wasPlayingPlayer = null
            }

            OnActivityEntersBackground {
                ViewManager.getActiveView()?.let { view ->
                    view.player?.let { player ->
                        if (player.isPlaying && !view.isFullscreen) {
                            view.mute()
                            player.pause()
                            wasPlayingPlayer = player
                        }
                    }
                }
            }

            AsyncFunction("updateActiveVideoViewAsync") {
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    ViewManager.updateActiveView()
                }
            }

            View(VideoPlayerView::class) {
                Events(
                    "onActiveChange",
                    "onLoadingChange",
                    "onMutedChange",
                    "onPlayerPress",
                    "onStatusChange",
                    "onTimeRemainingChange",
                    "onFullscreenChange",
                    "onError",
                    "onPictureInPictureChange",
                )

                Prop("url") { view: VideoPlayerView, prop: Uri ->
                    view.url = prop
                }

                Prop("autoplay") { view: VideoPlayerView, prop: Boolean ->
                    view.autoplay = prop
                }

                Prop("beginMuted") { view: VideoPlayerView, prop: Boolean ->
                    view.beginMuted = prop
                }

                Prop("forceTakeover") { view: VideoPlayerView, prop: Boolean ->
                    view.forceTakeover = prop
                }

                // Proximity-based autoplay props
                Prop("enableProximityAutoplay") { view: VideoPlayerView, prop: Boolean ->
                    view.enableProximityAutoplay = prop
                }

                Prop("proximityThreshold") { view: VideoPlayerView, prop: Double ->
                    view.proximityThreshold = prop
                }

                // Thumbnail support
                Prop("thumbnailUrl") { view: VideoPlayerView, prop: String? ->
                    view.thumbnailUrl = prop
                }

                Prop("showThumbnailWhileLoading") { view: VideoPlayerView, prop: Boolean ->
                    view.showThumbnailWhileLoading = prop
                }

                Prop("showThumbnailWhenInactive") { view: VideoPlayerView, prop: Boolean ->
                    view.showThumbnailWhenInactive = prop
                }

                // Picture-in-Picture props
                Prop("enablePictureInPicture") { view: VideoPlayerView, prop: Boolean ->
                    view.enablePictureInPicture = prop
                }

                Prop("pipDimensions") { view: VideoPlayerView, prop: Map<String, Double> ->
                    view.pipDimensions = prop
                }

                AsyncFunction("togglePlayback") { view: VideoPlayerView ->
                    view.togglePlayback()
                }

                AsyncFunction("toggleMuted") { view: VideoPlayerView ->
                    view.toggleMuted()
                }

                AsyncFunction("enterFullscreen") { view: VideoPlayerView, keepDisplayOn: Boolean ->
                    view.enterFullscreen(keepDisplayOn)
                }

                // Picture-in-Picture functions
                AsyncFunction("enterPictureInPicture") { view: VideoPlayerView ->
                    view.enterPictureInPicture()
                }

                AsyncFunction("exitPictureInPicture") { view: VideoPlayerView ->
                    view.exitPictureInPicture()
                }
            }
        }
}
