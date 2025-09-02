import * as React from 'react';
import { VideoPlayerViewProps } from './VideoPlayer.types';
export declare class VideoPlayerView extends React.Component<VideoPlayerViewProps> {
    ref: React.RefObject<any>;
    togglePlayback: () => void;
    toggleMuted: () => void;
    enterFullscreen: (keepDisplayOn?: boolean) => void;
    render(): React.JSX.Element;
}
//# sourceMappingURL=VideoPlayerView.d.ts.map