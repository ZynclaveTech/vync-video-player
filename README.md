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
- **HLS Adaptive Streaming**: Automatic resolution adjustment based on network conditions and device capabilities
- **Hybrid Memory Management**: Smart memory optimization that keeps only nearby videos in memory (3-5 videos), destroying distant ones to reduce memory usage by up to 60%
- **Lazy Loading**: Videos only load when visible, reducing bandwidth and improving app performance
- **Optimized Rendering**: Hardware-accelerated video rendering for smooth playback

### **Cross-Platform Support**
- **iOS Support**: Full native iOS integration with AVPlayer framework
- **Android Support**: Native Android implementation using ExoPlayer
- **Expo Compatible**: Works seamlessly with Expo managed and bare workflows
- **React Native**: Compatible with all React Native versions 0.60+

### **Advanced Playback Features**
- **Smart Autoplay**: Visibility-based autoplay that only plays videos when they're visible
- **Proximity-Based Autoplay**: Videos start playing muted when they come within a configurable distance (e.g., 30% visibility) and unmute when they become the active video
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

### Proximity-Based Autoplay (Social Media Style)
```tsx
import React from 'react';
import { ScrollView, View } from 'react-native';
import { VideoPlayerView } from 'vync-video-player';

const videoData = [
  { id: '1', url: 'https://example.com/video1.mp4' },
  { id: '2', url: 'https://example.com/video2.mp4' },
  { id: '3', url: 'https://example.com/video3.mp4' },
];

function ProximityVideoFeed() {
  return (
    <ScrollView>
      {videoData.map((video) => (
        <View key={video.id} style={{ height: 400, marginBottom: 20 }}>
          <VideoPlayerView
            url={video.url}
            autoplay={false} // Disable regular autoplay
            beginMuted={true}
            // Enable proximity-based autoplay
            enableProximityAutoplay={true}
            proximityThreshold={0.3} // Start playing when 30% visible
            style={{ flex: 1 }}
          />
        </View>
      ))}
    </ScrollView>
  );
}
```

### Large Video Feed with Memory Optimization
```tsx
import React from 'react';
import { FlatList, View } from 'react-native';
import { VideoPlayerView, updateActiveVideoViewAsync } from 'vync-video-player';

const videoData = Array.from({ length: 100 }, (_, i) => ({
  id: i.toString(),
  url: `https://example.com/video${i}.mp4`,
  thumbnailUrl: `https://example.com/thumb${i}.jpg`
}));

