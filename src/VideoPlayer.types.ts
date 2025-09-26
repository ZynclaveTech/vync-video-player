import {NativeSyntheticEvent, StyleProp, ViewStyle, ImageStyle} from 'react-native'

export type VideoPlayerViewProps = {
  url: string
  autoplay: boolean
  beginMuted: boolean
  forceTakeover?: boolean
  accessibilityHint?: string
  accessibilityLabel?: string
  
  // Proximity-based autoplay
  enableProximityAutoplay?: boolean
  proximityThreshold?: number
  
  // Thumbnail support
  thumbnailUrl?: string
  showThumbnailWhileLoading?: boolean
  showThumbnailWhenInactive?: boolean
  thumbnailStyle?: StyleProp<ImageStyle>

  // Picture-in-Picture support
  enablePictureInPicture?: boolean
  pipDimensions?: {
    width: number
    height: number
  }
  onPictureInPictureChange?: (
    e: NativeSyntheticEvent<{isPictureInPicture: boolean}>
  ) => void

  onActiveChange?: (e: NativeSyntheticEvent<{isActive: boolean}>) => void
  onLoadingChange?: (e: NativeSyntheticEvent<{isLoading: boolean}>) => void
  onMutedChange?: (e: NativeSyntheticEvent<{isMuted: boolean}>) => void
  onPlayerPress?: () => void
  onStatusChange?: (
    e: NativeSyntheticEvent<{status: 'playing' | 'paused'}>
  ) => void
  onTimeRemainingChange?: (
    e: NativeSyntheticEvent<{timeRemaining: number}>
  ) => void
  onFullscreenChange?: (
    e: NativeSyntheticEvent<{isFullscreen: boolean}>
  ) => void
  onError?: (e: NativeSyntheticEvent<{error: string}>) => void
  style?: StyleProp<ViewStyle>
}
