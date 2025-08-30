import {requireNativeModule} from 'expo-modules-core'

const NativeModule = requireNativeModule('VideoPlayer')

export async function updateActiveVideoViewAsync() {
  NativeModule.updateActiveVideoViewAsync()
}
