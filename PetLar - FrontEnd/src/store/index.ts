import { configureStore } from '@reduxjs/toolkit'

import themeReducer from './reducers/Theme'

const store = configureStore({
  reducer: {
    theme: themeReducer
  }
})

export type RootReducer = ReturnType<typeof store.getState>
export default store
