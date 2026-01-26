import { useState } from 'react'
import { useTheme } from 'styled-components'

import Modal from '../Modal'
import PageCounter from '../PageCounter'
import Card from '../Card'

import {
  useGetRequestsForMyAnimals,
  useStatusUpdate
} from '../../hooks/useAdoption'

import { ButtonGroup, CardContainer, StyledButton } from '../../styles'

const AdoptionRequests = () => {
  const theme = useTheme()
  const [showModal, setShowModal] = useState<boolean>(false)
  const [currentPage, setCurrentPage] = useState<number>(1)
  const [adoption, setAdoption] = useState<Adoption>()
  const { data, totalPages } = useGetRequestsForMyAnimals({
    page: currentPage - 1,
    size: 4,
    sort: 'status,desc'
  })
  const { mutate } = useStatusUpdate()

  return (
    <>
      <CardContainer>
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
            <Card key={adoption.id} animal={adoption.animal}>
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
            </Card>
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
        title="Solicitação de adoção"
      >
        <>
          <p className="text">
            {adoption?.adopter.name} quer adotar {adoption?.animal.name}.
          </p>
          <p className="text">
            Motivo: <br /> {adoption?.reason}
          </p>
          <ButtonGroup>
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
          </ButtonGroup>
        </>
      </Modal>
    </>
  )
}

export default AdoptionRequests
