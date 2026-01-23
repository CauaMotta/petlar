import { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'
import { useFormik } from 'formik'
import * as Yup from 'yup'
import { useTheme } from 'styled-components'

import Modal from '../Modal'
import StyledButton from '../StyledButton'

import type { RootReducer } from '../../store'
import {
  useChangePassword,
  useDeleteUser,
  useUpdateUser
} from '../../hooks/useUser'

import { Container } from './styles'
import { ErrorMessage } from '../../styles'

const ProfileInfo = () => {
  const theme = useTheme()
  const { user } = useSelector((state: RootReducer) => state.auth)
  const [editProfile, setEditProfile] = useState<boolean>(false)
  const [editPassword, setEditPassword] = useState<boolean>(false)
  const { mutate: updateUser, isSuccess, error, reset } = useUpdateUser()
  const {
    mutate: changePassword,
    isSuccess: cpIsSuccess,
    reset: cpReset,
    error: cpError
  } = useChangePassword()
  const { mutate: deleteUser } = useDeleteUser()

  const editProfileForm = useFormik({
    initialValues: {
      name: user?.name || '',
      email: user?.email || ''
    },
    enableReinitialize: true,
    validationSchema: Yup.object({
      name: Yup.string()
        .min(3, 'Deve conter no minímo 3 caracteres.')
        .max(110, 'Excedeu o limite de caracteres.')
        .required('Este campo é obrigatório.'),
      email: Yup.string()
        .email('Formato inválido.')
        .required('Este campo é obrigatório.')
    }),
    onSubmit: async (values) => {
      updateUser(values)
    },
    validateOnMount: true
  })

  const editPasswordForm = useFormik({
    initialValues: {
      password: ''
    },
    validationSchema: Yup.object({
      password: Yup.string()
        .min(6, 'Deve conter no minímo 6 caracteres.')
        .max(50, 'Excedeu o limite de caracteres.')
        .required('Este campo é obrigatório.')
    }),
    onSubmit: async (values) => {
      changePassword(values)
    },
    validateOnMount: true
  })

  useEffect(() => {
    if (isSuccess) setEditProfile(false)
    if (cpIsSuccess) setEditPassword(false)
  }, [isSuccess, cpIsSuccess])

  const isProfileFormError = (fieldName: string) => {
    const isTouched = fieldName in editProfileForm.touched
    const isInvalid = fieldName in editProfileForm.errors

    if (isTouched && isInvalid) return true
    return false
  }

  const isPasswordFormError = (fieldName: string) => {
    const isTouched = fieldName in editPasswordForm.touched
    const isInvalid = fieldName in editPasswordForm.errors

    if (isTouched && isInvalid) return true
    return false
  }

  return (
    <Container>
      <div className="avatar">
        <i className="fa-solid fa-user"></i>
      </div>
      <div className="profileInfo">
        <h3 className="title">{user?.name}</h3>
        <p className="text">{user?.email}</p>
      </div>
      <div className="btnGroup">
        <StyledButton
          backgroundColor={theme.colors.highlightColor}
          paddingBlock="6px"
          paddingInline="12px"
          fontSize="14px"
          maxWidth="fit-content"
          onClick={() => {
            setEditProfile(true)
          }}
        >
          <>
            <i className="fa-solid fa-pen-to-square"></i> Editar perfil
          </>
        </StyledButton>

        <StyledButton
          backgroundColor={theme.colors.highlightColor}
          paddingBlock="6px"
          paddingInline="12px"
          fontSize="14px"
          maxWidth="fit-content"
          onClick={() => {
            setEditPassword(true)
          }}
        >
          <>
            <i className="fa-solid fa-key"></i> Trocar senha
          </>
        </StyledButton>
      </div>

      <Modal
        title="Editar perfil"
        isOpen={editProfile}
        onClose={() => {
          setEditProfile(false)
          editProfileForm.resetForm()
          reset()
        }}
      >
        <form>
          <div className="inputGroup">
            <label className="text" htmlFor="name">
              Nome{' '}
              {isProfileFormError('name') ? (
                <ErrorMessage>* {editProfileForm.errors.name}</ErrorMessage>
              ) : (
                ''
              )}
            </label>
            <input
              id="name"
              type="text"
              value={editProfileForm.values.name}
              onChange={editProfileForm.handleChange}
              onBlur={(e) => {
                const trimmedValue = e.target.value.replace(/\s+/g, ' ').trim()
                editProfileForm.setFieldValue('name', trimmedValue)
                editProfileForm.handleBlur(e)
              }}
            />
          </div>
          <div className="inputGroup">
            <label className="text" htmlFor="email">
              Email{' '}
              {isProfileFormError('email') ? (
                <ErrorMessage>* {editProfileForm.errors.email}</ErrorMessage>
              ) : (
                ''
              )}
            </label>
            <input
              id="email"
              type="text"
              value={editProfileForm.values.email}
              onChange={editProfileForm.handleChange}
              onBlur={editProfileForm.handleBlur}
            />
          </div>
          {error && (
            <ErrorMessage>
              <i className="fa-solid fa-circle-exclamation"></i>{' '}
              {error.response?.data.message}
            </ErrorMessage>
          )}
          <div className="btnGroup">
            <StyledButton
              backgroundColor={theme.colors.highlightColor}
              fontSize="14px"
              maxWidth="fit-content"
              onClick={() => {
                editProfileForm.resetForm()
                reset()
              }}
            >
              <>
                Reset <i className="fa-solid fa-arrows-rotate"></i>
              </>
            </StyledButton>
            <StyledButton
              backgroundColor={theme.colors.highlightColor}
              fontSize="14px"
              maxWidth="fit-content"
              onClick={() => editProfileForm.handleSubmit()}
            >
              <>
                Salvar <i className="fa-solid fa-floppy-disk"></i>
              </>
            </StyledButton>
          </div>
          <button
            className="btnDelete"
            type="button"
            onClick={() => deleteUser()}
          >
            <i className="fa-solid fa-trash"></i> Apagar conta
          </button>
        </form>
      </Modal>

      <Modal
        title="Editar senha"
        isOpen={editPassword}
        onClose={() => {
          setEditPassword(false)
          editPasswordForm.resetForm()
          cpReset()
        }}
      >
        <form>
          <div className="inputGroup">
            <label className="text" htmlFor="password">
              Senha{' '}
              {isPasswordFormError('password') ? (
                <ErrorMessage>
                  * {editPasswordForm.errors.password}
                </ErrorMessage>
              ) : (
                ''
              )}
            </label>
            <input
              id="password"
              type="password"
              value={editPasswordForm.values.password}
              onChange={editPasswordForm.handleChange}
              onBlur={(e) => {
                const trimmedValue = e.target.value.replace(/\s+/g, ' ').trim()
                editPasswordForm.setFieldValue('password', trimmedValue)
                editPasswordForm.handleBlur(e)
              }}
            />
          </div>
          {cpError && (
            <ErrorMessage>
              <i className="fa-solid fa-circle-exclamation"></i>{' '}
              {cpError.response?.data.message}
            </ErrorMessage>
          )}
          <div className="btnGroup">
            <StyledButton
              backgroundColor={theme.colors.highlightColor}
              fontSize="14px"
              maxWidth="fit-content"
              onClick={() => {
                editPasswordForm.resetForm()
                cpReset()
              }}
            >
              <>
                Reset <i className="fa-solid fa-arrows-rotate"></i>
              </>
            </StyledButton>
            <StyledButton
              backgroundColor={theme.colors.highlightColor}
              fontSize="14px"
              maxWidth="fit-content"
              onClick={() => editPasswordForm.handleSubmit()}
            >
              <>
                Salvar <i className="fa-solid fa-floppy-disk"></i>
              </>
            </StyledButton>
          </div>
        </form>
      </Modal>

      <Modal isOpen={cpIsSuccess} onClose={() => cpReset()} title="Sucesso">
        <p>Senha atualizada com sucesso!</p>
      </Modal>
    </Container>
  )
}

export default ProfileInfo
