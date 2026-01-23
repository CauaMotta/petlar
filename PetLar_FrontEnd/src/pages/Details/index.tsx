import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { useFormik } from 'formik'
import { useTheme } from 'styled-components'
import * as Yup from 'yup'

import BackButton from '../../components/BackButton'
import Loader from '../../components/Loader'
import Modal from '../../components/Modal'

import { API_URL } from '../../main'
import { useGetAnimalById } from '../../hooks/useAnimals'
import { useInitAdoption } from '../../hooks/useAdoption'
import { formatDateBr, formatWeight } from '../../utils'
import type { RootReducer } from '../../store'

import { Card, Container, Description } from './styles'
import { ErrorMessage, StyledButton, Line, ButtonGroup } from '../../styles'

type Params = {
  id: string
}

const Details = () => {
  const theme = useTheme()
  const { id } = useParams() as Params
  const { data, isLoading, isError } = useGetAnimalById(id)
  const { isAuthenticated } = useSelector((state: RootReducer) => state.auth)
  const [showModal, setShowModal] = useState<boolean>(false)
  const [showErrorModal, setShowErrorModal] = useState<boolean>(false)
  const navigate = useNavigate()
  const { mutate, error, reset, isSuccess } = useInitAdoption()

  const form = useFormik({
    initialValues: {
      reason: ''
    },
    validationSchema: Yup.object({
      reason: Yup.string()
        .min(3, 'Deve conter no minímo 3 caracteres.')
        .max(255, 'Excedeu o limite de caracteres.')
        .required('Este campo é obrigatório.')
    }),
    onSubmit: async (values) => {
      const payload = {
        animalId: id,
        reason: values.reason
      }

      mutate(payload)
    },
    validateOnMount: true
  })

  const isFieldError = (fieldName: string) => {
    const isTouched = fieldName in form.touched
    const isInvalid = fieldName in form.errors

    if (isTouched && isInvalid) return true
    return false
  }

  const handleClick = () => {
    if (isAuthenticated) {
      setShowModal(true)
    } else {
      setShowErrorModal(true)
    }
  }

  useEffect(() => {
    if (isSuccess) setShowModal(false)
  }, [isSuccess])

  if (isLoading)
    return (
      <Container>
        <div className="box">
          <Loader />
        </div>
      </Container>
    )

  if (isError || !data)
    return (
      <Container>
        <BackButton path={-1} />
        <div className="box">
          <i className="fa-solid fa-file-circle-xmark"></i>
          <p className="text">Não encontramos este registro em nosso banco!</p>
        </div>
      </Container>
    )

  return (
    <Container>
      <BackButton path={-1} />
      <Card>
        <div className="image">
          {data.imagePath != null ? (
            <img src={API_URL + data.imagePath} alt={data.name} />
          ) : (
            <span className="noImage">
              <i className="fa-solid fa-image"></i>
            </span>
          )}
        </div>
        <div className="content">
          <h2 className="title">{data.name}</h2>
          <Line />
          <div className="info">
            <p className="text">
              <b>Espécie:</b> {data.type}
            </p>
            <p className="text">
              <b>Porte:</b> {data.size}
            </p>
            <p className="text">
              <b>Sexo:</b> {data.sex}
            </p>
            <p className="text">
              <b>Peso:</b> {formatWeight(data.weight)} kg
            </p>
            <p className="text">
              <b>Data de nascimento:</b> {formatDateBr(data.birthDate)}
            </p>
          </div>
          <div className="contact">
            <p className="text">
              <b>Registrado por:</b> {data.author.name}
            </p>
            <StyledButton $maxWidth="fit-content" onClick={handleClick}>
              Quero Adotar! <i className="fa-solid fa-paw"></i>
            </StyledButton>
          </div>
        </div>
      </Card>
      {data.description != null && (
        <Description>
          <h3 className="title--small">Descrição</h3>
          <Line />
          <p className="text">{data.description}</p>
        </Description>
      )}

      <Modal
        title="Solicitar adoção"
        isOpen={showModal}
        onClose={() => {
          form.resetForm()
          reset()
          setShowModal(false)
        }}
      >
        <form>
          <div className="inputGroup">
            <label className="text" htmlFor="reason">
              Motivo{' '}
              {isFieldError('reason') ? (
                <ErrorMessage>* {form.errors.reason}</ErrorMessage>
              ) : (
                ''
              )}
            </label>
            <textarea
              id="reason"
              rows={3}
              value={form.values.reason}
              onChange={form.handleChange}
              onBlur={(e) => {
                const trimmedValue = e.target.value.replace(/\s+/g, ' ').trim()
                form.setFieldValue('reason', trimmedValue)
                form.handleBlur(e)
              }}
            />
          </div>
          {error && (
            <ErrorMessage>
              <i className="fa-solid fa-circle-exclamation"></i>{' '}
              {error.response?.data.message}
            </ErrorMessage>
          )}
          <ButtonGroup $marginTop="12px">
            <StyledButton
              $backgroundColor={theme.colors.highlightColor}
              $fontSize="14px"
              $maxWidth="fit-content"
              type="button"
              onClick={() => {
                form.resetForm()
                reset()
                setShowModal(false)
              }}
            >
              Cancelar <i className="fa-solid fa-xmark"></i>
            </StyledButton>

            <StyledButton
              $backgroundColor={theme.colors.highlightColor}
              $fontSize="14px"
              $maxWidth="fit-content"
              type="button"
              onClick={() => {
                form.handleSubmit()
              }}
            >
              Enviar <i className="fa-solid fa-share"></i>
            </StyledButton>
          </ButtonGroup>
        </form>
      </Modal>

      <Modal
        onClose={() => {
          reset()
          navigate('/')
        }}
        isOpen={isSuccess}
        title="Enviado!"
      >
        <p style={{ textAlign: 'center' }}>
          Solicitação enviada, <br /> agora só aguardar a resposta.
        </p>
      </Modal>

      <Modal
        title="Não autorizado"
        isOpen={showErrorModal}
        onClose={() => {
          setShowErrorModal(false)
          navigate('/login')
        }}
      >
        <p>
          Necessário autenticação <br /> para solicitar uma adoção.
        </p>
      </Modal>
    </Container>
  )
}

export default Details
