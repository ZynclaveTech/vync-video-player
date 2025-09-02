import {VideoPlayerView} from 'vync-video-player'
import {updateActiveVideoViewAsync} from 'vync-video-player'
import React from 'react'
import {
  FlatList,
  ListRenderItemInfo,
  Platform,
  Pressable,
  SafeAreaView,
  View,
  Text,
  Switch,
  SwitchChangeEvent
} from 'react-native'

import {SAMPLE_VIDEOS} from './sampleVideos'

export default function App() {
  const data = React.useMemo(() => {
    return [...SAMPLE_VIDEOS, ...SAMPLE_VIDEOS]
  }, [])

  const [fullscreenKeepDisplayOn, setFullscreenKeepDisplayOn] =
    React.useState<boolean>(false)
  const [gaps, setGaps] = React.useState<boolean>(false)
  const toggleFullscreenKeepDisplayOn = React.useCallback(
    (event: SwitchChangeEvent) => {
      setFullscreenKeepDisplayOn(v => !v)
    },
    [setFullscreenKeepDisplayOn]
  )

  const toggleGaps = React.useCallback(
    (event: SwitchChangeEvent) => {
      setGaps(v => !v)
    },
    [setGaps]
  )

  const renderItem = React.useCallback(
    ({item, index}: ListRenderItemInfo<string>) => {
      return (
        <Player
          url={item}
          num={index + 1}
          fullscreenKeepDisplayOn={fullscreenKeepDisplayOn}
          gaps={gaps}
        />
      )
    },
    [fullscreenKeepDisplayOn, gaps]
  )

  // @ts-ignore
  const uiManager = global?.nativeFabricUIManager ? 'Fabric' : 'Paper'

  return (
    <SafeAreaView style={{flex: 1}}>
      <Text style={{fontWeight: 'bold'}}>Options</Text>
      <View
        style={{
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'space-between'
        }}>
        <Text>Add gaps between videos</Text>
        <Switch onChange={toggleGaps} value={gaps} />
      </View>
      <View
        style={{
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'space-between'
        }}>
        <Text>Keep display on when fullscreen</Text>
        <Switch
          onChange={toggleFullscreenKeepDisplayOn}
          value={fullscreenKeepDisplayOn}
        />
      </View>
      <Text style={{height: 20}}>Renderer: {uiManager}</Text>
      <View style={{flex: 1}}>
        <FlatList
          data={data}
          renderItem={renderItem}
          removeClippedSubviews
          onScroll={() => {
            updateActiveVideoViewAsync()
          }}
          scrollEventThrottle={250}
        />
      </View>
    </SafeAreaView>
  )
}

function Player({
  url,
  num,
  fullscreenKeepDisplayOn,
  gaps
}: {
  url: string
  num: number
  fullscreenKeepDisplayOn: boolean
  gaps: boolean
}) {
  const ref = React.useRef<VideoPlayerView>(null)
  const [active, setActive] = React.useState(false)

  const onPress = () => {
    console.log('press')
    ref.current?.enterFullscreen(fullscreenKeepDisplayOn)
  }

  // Generate a sample thumbnail URL (you can replace this with actual thumbnail URLs)
  const thumbnailUrl = `https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?ixlib=rb-4.1.0&q=85&fm=jpg&crop=entropy&cs=srgb&dl=patrick-perkins-3wylDrjxH-E-unsplash.jpg&w=640`

  return (
    <Pressable
      style={{
        backgroundColor: active ? 'green' : 'gray',
        height: 300,
        marginBottom: gaps ? 400 : 0
      }}
      onPress={Platform.OS === 'ios' ? onPress : undefined}>
      <Text>Video: {num}</Text>
      <Text style={{ fontSize: 12, color: 'blue' }}>
        Thumbnail: {thumbnailUrl ? 'Set' : 'Not Set'} | 
        Show While Loading: {true} | 
        Show When Inactive: {true}
      </Text>
      <VideoPlayerView
        beginMuted={false}
        url={url}
        autoplay
        ref={ref}
        // Thumbnail support
        thumbnailUrl={thumbnailUrl}
        showThumbnailWhileLoading={true}
        showThumbnailWhenInactive={true}
        onError={e => {
          console.log('error', e.nativeEvent.error)
        }}
        onStatusChange={e => {
          console.log('▶️ Status changed:', e.nativeEvent.status)
        }}
        onActiveChange={e => {
          console.log('👁️ Active state changed:', e.nativeEvent.isActive)
          setActive(e.nativeEvent.isActive)
        }}
        onLoadingChange={e => {
          console.log('🔄 Loading state changed:', e.nativeEvent.isLoading)
        }}
        onTimeRemainingChange={e => {
          console.log('timeRemaining', e.nativeEvent.timeRemaining)
        }}
        onPlayerPress={Platform.OS === 'android' ? onPress : undefined}
      />
    </Pressable>
  )
}
