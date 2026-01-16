import { useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useFormik } from 'formik'
import * as Yup from 'yup'
import { PulseLoader } from 'react-spinners'
import { useTheme } from 'styled-components'

import { useLogin } from '../../hooks/useLogin'

import { Button } from '../../styles'
import { Container } from './styles'

const Login = () => {
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
        .email('Formato de email inválido.')
        .required('O campo email é obrigatório.'),
      password: Yup.string().required('O campo senha é obrigatório.')
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
    if (isSuccess) navigate('/')
  }, [navigate, isSuccess])

  return (
    <Container>
      <div className="loginContainer">
        <p className="title--small">Faça o login:</p>
        <div className="formBox">
          <div className="inputGroup">
            <label className="text" htmlFor="email">
              Email{' '}
              {isError('email') ? <small>* {form.errors.email}</small> : ''}
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
                <small>* {form.errors.password}</small>
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
            <div className="errorMessage">
              <small>
                <i className="fa-solid fa-circle-exclamation"></i>{' '}
                {error?.response?.data.message}
              </small>
            </div>
          )}
          <div className="btnGroup">
            <Button
              className="reset"
              type="button"
              onClick={() => form.handleSubmit()}
            >
              {isPending ? (
                <PulseLoader color={theme.colors.fontColor} size={8} />
              ) : (
                <>
                  Login <i className="fa-solid fa-arrow-right-to-bracket"></i>
                </>
              )}
            </Button>
          </div>
          <Link className="registerLink" to={-1}>
            Ainda não é cadastrado? <br /> <u>Clique aqui</u> e se cadastre!
          </Link>
        </div>
      </div>
    </Container>
  )
}

export default Login
