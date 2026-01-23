import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useFormik } from 'formik'
import * as Yup from 'yup'
import { PulseLoader } from 'react-spinners'
import { useTheme } from 'styled-components'
import { useSelector } from 'react-redux'

import Modal from '../../components/Modal'

import { useRegisterUser } from '../../hooks/useUser'
import type { RootReducer } from '../../store'

import { Container } from './styles'
import {
  ButtonGroup,
  ErrorMessage,
  InputGroup,
  StyledButton
} from '../../styles'

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
        <div>
          <InputGroup
            $light
            $fontSize="14px"
            $borderRadius="6px"
            $minHeight="28px"
          >
            <label className="text" htmlFor="name">
              Nome{' '}
              {isError('name') ? (
                <ErrorMessage>* {form.errors.name}</ErrorMessage>
              ) : (
                ''
              )}
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
          </InputGroup>
          <InputGroup
            $light
            $fontSize="14px"
            $borderRadius="6px"
            $minHeight="28px"
          >
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
          </InputGroup>
          <InputGroup
            $light
            $fontSize="14px"
            $borderRadius="6px"
            $minHeight="28px"
          >
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
          </InputGroup>
          {error && (
            <div className="textCenter">
              <ErrorMessage>
                <i className="fa-solid fa-circle-exclamation"></i>{' '}
                {error?.response?.data.message}
              </ErrorMessage>
            </div>
          )}
          <ButtonGroup>
            <StyledButton onClick={() => form.handleSubmit()}>
              {isPending ? (
                <PulseLoader color={theme.colors.fontColor} size={8} />
              ) : (
                <>
                  Cadastrar <i className="fa-solid fa-paw"></i>
                </>
              )}
            </StyledButton>
          </ButtonGroup>
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
