import { configureStore } from '@reduxjs/toolkit'

import authReducer from './reducers/authSlice'

const store = configureStore({
  reducer: {
    auth: authReducer
  }
})

export type RootReducer = ReturnType<typeof store.getState>
export default store
