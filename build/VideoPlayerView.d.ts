import * as React from 'react';
import { VideoPlayerViewProps } from './VideoPlayer.types';
export declare class VideoPlayerView extends React.Component<VideoPlayerViewProps> {
    ref: React.RefObject<any>;
    state: {
        isLoading: boolean;
        isActive: boolean;
    };
    togglePlayback: () => void;
    toggleMuted: () => void;
    enterFullscreen: (keepDisplayOn?: boolean) => void;
    onLoadingChange: (e: any) => void;
    onActiveChange: (e: any) => void;
    shouldShowThumbnail: () => boolean | undefined;
    renderThumbnail(): React.JSX.Element | null;
    render(): React.JSX.Element;
}
//# sourceMappingURL=VideoPlayerView.d.ts.map