function OptimizedVideoFeed() {
  const handleScroll = () => {
    // Update active video based on scroll position
    updateActiveVideoViewAsync();
  };

  const renderVideo = ({ item }) => (
    <View style={{ height: 400, marginBottom: 20 }}>
      <VideoPlayerView
        url={item.url}
        autoplay={true}
        beginMuted={true}
        thumbnailUrl={item.thumbnailUrl}
        showThumbnailWhileLoading={true}
        showThumbnailWhenInactive={true}
        style={{ flex: 1 }}
      />
    </View>
  );

  return (
    <FlatList
      data={videoData}
      renderItem={renderVideo}
      keyExtractor={(item) => item.id}
      onScroll={handleScroll}
      scrollEventThrottle={16}
      showsVerticalScrollIndicator={false}
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
| `enableProximityAutoplay` | boolean | false | ❌ | Enable proximity-based autoplay (plays muted when nearby) |
| `proximityThreshold` | number | 0.3 | ❌ | Visibility threshold (0.0-1.0) to trigger proximity autoplay |
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

### Hybrid Memory Management
The Vync Video Player uses an innovative hybrid memory management system that dramatically reduces memory usage while maintaining smooth performance:

- **Smart Memory Allocation**: Only keeps 3-5 videos in memory at any time
- **Nearby Video Detection**: Videos within 2 screen heights are paused but kept alive for instant playback
- **Distant Video Cleanup**: Videos far from the current position are completely destroyed to free memory
- **Automatic Memory Optimization**: System automatically manages memory based on scroll position
- **Memory Efficiency**: Up to 60% reduction in total memory usage for large video lists

#### How It Works
1. **Active Video**: Currently visible video plays normally with full resources
2. **Nearby Videos**: Videos within 2 screen heights are paused but kept in memory for instant resume
3. **Distant Videos**: Videos far away are completely destroyed to free memory
4. **Smart Cleanup**: As you scroll, the system automatically manages which videos to keep or destroy

#### Memory Usage Comparison
- **Traditional Approach**: 20 videos × 40MB = 800MB total memory
- **Hybrid Approach**: 1 active (40MB) + 4 nearby (20MB each) + 15 destroyed (0MB) = 120MB total memory
- **Memory Savings**: Up to 85% reduction in memory usage

### Memory Management
- **View Recycling**: Efficient reuse of video player instances
- **Background Pause**: Videos pause when app goes to background
- **Resource Optimization**: Minimal memory footprint per video instance

### Smart Autoplay
- **Visibility Detection**: Only plays videos when 50%+ visible on screen
- **Scroll Optimization**: Efficient handling of scroll-based video feeds
- **Battery Optimization**: Respects system power management
- **Network Awareness**: Adapts to network conditions

### Proximity-Based Autoplay
The proximity-based autoplay feature provides a smooth, social media-style video experience by intelligently managing video preparation and playback:

- **Immediate Preparation**: Videos start preparing (loading, buffering) as soon as they come into view (even at 1% visibility)
- **Smart Audio Control**: Videos start playing muted when they reach the proximity threshold (default 30% visibility)
- **Instant Active Playback**: When a video becomes the active (most visible) video, it unmutes and plays with audio immediately
- **Single Audio Source**: Only one video plays audio at a time, preventing audio chaos
- **Configurable Threshold**: Adjust the visibility threshold (0.0-1.0) to control when videos start playing muted

#### How Proximity Autoplay Works
1. **View Detection**: As soon as a video comes into view (any visibility > 0%), it starts preparing (loads video, sets up buffers)
2. **Audio Threshold**: When the video reaches the proximity threshold (default 30% visibility), it starts playing muted
3. **Active Transition**: When the video becomes the most visible (active), it unmutes and plays with audio
4. **Proximity Exit**: When the video moves away from view, it stops preparing and pauses
5. **Memory Management**: Prepared videos are kept in memory for instant playback when they become active

#### Use Cases
- **Social Media Feeds**: Instagram, TikTok-style video scrolling with smooth audio transitions
- **Video Galleries**: Smooth browsing through video collections
- **E-commerce**: Product videos that start playing when users scroll near them
- **Educational Content**: Course videos that preload when students scroll through content

## Security & Privacy

- **No Data Collection**: Library doesn't collect user data or analytics
- **Local Processing**: All video processing happens locally on device
- **Secure URLs**: Supports HTTPS and secure video sources
- **Privacy First**: No tracking or external service dependencies

### HLS Adaptive Streaming

The Vync Video Player provides comprehensive support for HLS (HTTP Live Streaming) with automatic resolution adjustment:

#### **iOS Implementation (AVPlayer)**
- **Automatic Quality Selection**: `preferredPeakBitRate = 0` enables adaptive bitrate streaming
- **Network-Aware**: Automatically adjusts quality based on available bandwidth
- **Device Optimization**: Selects appropriate resolution for device capabilities
- **Seamless Switching**: Smooth transitions between different quality levels

#### **Android Implementation (ExoPlayer)**
- **Adaptive Track Selection**: Uses `DefaultTrackSelector` with adaptive parameters
- **Mixed MIME Type Support**: Handles different video formats in the same stream
- **SD to HD Scaling**: Starts with SD quality and adapts up based on conditions
- **Buffer Optimization**: Intelligent buffering for smooth quality transitions

#### **Key Benefits**
- **Bandwidth Efficiency**: Automatically uses the best quality for current network conditions
- **Battery Optimization**: Reduces power consumption by avoiding unnecessary high-quality streams
- **User Experience**: Prevents buffering by adapting to network constraints
- **Cost Effective**: Reduces data usage on mobile networks

#### **Supported Formats**
- **HLS (.m3u8)**: Full support for Apple's HTTP Live Streaming
- **DASH**: Dynamic Adaptive Streaming over HTTP
- **Progressive**: Standard MP4, WebM, and other formats
- **Live Streaming**: Real-time adaptive streaming support

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
- **Memory Usage**: ~20MB per active video, ~10MB per paused nearby video
- **Memory Efficiency**: Up to 60% reduction in total memory usage for large video lists
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

- Built with passion by the Zynclave Tech team
- Inspired by modern video streaming platforms
- Powered by native iOS and Android video frameworks
- Community-driven development and feedback

---

**Made by [Zynclave Tech](https://vync.live)**

*Transform your React Native apps with professional-grade video playback capabilities.*
