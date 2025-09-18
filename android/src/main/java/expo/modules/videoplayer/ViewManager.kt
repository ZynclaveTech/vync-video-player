package expo.modules.videoplayer

import android.graphics.Rect
import android.view.View
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class ViewManager {
    companion object {
        private val views = mutableSetOf<VideoPlayerView>()
        private var currentlyActiveView: VideoPlayerView? = null

        fun addView(view: VideoPlayerView) {
            views.add(view)
        }

        fun removeView(view: VideoPlayerView) {
            views.remove(view)
            if (currentlyActiveView == view) {
                currentlyActiveView = null
            }
        }

        fun updateActiveView() {
            var activeView: VideoPlayerView? = null
            var maxVisibility = 0.0

            for (view in views) {
                if (view.isViewableEnough()) {
                    val visibility = view.calculateVisibilityPercentage()
                    if (visibility > maxVisibility) {
                        maxVisibility = visibility
                        activeView = view
                    }
                }
            }

            if (activeView != currentlyActiveView) {
                // Update nearby status for all views before changing active view
                updateNearbyStatus(activeView)
                
                currentlyActiveView?.setIsCurrentlyActive(false)
                activeView?.setIsCurrentlyActive(true)
                currentlyActiveView = activeView
            }
        }
        
        private fun updateNearbyStatus(activeView: VideoPlayerView?) {
            for (view in views) {
                val isNearby = isViewNearby(view, activeView)
                view.setIsNearby(isNearby)
            }
        }
        
        private fun isViewNearby(view: VideoPlayerView, activeView: VideoPlayerView?): Boolean {
            if (activeView == null) return false
            
            val activePosition = activeView.getPositionOnScreen() ?: return false
            val viewPosition = view.getPositionOnScreen() ?: return false
            
            // Consider a view "nearby" if it's within 2 screen heights of the active view
            val screenHeight = view.context.resources.displayMetrics.heightPixels
            val distanceThreshold = screenHeight * 2
            
            val verticalDistance = kotlin.math.abs(activePosition.centerY() - viewPosition.centerY())
            return verticalDistance <= distanceThreshold
        }

        fun setActiveView(view: VideoPlayerView) {
            if (currentlyActiveView != view) {
                currentlyActiveView?.setIsCurrentlyActive(false)
                view.setIsCurrentlyActive(true)
                currentlyActiveView = view
            }
        }

        fun getActiveView(): VideoPlayerView? = currentlyActiveView
    }
}

// Extension functions for VideoPlayerView
fun VideoPlayerView.calculateVisibilityPercentage(): Double {
    val positionOnScreen = this.getPositionOnScreen() ?: return 0.0
    val visibleArea = positionOnScreen.width() * positionOnScreen.height()
    val totalArea = this.width * this.height
    return if (totalArea > 0) visibleArea.toDouble() / totalArea.toDouble() else 0.0
}

fun VideoPlayerView.isViewableEnough(): Boolean {
    return calculateVisibilityPercentage() >= 0.3 // Reduced from 0.5 to 0.3 (30% visibility)
}
