package expo.modules.videoplayer

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import java.lang.ref.WeakReference

@UnstableApi
class PictureInPictureActivity : AppCompatActivity() {
    companion object {
        var associatedVideoView: WeakReference<VideoPlayerView>? = null
        private const val ASPECT_RATIO_WIDTH = 16
        private const val ASPECT_RATIO_HEIGHT = 9
    }

    private var playerView: PlayerView? = null
    private var isInPictureInPictureMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoView = associatedVideoView?.get()
        val player = videoView?.player

        if (player == null) {
            finish()
            return
        }

        // Create player view
        playerView = PlayerView(this).apply {
            setBackgroundColor(Color.BLACK)
            useController = false
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        playerView?.player = player
        setContentView(playerView)

        // Enter PiP mode immediately
        enterPictureInPictureMode()
    }

    private fun enterPictureInPictureMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(Rational(ASPECT_RATIO_WIDTH, ASPECT_RATIO_HEIGHT))
                .build()
            
            enterPictureInPictureMode(pipParams)
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        this.isInPictureInPictureMode = isInPictureInPictureMode
        
        if (isInPictureInPictureMode) {
            // Hide system UI
            window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        } else {
            // Show system UI
            window.decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            associatedVideoView?.get()?.onExitPictureInPicture()
        }
        super.onDestroy()
    }
}
