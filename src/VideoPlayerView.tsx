import {requireNativeViewManager} from 'expo-modules-core'
import * as React from 'react'
import {StyleProp, ViewStyle, Image, View, ImageStyle} from 'react-native'

import {VideoPlayerViewProps} from './VideoPlayer.types'

const NativeView: React.ComponentType<
  VideoPlayerViewProps & {
    style: StyleProp<ViewStyle>
    ref: React.Ref<any>
  }
> = requireNativeViewManager('VideoPlayer')

export class VideoPlayerView extends React.Component<VideoPlayerViewProps> {
  ref: React.RefObject<any> = React.createRef()
  state = {
    isLoading: true,
    isActive: false,
    showThumbnail: false
  }

  togglePlayback = () => {
    this.ref.current?.togglePlayback()
  }

  toggleMuted = () => {
    this.ref.current?.toggleMuted()
  }

  enterFullscreen = (keepDisplayOn?: boolean) => {
    this.ref.current?.enterFullscreen(keepDisplayOn ?? false)
  }

  onLoadingChange = (e: any) => {
    const isLoading = e.nativeEvent.isLoading
    this.setState({ 
      isLoading,
      showThumbnail: isLoading && this.props.showThumbnailWhileLoading
    })
  }

  onActiveChange = (e: any) => {
    const isActive = e.nativeEvent.isActive
    this.setState({ 
      isActive,
      showThumbnail: !isActive && this.props.showThumbnailWhenInactive
    })
  }

  renderThumbnail() {
    const { thumbnailUrl, thumbnailStyle, style } = this.props
    const { showThumbnail } = this.state

    if (!showThumbnail || !thumbnailUrl) {
      return null
    }

    return (
      <Image
        source={{ uri: thumbnailUrl }}
        style={[
          {
            position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            resizeMode: 'cover',
            zIndex: 1
          },
          thumbnailStyle,
          style as StyleProp<ImageStyle>
        ]}
      />
    )
  }

  render() {
    const { thumbnailUrl, showThumbnailWhileLoading, showThumbnailWhenInactive, thumbnailStyle, ...props } = this.props

    return (
      <View style={[{ flex: 1 }, this.props.style]}>
        {this.renderThumbnail()}
        <NativeView
          {...props}
          style={[{ flex: 1 }, this.props.style]}
          ref={this.ref}
          onLoadingChange={this.onLoadingChange}
          onActiveChange={this.onActiveChange}
        />
      </View>
    )
  }
}
