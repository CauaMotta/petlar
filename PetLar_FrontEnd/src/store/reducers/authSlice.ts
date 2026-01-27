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
    },

    setToken(state, action: PayloadAction<string>) {
      state.token = action.payload

      Cookies.set('token', action.payload, {
        expires: 1,
        sameSite: 'strict'
      })
    }
  }
})

export const { loginSuccess, logout, setUser, setToken } = authSlice.actions
export default authSlice.reducer
