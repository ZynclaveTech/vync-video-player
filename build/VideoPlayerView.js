import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';
import { Image, View } from 'react-native';
const NativeView = requireNativeViewManager('VideoPlayer');
export class VideoPlayerView extends React.Component {
    ref = React.createRef();
    state = {
        isLoading: true,
        isActive: false
    };
    togglePlayback = () => {
        this.ref.current?.togglePlayback();
    };
    toggleMuted = () => {
        this.ref.current?.toggleMuted();
    };
    enterFullscreen = (keepDisplayOn) => {
        this.ref.current?.enterFullscreen(keepDisplayOn ?? false);
    };
    onLoadingChange = (e) => {
        const isLoading = e.nativeEvent.isLoading;
        console.log('VideoPlayerView onLoadingChange:', isLoading);
        this.setState({ isLoading });
    };
    onActiveChange = (e) => {
        const isActive = e.nativeEvent.isActive;
        console.log('VideoPlayerView onActiveChange:', isActive);
        this.setState({ isActive });
    };
    shouldShowThumbnail = () => {
        const { showThumbnailWhileLoading, showThumbnailWhenInactive } = this.props;
        const { isLoading, isActive } = this.state;
        const shouldShow = ((isLoading && showThumbnailWhileLoading) ||
            (!isActive && showThumbnailWhenInactive));
        console.log('shouldShowThumbnail:', {
            isLoading,
            isActive,
            showThumbnailWhileLoading,
            showThumbnailWhenInactive,
            shouldShow
        });
        return shouldShow;
    };
    renderThumbnail() {
        const { thumbnailUrl, thumbnailStyle, style } = this.props;
        if (!this.shouldShowThumbnail() || !thumbnailUrl) {
            return null;
        }
        return (<Image source={{ uri: thumbnailUrl }} style={[
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
                style
            ]}/>);
    }
    render() {
        const { thumbnailUrl, showThumbnailWhileLoading, showThumbnailWhenInactive, thumbnailStyle, ...props } = this.props;
        return (<View style={[{ flex: 1 }, this.props.style]}>
        {this.renderThumbnail()}
        <NativeView {...props} style={[{ flex: 1 }, this.props.style]} ref={this.ref} onLoadingChange={this.onLoadingChange} onActiveChange={this.onActiveChange}/>
      </View>);
    }
}
//# sourceMappingURL=VideoPlayerView.js.map