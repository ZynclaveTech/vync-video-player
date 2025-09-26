import ExpoModulesCore
import AVKit

public class VideoPlayerModule: Module {
  private var wasPlayingPlayer: AVPlayer?

  public func definition() -> ModuleDefinition {
    Name("VideoPlayer")

    OnCreate {
      AudioManagement.shared.setPlayingVideo(false)
    }

    OnAppEntersForeground {
      self.wasPlayingPlayer?.play()
      self.wasPlayingPlayer = nil
    }

    OnAppEntersBackground {
      PlayerManager.shared.allPlayers().forEach { player in
        if player.isPlaying {
          player.pause()
          self.wasPlayingPlayer = player
          return
        }
      }
    }

    AsyncFunction("updateActiveVideoViewAsync") {
      ViewManager.shared.updateActiveView()
    }

    View(VideoView.self) {
      Events([
        "onActiveChange",
        "onLoadingChange",
        "onMutedChange",
        "onStatusChange",
        "onTimeRemainingChange",
        "onFullscreenChange",
        "onError",
        "onPictureInPictureChange"
      ])

      Prop("url") { (view: VideoView, prop: URL) in
        view.url = prop
      }

      Prop("autoplay") { (view: VideoView, prop: Bool) in
        view.autoplay = prop
      }

      Prop("beginMuted") { (view: VideoView, prop: Bool) in
        view.beginMuted = prop
      }

      Prop("forceTakeover") { (view: VideoView, prop: Bool) in
        view.forceTakeover = prop
      }

      // Proximity-based autoplay props
      Prop("enableProximityAutoplay") { (view: VideoView, prop: Bool) in
        view.enableProximityAutoplay = prop
      }

      Prop("proximityThreshold") { (view: VideoView, prop: Double) in
        view.proximityThreshold = CGFloat(prop)
      }

      // Thumbnail support
      Prop("thumbnailUrl") { (view: VideoView, prop: String?) in
        view.thumbnailUrl = prop
      }

      Prop("showThumbnailWhileLoading") { (view: VideoView, prop: Bool) in
        view.showThumbnailWhileLoading = prop
      }

      Prop("showThumbnailWhenInactive") { (view: VideoView, prop: Bool) in
        view.showThumbnailWhenInactive = prop
      }

      // Picture-in-Picture props
      Prop("enablePictureInPicture") { (view: VideoView, prop: Bool) in
        view.enablePictureInPicture = prop
      }

      Prop("pipDimensions") { (view: VideoView, prop: [String: Double]) in
        view.pipDimensions = prop
      }

      AsyncFunction("togglePlayback") { (view: VideoView) in
        view.togglePlayback()
      }

      AsyncFunction("toggleMuted") { (view: VideoView) in
        view.toggleMuted()
      }

      AsyncFunction("enterFullscreen") { (view: VideoView, keepDisplayOn: Bool) in
        view.enterFullscreen(keepDisplayOn: keepDisplayOn)
      }

      // Picture-in-Picture functions
      AsyncFunction("enterPictureInPicture") { (view: VideoView) in
        view.enterPictureInPicture()
      }

      AsyncFunction("exitPictureInPicture") { (view: VideoView) in
        view.exitPictureInPicture()
      }
    }
  }
}
