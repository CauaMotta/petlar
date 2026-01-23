import { useState } from 'react'
import { useTheme } from 'styled-components'

import CardForProfile from '../CardForProfile'
import Modal from '../Modal'

import {
  useGetRequestsForMyAnimals,
  useStatusUpdate
} from '../../hooks/useAdoption'

import { Container } from './styles'
import { StyledButton } from '../../styles'

const AdoptionRequests = () => {
  const theme = useTheme()
  const [showModal, setShowModal] = useState<boolean>(false)
  const [currentPage, setCurrentPage] = useState<number>(1)
  const [adoption, setAdoption] = useState<Adoption>()
  const { data, totalPages } = useGetRequestsForMyAnimals({
    page: currentPage - 1,
    size: 4
  })
  const { mutate } = useStatusUpdate()

  const handlePageClick = (pageNumber: number) => {
    window.scrollTo({ top: 0, behavior: 'smooth' })
    setCurrentPage(pageNumber)
  }

  return (
    <Container>
      <div className="animals">
        {data.length == 0 && (
          <p className="text">Você ainda não cadastrou nenhum animal!</p>
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
        title="Solicitação de adoção"
      >
        <div className="modalContainer">
          <p>
            {adoption?.adopter.name} quer adotar {adoption?.animal.name}.
          </p>
          <p>
            Motivo: <br /> {adoption?.reason}
          </p>
          <div className="btnGroup">
            <StyledButton
              $backgroundColor={theme.colors.highlightColor}
              $maxWidth="fit-content"
              $fontSize="14px"
              onClick={() => {
                mutate({ id: adoption!.id, status: 'deny' })
                setShowModal(false)
              }}
            >
              Recusar <i className="fa-solid fa-xmark"></i>
            </StyledButton>
            <StyledButton
              $backgroundColor={theme.colors.highlightColor}
              $maxWidth="fit-content"
              $fontSize="14px"
              onClick={() => {
                mutate({ id: adoption!.id, status: 'accept' })
                setShowModal(false)
              }}
            >
              Aprovar <i className="fa-solid fa-check"></i>
            </StyledButton>
          </div>
        </div>
      </Modal>
    </Container>
  )
}

export default AdoptionRequests
