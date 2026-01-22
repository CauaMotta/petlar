import { useState } from 'react'
import { useFormik } from 'formik'
import * as Yup from 'yup'

import CardForProfile from '../CardForProfile'

import {
  useEditReason,
  useGetMyRequests,
  useStatusUpdate
} from '../../hooks/useAdoption'

import Modal from '../Modal'

import { Container } from './styles'

const MyRequests = () => {
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

  const handlePageClick = (pageNumber: number) => {
    window.scrollTo({ top: 0, behavior: 'smooth' })
    setCurrentPage(pageNumber)
  }

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

  return (
    <Container>
      <div className="animals">
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
              <div className="btnGroup">
                {adoption.status.toLowerCase() === 'pendente' ? (
                  <>
                    <button
                      type="button"
                      onClick={() => {
                        setShowModal(true)
                        setAdoption(adoption)
                      }}
                    >
                      <i className="fa-solid fa-eye"></i> Visualizar
                    </button>
                  </>
                ) : (
                  <>
                    <button type="button" disabled>
                      {adoption.status}!
                    </button>
                  </>
                )}
              </div>
            </CardForProfile>
          ))}
      </div>
      {!!totalPages && totalPages > 1 && (
        <div className="pagesContainer">
          <ul>
            <li>
              <button
                disabled={currentPage === 1}
                onClick={() => handlePageClick(currentPage - 1)}
                className="navBtn"
              >
                <i className="fa-solid fa-chevron-left"></i>
              </button>
            </li>
            {Array.from({ length: totalPages }, (_, i) => (
              <li key={i + 1}>
                <button
                  className={currentPage === i + 1 ? 'active' : ''}
                  onClick={() => handlePageClick(i + 1)}
                >
                  {i + 1}
                </button>
              </li>
            ))}
            <li>
              <button
                disabled={currentPage === totalPages}
                onClick={() => handlePageClick(currentPage + 1)}
                className="navBtn"
              >
                <i className="fa-solid fa-chevron-right"></i>
              </button>
            </li>
          </ul>
        </div>
      )}

      <Modal
        onClose={() => setShowModal(false)}
        isOpen={showModal}
        title="Solicitação enviada"
      >
        <div className="modalContainer">
          <p>Pedido enviado para {adoption?.animalOwner.name}</p>
          <p>
            Motivo: <br /> {adoption?.reason}
          </p>
          <div className="btnGroup">
            <button
              type="button"
              onClick={() => {
                mutate({ id: adoption!.id, status: 'cancel' })
                setShowModal(false)
              }}
            >
              Cancelar <i className="fa-solid fa-xmark"></i>
            </button>
            <button
              type="button"
              onClick={() => {
                setShowEditModal(true)
                setShowModal(false)
              }}
            >
              Editar <i className="fa-solid fa-pen-to-square"></i>
            </button>
          </div>
        </div>
      </Modal>

      <Modal
        onClose={() => setShowEditModal(false)}
        isOpen={showEditModal}
        title="Editar motivo"
      >
        <div className="modalContainer">
          <div className="inputGroup">
            <label className="text" htmlFor="reason">
              Texto{' '}
              {isError('reason') ? <small>* {form.errors.reason}</small> : ''}
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
          {editError && (
            <small>
              <i className="fa-solid fa-circle-exclamation"></i>{' '}
              {editError.response?.data.message}
            </small>
          )}
          <div className="btnGroup">
            <button
              type="button"
              onClick={() => {
                setShowEditModal(false)
                form.resetForm()
              }}
            >
              Cancelar <i className="fa-solid fa-xmark"></i>
            </button>
            <button
              type="button"
              onClick={() => {
                form.handleSubmit()
                setShowEditModal(false)
              }}
            >
              Salvar <i className="fa-solid fa-floppy-disk"></i>
            </button>
          </div>
        </div>
      </Modal>

      <Modal onClose={() => reset()} isOpen={isSuccess} title="Sucesso!">
        <div className="modalContainer">
          <p style={{ textAlign: 'center' }}>Motivo alterado.</p>
        </div>
      </Modal>
    </Container>
  )
}

export default MyRequests
