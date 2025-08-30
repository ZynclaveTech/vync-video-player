# Video Player

[![npm version](https://badge.fury.io/js/vync-video-player.svg)](https://badge.fury.io/js/vync-video-player)
[![npm downloads](https://img.shields.io/npm/dm/vync-video-player.svg)](https://www.npmjs.com/package/vync-video-player)

A cross-platform video player library for React Native and Expo applications.

## Features

- **Cross-Platform**: Native iOS and Android support
- **Performance Optimized**: Efficient memory management and view recycling
- **Auto-play Management**: Smart visibility-based autoplay
- **Fullscreen Support**: Native fullscreen experience
- **Audio Focus Management**: Proper audio handling on mobile devices
- **Event-Driven**: Comprehensive event system for player state changes

## Installation

```bash
npm install vync-video-player
# or
yarn add vync-video-player
```

## Usage

```tsx
import { VideoPlayerView } from 'vync-video-player'

function MyComponent() {
  return (
    <VideoPlayerView
      url="https://example.com/video.mp4"
      autoplay={true}
      beginMuted={true}
      onStatusChange={(e) => console.log('Status:', e.nativeEvent.status)}
      onError={(e) => console.log('Error:', e.nativeEvent.error)}
    />
  )
}
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `url` | string | - | Video URL to play |
| `autoplay` | boolean | false | Whether to autoplay when visible |
| `beginMuted` | boolean | true | Whether to start muted |
| `forceTakeover` | boolean | false | Force this player to be active |
| `style` | ViewStyle | - | Custom styling |

## Events

| Event | Description |
|-------|-------------|
| `onActiveChange` | Fired when player becomes active/inactive |
| `onLoadingChange` | Fired when loading state changes |
| `onMutedChange` | Fired when mute state changes |
| `onStatusChange` | Fired when play/pause state changes |
| `onTimeRemainingChange` | Fired every second with remaining time |
| `onFullscreenChange` | Fired when entering/exiting fullscreen |
| `onError` | Fired when an error occurs |

## Methods

| Method | Description |
|--------|-------------|
| `togglePlayback()` | Toggle between play and pause |
| `toggleMuted()` | Toggle between muted and unmuted |
| `enterFullscreen(keepDisplayOn?)` | Enter fullscreen mode |

## Supported Formats

- **iOS**: H.264, H.265, AAC, MP4, MOV, M4V
- **Android**: H.264, H.265, VP9, AAC, MP4, WebM

## Requirements

- React Native 0.60+
- Expo SDK 40+
- iOS 12.0+
- Android API 21+

## License

MIT
