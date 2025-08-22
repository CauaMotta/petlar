import { configureStore } from '@reduxjs/toolkit'

import selectReducer from './reducers/Select'
import themeReducer from './reducers/Theme'

const store = configureStore({
  reducer: {
    select: selectReducer,
    theme: themeReducer
  }
})

export type RootReducer = ReturnType<typeof store.getState>
export default store
