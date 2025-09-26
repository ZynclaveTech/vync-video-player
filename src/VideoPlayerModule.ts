import {requireNativeModule} from 'expo-modules-core'

const NativeModule = requireNativeModule('VideoPlayer')

export async function updateActiveVideoViewAsync() {
  if (!NativeModule) {
    throw new Error('Native module not available')
  }
  return NativeModule.updateActiveVideoViewAsync()
}

export async function enterPictureInPictureAsync() {
  if (!NativeModule) {
    throw new Error('PiP not supported')
  }
  return NativeModule.enterPictureInPictureAsync()
}

export async function exitPictureInPictureAsync() {
  if (!NativeModule) {
    throw new Error('PiP not active')
  }
  return NativeModule.exitPictureInPictureAsync()
}
