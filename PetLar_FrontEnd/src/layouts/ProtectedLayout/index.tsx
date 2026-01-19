import { Navigate, Outlet } from 'react-router-dom'

type Props = {
  isAuthenticated: boolean
}

const ProtectedLayout = ({ isAuthenticated }: Props) => {
  if (!isAuthenticated) return <Navigate to="/login" replace />

  return (
    <>
      <Outlet />
    </>
  )
}

export default ProtectedLayout
