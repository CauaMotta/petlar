import { createSlice, type PayloadAction } from '@reduxjs/toolkit'

type selectState = {
  id: string
}

const initialState: selectState = {
  id: ''
}

const selectSlice = createSlice({
  name: 'select',
  initialState,
  reducers: {
    setId: (state, action: PayloadAction<string>) => {
      state.id = action.payload
    }
  }
})

export const { setId } = selectSlice.actions
export default selectSlice.reducer
