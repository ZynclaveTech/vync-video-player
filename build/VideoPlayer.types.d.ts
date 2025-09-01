import { NativeSyntheticEvent, StyleProp, ViewStyle, ImageStyle } from 'react-native';
export type VideoPlayerViewProps = {
    url: string;
    autoplay: boolean;
    beginMuted: boolean;
    forceTakeover?: boolean;
    accessibilityHint?: string;
    accessibilityLabel?: string;
    thumbnailUrl?: string;
    showThumbnailWhileLoading?: boolean;
    showThumbnailWhenInactive?: boolean;
    thumbnailStyle?: StyleProp<ImageStyle>;
    onActiveChange?: (e: NativeSyntheticEvent<{
        isActive: boolean;
    }>) => void;
    onLoadingChange?: (e: NativeSyntheticEvent<{
        isLoading: boolean;
    }>) => void;
    onMutedChange?: (e: NativeSyntheticEvent<{
        isMuted: boolean;
    }>) => void;
    onPlayerPress?: () => void;
    onStatusChange?: (e: NativeSyntheticEvent<{
        status: 'playing' | 'paused';
    }>) => void;
    onTimeRemainingChange?: (e: NativeSyntheticEvent<{
        timeRemaining: number;
    }>) => void;
    onFullscreenChange?: (e: NativeSyntheticEvent<{
        isFullscreen: boolean;
    }>) => void;
    onError?: (e: NativeSyntheticEvent<{
        error: string;
    }>) => void;
    style?: StyleProp<ViewStyle>;
};
//# sourceMappingURL=VideoPlayer.types.d.ts.map