import { useEffect } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useFormik } from 'formik'
import * as Yup from 'yup'
import { PulseLoader } from 'react-spinners'
import { useTheme } from 'styled-components'
import { useSelector } from 'react-redux'

import StyledButton from '../../components/StyledButton'

import { useLogin } from '../../hooks/useLogin'
import type { RootReducer } from '../../store'

import { Container } from './styles'
import { ErrorMessage } from '../../styles'

const Login = () => {
  const { isAuthenticated } = useSelector((state: RootReducer) => state.auth)
  const theme = useTheme()
  const navigate = useNavigate()
  const { mutate, error, isPending, isSuccess } = useLogin()

  const form = useFormik({
    initialValues: {
      email: '',
      password: ''
    },
    validationSchema: Yup.object({
      email: Yup.string()
        .email('Formato inválido.')
        .required('Este campo é obrigatório.'),
      password: Yup.string().required('Este campo é obrigatório.')
    }),
    onSubmit: async (values) => {
      mutate(values)
    },
    validateOnMount: true
  })

  const isError = (fieldName: string) => {
    const isTouched = fieldName in form.touched
    const isInvalid = fieldName in form.errors

    if (isTouched && isInvalid) return true
    return false
  }

  useEffect(() => {
    if (isSuccess) navigate('/', { replace: true })
  }, [navigate, isSuccess])

  if (isAuthenticated) return <Navigate to="/" />

  return (
    <Container>
      <div className="loginContainer">
        <p className="title--small">Faça o login</p>
        <div className="formBox">
          <div className="inputGroup">
            <label className="text" htmlFor="email">
              Email{' '}
              {isError('email') ? (
                <ErrorMessage>* {form.errors.email}</ErrorMessage>
              ) : (
                ''
              )}
            </label>
            <input
              id="email"
              type="text"
              value={form.values.email}
              onChange={form.handleChange}
              onBlur={form.handleBlur}
            />
          </div>
          <div className="inputGroup">
            <label className="text" htmlFor="password">
              Senha{' '}
              {isError('password') ? (
                <ErrorMessage>* {form.errors.password}</ErrorMessage>
              ) : (
                ''
              )}
            </label>
            <input
              type="password"
              id="password"
              value={form.values.password}
              onChange={form.handleChange}
              onBlur={form.handleBlur}
            />
          </div>
          {error && (
            <div className="errorWrapper">
              <ErrorMessage>
                <i className="fa-solid fa-circle-exclamation"></i>{' '}
                {error.response?.data.message}
              </ErrorMessage>
            </div>
          )}
          <div className="btnGroup">
            <StyledButton onClick={() => form.handleSubmit()}>
              {isPending ? (
                <PulseLoader color={theme.colors.fontColor} size={8} />
              ) : (
                <>
                  Login <i className="fa-solid fa-arrow-right-to-bracket"></i>
                </>
              )}
            </StyledButton>
          </div>
          <Link className="registerLink" to={'/register'}>
            Ainda não é cadastrado? <br /> <u>Clique aqui</u> e se cadastre!
          </Link>
        </div>
      </div>
    </Container>
  )
}

export default Login
