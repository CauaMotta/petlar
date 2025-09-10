import { createSlice, type PayloadAction } from '@reduxjs/toolkit'

import { dogTheme, themesMap } from '../../themes'

type ThemeState = {
  theme: typeof dogTheme
}

const initialState: ThemeState = {
  theme: dogTheme
}

const ThemeSlice = createSlice({
  name: 'theme',
  initialState,
  reducers: {
    changeTheme: (state, action: PayloadAction<string>) => {
      state.theme = themesMap[action.payload]
    }
  }
})

export const { changeTheme } = ThemeSlice.actions
export default ThemeSlice.reducer
