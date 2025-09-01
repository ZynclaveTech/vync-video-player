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

                AsyncFunction("togglePlayback") { view: VideoPlayerView ->
                    view.togglePlayback()
                }

                AsyncFunction("toggleMuted") { view: VideoPlayerView ->
                    view.toggleMuted()
                }

                AsyncFunction("enterFullscreen") { view: VideoPlayerView, keepDisplayOn: Boolean ->
                    view.enterFullscreen(keepDisplayOn)
                }
            }
        }
}
