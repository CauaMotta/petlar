import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useFormik } from 'formik'
import * as Yup from 'yup'
import { PulseLoader } from 'react-spinners'
import { useTheme } from 'styled-components'
import { useSelector } from 'react-redux'

import { useRegisterUser } from '../../hooks/useUser'
import type { RootReducer } from '../../store'

import { Button } from '../../styles'
import { Container } from './styles'
import Modal from '../../components/Modal'

const RegisterUser = () => {
  const { isAuthenticated } = useSelector((state: RootReducer) => state.auth)
  const theme = useTheme()
  const navigate = useNavigate()
  const { mutate, error, isPending, isSuccess } = useRegisterUser()

  const form = useFormik({
    initialValues: {
      name: '',
      email: '',
      password: ''
    },
    validationSchema: Yup.object({
      name: Yup.string()
        .min(3, 'Deve conter no minímo 3 caracteres.')
        .max(110, 'Excedeu o limite de caracteres.')
        .required('Este campo é obrigatório.'),
      email: Yup.string()
        .email('Formato inválido.')
        .required('Este campo é obrigatório.'),
      password: Yup.string()
        .min(6, 'Deve conter no minímo 6 caracteres.')
        .max(50, 'Excedeu o limite de caracteres.')
        .required('Este campo é obrigatório.')
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

  if (isAuthenticated) return <Navigate to="/" />

  return (
    <Container>
      <div className="loginContainer">
        <p className="title--small">Cadastre-se!</p>
        <div className="formBox">
          <div className="inputGroup">
            <label className="text" htmlFor="name">
              Nome {isError('name') ? <small>* {form.errors.name}</small> : ''}
            </label>
            <input
              id="name"
              type="text"
              value={form.values.name}
              onChange={form.handleChange}
              onBlur={(e) => {
                const trimmedValue = e.target.value.replace(/\s+/g, ' ').trim()
                form.setFieldValue('name', trimmedValue)
                form.handleBlur(e)
              }}
            />
          </div>
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
            <Button type="button" onClick={() => form.handleSubmit()}>
              {isPending ? (
                <PulseLoader color={theme.colors.fontColor} size={8} />
              ) : (
                <>
                  Cadastrar <i className="fa-solid fa-paw"></i>
                </>
              )}
            </Button>
          </div>
          <Link className="registerLink" to={'/login'}>
            Já possuí conta? <br /> <u>Clique aqui</u> e faça o login.
          </Link>
        </div>
      </div>
      <Modal
        onClose={() => navigate('/login')}
        isOpen={isSuccess}
        title="Parabéns!"
      >
        <div>
          <p className="text">Cadastro realizado com sucesso!</p>
        </div>
      </Modal>
    </Container>
  )
}

export default RegisterUser
