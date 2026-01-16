import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'

import { useGetMe } from '../../hooks/useUser'

import { setUser, logout } from '../../store/reducers/authSlice'
import type { RootReducer } from '../../store'

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const dispatch = useDispatch()
  const { token } = useSelector((state: RootReducer) => state.auth)

  const { data: user, isError } = useGetMe()

  useEffect(() => {
    if (token && user) {
      dispatch(setUser(user))
    } else if (!token || isError) {
      dispatch(logout())
    }
  }, [token, user, isError, dispatch])

  return <>{children}</>
}
