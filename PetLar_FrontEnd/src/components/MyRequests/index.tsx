import { useEffect, useState } from 'react'
import { useFormik } from 'formik'
import * as Yup from 'yup'
import { useTheme } from 'styled-components'

import CardForProfile from '../CardForProfile'
import Modal from '../Modal'
import PageCounter from '../PageCounter'

import {
  useEditReason,
  useGetMyRequests,
  useStatusUpdate
} from '../../hooks/useAdoption'

import {
  ButtonGroup,
  CardContainer,
  ErrorMessage,
  InputGroup,
  StyledButton
} from '../../styles'

const MyRequests = () => {
  const theme = useTheme()
  const [showModal, setShowModal] = useState<boolean>(false)
  const [showEditModal, setShowEditModal] = useState<boolean>(false)
  const [currentPage, setCurrentPage] = useState<number>(1)
  const [adoption, setAdoption] = useState<Adoption>()
  const { data, totalPages } = useGetMyRequests({
    page: currentPage - 1,
    size: 4
  })
  const { mutate } = useStatusUpdate()
  const { mutate: edit, error: editError, isSuccess, reset } = useEditReason()

  const form = useFormik({
    initialValues: {
      reason: adoption?.reason || ''
    },
    enableReinitialize: true,
    validationSchema: Yup.object({
      reason: Yup.string()
        .min(3, 'Deve conter no minímo 3 caracteres.')
        .max(255, 'Excedeu o limite de caracteres.')
        .required('Este campo é obrigatório.')
    }),
    onSubmit: async (values) => {
      edit({ id: adoption!.id, reason: values.reason })
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
    if (isSuccess) setShowEditModal(false)
  }, [isSuccess])

  return (
    <>
      <CardContainer>
        {data.length == 0 && (
          <p className="text">Você ainda não solicitou nenhum animal!</p>
        )}
        {data
          .sort((a, b) => {
            const statusA = a.status.toLowerCase()
            const statusB = b.status.toLowerCase()

            if (statusA === 'pendente' && statusB !== 'pendente') return -1
            if (statusA !== 'pendente' && statusB === 'pendente') return 1
            return 0
          })
          .map((adoption) => (
            <CardForProfile key={adoption.id} animal={adoption.animal}>
              <ButtonGroup $marginTop="8px">
                {adoption.status.toLowerCase() === 'pendente' ? (
                  <>
                    <StyledButton
                      $fontSize="14px"
                      onClick={() => {
                        setShowModal(true)
                        setAdoption(adoption)
                      }}
                    >
                      <i className="fa-solid fa-eye"></i> Visualizar
                    </StyledButton>
                  </>
                ) : (
                  <>
                    <StyledButton $fontSize="14px" disabled>
                      {adoption.status}!
                    </StyledButton>
                  </>
                )}
              </ButtonGroup>
            </CardForProfile>
          ))}
      </CardContainer>
      <PageCounter
        totalPages={totalPages}
        currentPage={currentPage}
        setCurrentPage={setCurrentPage}
      />

      <Modal
        onClose={() => setShowModal(false)}
        isOpen={showModal}
        title="Solicitação enviada"
      >
        <>
          <p className="text">
            Pedido enviado para {adoption?.animalOwner.name}
          </p>
          <p className="text">
            Motivo: <br /> {adoption?.reason}
          </p>
          <ButtonGroup>
            <StyledButton
              $backgroundColor={theme.colors.highlightColor}
              $fontSize="14px"
              $maxWidth="fit-content"
              onClick={() => {
                mutate({ id: adoption!.id, status: 'cancel' })
                setShowModal(false)
              }}
            >
              Cancelar <i className="fa-solid fa-xmark"></i>
            </StyledButton>
            <StyledButton
              $backgroundColor={theme.colors.highlightColor}
              $fontSize="14px"
              $maxWidth="fit-content"
              onClick={() => {
                setShowEditModal(true)
                setShowModal(false)
              }}
            >
              Editar <i className="fa-solid fa-pen-to-square"></i>
            </StyledButton>
          </ButtonGroup>
        </>
      </Modal>

      <Modal
        onClose={() => setShowEditModal(false)}
        isOpen={showEditModal}
        title="Editar motivo"
      >
        <>
          <InputGroup $fontSize="14px" $borderRadius="6px" $light>
            <label className="text" htmlFor="reason">
              Texto{' '}
              {isError('reason') ? (
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
          </InputGroup>
          {editError && (
            <ErrorMessage>
              <i className="fa-solid fa-circle-exclamation"></i>{' '}
              {editError.response?.data.message}
            </ErrorMessage>
          )}
          <ButtonGroup>
            <StyledButton
              $backgroundColor={theme.colors.highlightColor}
              $fontSize="14px"
              $maxWidth="fit-content"
              onClick={() => {
                setShowEditModal(false)
                form.resetForm()
              }}
            >
              Cancelar <i className="fa-solid fa-xmark"></i>
            </StyledButton>
            <StyledButton
              $backgroundColor={theme.colors.highlightColor}
              $fontSize="14px"
              $maxWidth="fit-content"
              onClick={() => {
                form.handleSubmit()
              }}
            >
              Salvar <i className="fa-solid fa-floppy-disk"></i>
            </StyledButton>
          </ButtonGroup>
        </>
      </Modal>

      <Modal onClose={() => reset()} isOpen={isSuccess} title="Sucesso!">
        <p className="text textCenter">Motivo alterado.</p>
      </Modal>
    </>
  )
}

export default MyRequests
