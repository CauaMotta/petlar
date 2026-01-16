import { createSlice, type PayloadAction } from '@reduxjs/toolkit'
import Cookies from 'js-cookie'

type AuthState = {
  user: User | null
  token: string | null
  isAuthenticated: boolean
}

const initialState: AuthState = {
  user: null,
  token: Cookies.get('token') || null,
  isAuthenticated: !!Cookies.get('token')
}

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginSuccess(state, action: PayloadAction<LoginResponse>) {
      state.token = action.payload.token
      state.user = action.payload.user
      state.isAuthenticated = true

      Cookies.set('token', action.payload.token, {
        expires: 1,
        sameSite: 'strict'
      })
    },

    logout(state) {
      state.user = null
      state.token = null
      state.isAuthenticated = false
      Cookies.remove('token')
    },

    setUser(state, action: PayloadAction<User>) {
      state.user = action.payload
    }
  }
})

export const { loginSuccess, logout, setUser } = authSlice.actions
export default authSlice.reducer
