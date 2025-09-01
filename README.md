# Vync Video Player

[![npm version](https://badge.fury.io/js/vync-video-player.svg)](https://badge.fury.io/js/vync-video-player)
[![npm downloads](https://img.shields.io/npm/dm/vync-video-player.svg)](https://www.npmjs.com/package/vync-video-player)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform: React Native](https://img.shields.io/badge/Platform-React%20Native-blue.svg)](https://reactnative.dev/)
[![Platform: Expo](https://img.shields.io/badge/Platform-Expo-9C27B0.svg)](https://expo.dev/)

> **Professional-grade video player library for React Native and Expo applications with native performance, smart autoplay, and comprehensive video format support.**

## Overview

Vync Video Player is a high-performance, cross-platform video player library designed specifically for React Native and Expo applications. Built with native iOS and Android implementations, it provides smooth video playback, intelligent memory management, and an intuitive API for developers.

## Key Features

### **Performance & Optimization**
- **Native Performance**: Built with native iOS (AVPlayer) and Android (ExoPlayer) for optimal performance
- **Smart Memory Management**: Efficient view recycling and automatic cleanup to prevent memory leaks
- **Lazy Loading**: Videos only load when visible, reducing bandwidth and improving app performance
- **Optimized Rendering**: Hardware-accelerated video rendering for smooth playback

### **Cross-Platform Support**
- **iOS Support**: Full native iOS integration with AVPlayer framework
- **Android Support**: Native Android implementation using ExoPlayer
- **Expo Compatible**: Works seamlessly with Expo managed and bare workflows
- **React Native**: Compatible with all React Native versions 0.60+

### **Advanced Playback Features**
- **Smart Autoplay**: Visibility-based autoplay that only plays videos when they're visible
- **Fullscreen Experience**: Native fullscreen mode with proper orientation handling
- **Audio Focus Management**: Intelligent audio handling that respects system audio policies
- **Seamless Looping**: Built-in video looping with smooth transitions
- **Progress Tracking**: Real-time playback progress and time remaining updates

### **Developer Experience**
- **TypeScript Support**: Full TypeScript definitions for better development experience
- **Event-Driven Architecture**: Comprehensive event system for all player state changes
- **Flexible Styling**: Customizable video player appearance and behavior
- **Error Handling**: Robust error handling with detailed error information
- **Accessibility**: Built-in accessibility support for screen readers

## Installation

### Prerequisites
- React Native 0.60+ or Expo SDK 40+
- iOS 12.0+ / Android API 21+
- Node.js 14+ and npm/yarn

### Install Package
```bash
# Using npm
npm install vync-video-player

# Using yarn
yarn add vync-video-player

# Using pnpm
pnpm add vync-video-player
```

## Usage Examples

### Basic Video Player
```tsx
import React from 'react';
import { VideoPlayerView } from 'vync-video-player';

function BasicVideoPlayer() {
  return (
    <VideoPlayerView
      url="https://example.com/video.mp4"
      autoplay={true}
      beginMuted={true}
      style={{ width: '100%', height: 300 }}
    />
  );
}
```

### Advanced Video Player with Event Handling
```tsx
import React, { useRef, useState } from 'react';
import { VideoPlayerView } from 'vync-video-player';
import { View, Text, Button } from 'react-native';

function AdvancedVideoPlayer() {
  const videoRef = useRef(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [isMuted, setIsMuted] = useState(true);
  const [timeRemaining, setTimeRemaining] = useState(0);

  const handlePlayPause = () => {
    videoRef.current?.togglePlayback();
  };

  const handleMuteToggle = () => {
    videoRef.current?.toggleMuted();
  };

  const handleFullscreen = () => {
    videoRef.current?.enterFullscreen(true);
  };

  return (
    <View style={{ flex: 1 }}>
      <VideoPlayerView
        ref={videoRef}
        url="https://example.com/video.mp4"
        autoplay={false}
        beginMuted={true}
        style={{ width: '100%', height: 300 }}
        onStatusChange={(e) => setIsPlaying(e.nativeEvent.status === 'playing')}
        onMutedChange={(e) => setIsMuted(e.nativeEvent.isMuted)}
        onTimeRemainingChange={(e) => setTimeRemaining(e.nativeEvent.timeRemaining)}
        onError={(e) => console.error('Video error:', e.nativeEvent.error)}
      />
      
      <View style={{ padding: 20 }}>
        <Text>Status: {isPlaying ? 'Playing' : 'Paused'}</Text>
        <Text>Muted: {isMuted ? 'Yes' : 'No'}</Text>
        <Text>Time Remaining: {Math.round(timeRemaining)}s</Text>
        
        <Button title={isPlaying ? 'Pause' : 'Play'} onPress={handlePlayPause} />
        <Button title={isMuted ? 'Unmute' : 'Mute'} onPress={handleMuteToggle} />
        <Button title="Fullscreen" onPress={handleFullscreen} />
      </View>
    </View>
  );
}
```

### Feed-Style Video Player (Social Media Apps)
```tsx
import React from 'react';
import { FlatList, View } from 'react-native';
import { VideoPlayerView } from 'vync-video-player';

const videoData = [
  { id: '1', url: 'https://example.com/video1.mp4' },
  { id: '2', url: 'https://example.com/video2.mp4' },
  { id: '3', url: 'https://example.com/video3.mp4' },
];

function FeedVideoPlayer() {
  const renderVideo = ({ item }) => (
    <View style={{ height: 400, marginBottom: 20 }}>
      <VideoPlayerView
        url={item.url}
        autoplay={true}
        beginMuted={true}
        forceTakeover={false}
        style={{ flex: 1 }}
      />
    </View>
  );

  return (
    <FlatList
      data={videoData}
      renderItem={renderVideo}
      keyExtractor={(item) => item.id}
      showsVerticalScrollIndicator={false}
    />
  );
}
```

### Video Player with Thumbnails
```tsx
import React from 'react';
import { VideoPlayerView } from 'vync-video-player';

function VideoPlayerWithThumbnails() {
  return (
    <VideoPlayerView
      url="https://example.com/video.mp4"
      autoplay={true}
      beginMuted={true}
      // Thumbnail configuration
      thumbnailUrl="https://example.com/thumbnail.jpg"
      showThumbnailWhileLoading={true}
      showThumbnailWhenInactive={true}
      thumbnailStyle={{
        borderRadius: 8,
        opacity: 0.9
      }}
      style={{ width: '100%', height: 300 }}
    />
  );
}
```

## API Reference

### Props

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `url` | string | - | ✅ | Video URL to play (supports HTTP/HTTPS, local files) |
| `autoplay` | boolean | false | ❌ | Whether to autoplay when video becomes visible |
| `beginMuted` | boolean | true | ❌ | Whether to start with muted audio |
| `forceTakeover` | boolean | false | ❌ | Force this player to be active (useful for single video apps) |
| `style` | ViewStyle | - | ❌ | Custom styling for the video player container |
| `accessibilityLabel` | string | - | ❌ | Accessibility label for screen readers |
| `accessibilityHint` | string | - | ❌ | Accessibility hint for screen readers |

#### Thumbnail Support

| Prop | Type | Default | Required | Description |
|------|------|---------|----------|-------------|
| `thumbnailUrl` | string | - | ❌ | URL of the thumbnail image to display |
| `showThumbnailWhileLoading` | boolean | false | ❌ | Show thumbnail while video is loading |
| `showThumbnailWhenInactive` | boolean | false | ❌ | Show thumbnail when video view is not active/visible |
| `thumbnailStyle` | ImageStyle | - | ❌ | Custom styling for the thumbnail image |

### Events

| Event | Type | Description |
|-------|------|-------------|
| `onActiveChange` | `{isActive: boolean}` | Fired when player becomes active/inactive based on visibility |
| `onLoadingChange` | `{isLoading: boolean}` | Fired when video loading state changes |
| `onMutedChange` | `{isMuted: boolean}` | Fired when audio mute state changes |
| `onStatusChange` | `{status: 'playing' \| 'paused'}` | Fired when play/pause state changes |
| `onTimeRemainingChange` | `{timeRemaining: number}` | Fired every second with remaining playback time |
| `onFullscreenChange` | `{isFullscreen: boolean}` | Fired when entering/exiting fullscreen mode |
| `onPlayerPress` | `void` | Fired when user taps on the video player |
| `onError` | `{error: string}` | Fired when an error occurs during playback |

### Methods

| Method | Parameters | Description |
|--------|------------|-------------|
| `togglePlayback()` | - | Toggle between play and pause states |
| `toggleMuted()` | - | Toggle between muted and unmuted audio |
| `enterFullscreen(keepDisplayOn?)` | `keepDisplayOn?: boolean` | Enter fullscreen mode with optional display wake lock |

## Supported Video Formats

### iOS (AVPlayer)
- **Video Codecs**: H.264, H.265 (HEVC), MPEG-4
- **Audio Codecs**: AAC, MP3, ALAC
- **Container Formats**: MP4, MOV, M4V, 3GP
- **Streaming**: HLS (.m3u8), DASH
- **Resolution**: Up to 4K (device dependent)

### Android (ExoPlayer)
- **Video Codecs**: H.264, H.265, VP8, VP9, AV1
- **Audio Codecs**: AAC, MP3, Opus, Vorbis
- **Container Formats**: MP4, WebM, MKV, AVI, FLV
- **Streaming**: HLS, DASH, SmoothStreaming
- **Resolution**: Up to 8K (device dependent)

## Performance Features

### Memory Management
- **View Recycling**: Efficient reuse of video player instances
- **Automatic Cleanup**: Videos are automatically destroyed when not visible
- **Background Pause**: Videos pause when app goes to background
- **Resource Optimization**: Minimal memory footprint per video instance

### Smart Autoplay
- **Visibility Detection**: Only plays videos when 50%+ visible on screen
- **Scroll Optimization**: Efficient handling of scroll-based video feeds
- **Battery Optimization**: Respects system power management
- **Network Awareness**: Adapts to network conditions

## Security & Privacy

- **No Data Collection**: Library doesn't collect user data or analytics
- **Local Processing**: All video processing happens locally on device
- **Secure URLs**: Supports HTTPS and secure video sources
- **Privacy First**: No tracking or external service dependencies

## Use Cases

### Social Media Applications
- **Video Feeds**: Instagram, TikTok-style video scrolling
- **Story Features**: Snapchat-style vertical video stories
- **Live Streaming**: Real-time video broadcasting
- **User-Generated Content**: Community video sharing

### E-Learning Platforms
- **Course Videos**: Educational content delivery
- **Interactive Lessons**: Video-based learning modules
- **Mobile Learning**: On-the-go educational content
- **Offline Support**: Local video playback

### Entertainment Apps
- **Video Streaming**: Netflix-style video applications
- **Gaming Content**: Gameplay video sharing
- **Music Videos**: Audio-visual content delivery
- **Podcast Videos**: Video podcast applications

### Business Applications
- **Product Demos**: E-commerce product videos
- **Training Videos**: Employee onboarding content
- **Marketing Content**: Promotional video delivery
- **Corporate Communications**: Internal video messaging

## Benchmarks

- **Startup Time**: < 100ms for video initialization
- **Memory Usage**: < 50MB per video instance
- **Battery Impact**: Minimal battery drain during playback
- **Network Efficiency**: Smart buffering with 5-second forward buffer
- **Frame Rate**: Consistent 60fps playback on supported devices

## Development

### Building from Source
```bash
# Clone repository
git clone https://github.com/ZynclaveTech/vync-video-player.git
cd vync-video-player

# Install dependencies
yarn install

# Build package
yarn build

# Run tests
yarn test

# Run example app
cd example
yarn start
```

### Contributing
We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

### Documentation
- [Examples](https://github.com/ZynclaveTech/vync-video-player#usage-examples)
- [Troubleshooting](https://github.com/ZynclaveTech/vync-video-player/issues)

### Community
- **GitHub Issues**: [Report bugs or request features](https://github.com/ZynclaveTech/vync-video-player/issues)
- **Discussions**: [Join community discussions](https://github.com/ZynclaveTech/vync-video-player/discussions)
- **Stack Overflow**: Tag questions with `vync-video-player`

### Enterprise Support
For enterprise support, custom implementations, or consulting services, contact us at [support@zynclave.tech](mailto:info@zynclave.com)

## Acknowledgments

- Built with ❤️ by the Zynclave Tech team
- Inspired by modern video streaming platforms
- Powered by native iOS and Android video frameworks
- Community-driven development and feedback

---

**Made with ❤️ by [Zynclave Tech](https://vync.live)**

*Transform your React Native apps with professional-grade video playback capabilities.*
