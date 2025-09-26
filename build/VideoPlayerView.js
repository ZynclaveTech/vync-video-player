import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';
const NativeView = requireNativeViewManager('VideoPlayer');
export class VideoPlayerView extends React.Component {
    ref = React.createRef();
    togglePlayback = () => {
        this.ref.current?.togglePlayback();
    };
    toggleMuted = () => {
        this.ref.current?.toggleMuted();
    };
    enterFullscreen = (keepDisplayOn) => {
        this.ref.current?.enterFullscreen(keepDisplayOn ?? false);
    };
    enterPictureInPicture = () => {
        this.ref.current?.enterPictureInPicture();
    };
    exitPictureInPicture = () => {
        this.ref.current?.exitPictureInPicture();
    };
    render() {
        return (<NativeView {...this.props} style={[this.props.style, { flex: 1 }]} ref={this.ref}/>);
    }
}
//# sourceMappingURL=VideoPlayerView.js.map