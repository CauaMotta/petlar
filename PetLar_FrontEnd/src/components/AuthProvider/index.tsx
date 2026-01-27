import { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import Cookies from 'js-cookie'

import { useGetMe } from '../../hooks/useUser'

import { setUser, logout } from '../../store/reducers/authSlice'

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const dispatch = useDispatch()
  const token = Cookies.get('token')

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
