import {requireNativeViewManager} from 'expo-modules-core'
import * as React from 'react'
import {StyleProp, ViewStyle} from 'react-native'

import {VideoPlayerViewProps} from './VideoPlayer.types'

const NativeView: React.ComponentType<
  VideoPlayerViewProps & {
    style: StyleProp<ViewStyle>
    ref: React.Ref<any>
  }
> = requireNativeViewManager('VideoPlayer')

export class VideoPlayerView extends React.Component<VideoPlayerViewProps> {
  ref: React.RefObject<any> = React.createRef()

  togglePlayback = () => {
    this.ref.current?.togglePlayback()
  }

  toggleMuted = () => {
    this.ref.current?.toggleMuted()
  }

  enterFullscreen = (keepDisplayOn?: boolean) => {
    this.ref.current?.enterFullscreen(keepDisplayOn ?? false)
  }

  render() {
    return (
      <NativeView
        {...this.props}
        style={[this.props.style, {flex: 1}]}
        ref={this.ref}
      />
    )
  }
}
