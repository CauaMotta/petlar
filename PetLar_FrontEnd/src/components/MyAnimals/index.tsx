import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTheme } from 'styled-components'

import PageCounter from '../PageCounter'
import CardForProfile from '../CardForProfile'
import Modal from '../Modal'

import { useDeleteAnimal, useGetMyAnimals } from '../../hooks/useAnimals'

import { ButtonGroup, CardContainer, StyledButton } from '../../styles'

const MyAnimals = () => {
  const theme = useTheme()
  const navigate = useNavigate()
  const [showDeleteModal, setShowDeleteModal] = useState<boolean>(false)
  const [currentPage, setCurrentPage] = useState<number>(1)
  const { data, totalPages } = useGetMyAnimals({
    page: currentPage - 1,
    size: 4
  })
  const [animal, setAnimal] = useState<Animal>()
  const { mutate } = useDeleteAnimal()

  return (
    <>
      <CardContainer>
        {data.length == 0 && (
          <p className="text">Você ainda não cadastrou nenhum animal!</p>
        )}
        {data.map((animal) => (
          <CardForProfile key={animal.id} animal={animal}>
            <ButtonGroup $marginTop="8px" $flexDirection="column" $gap="4px">
              {animal.status.toLowerCase() !== 'adotado' ? (
                <>
                  <StyledButton
                    $fontSize="14px"
                    onClick={() => navigate('/editAnimal/' + animal.id)}
                  >
                    <i className="fa-solid fa-pen-to-square"></i> Editar
                  </StyledButton>
                  <StyledButton
                    $fontSize="14px"
                    $backgroundColor="#E3C1A3"
                    onClick={() => {
                      setShowDeleteModal(true)
                      setAnimal(animal)
                    }}
                  >
                    <i className="fa-solid fa-ban"></i> Deletar
                  </StyledButton>
                </>
              ) : (
                <>
                  <StyledButton $fontSize="14px" disabled>
                    Adotado!
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
        onClose={() => setShowDeleteModal(false)}
        isOpen={showDeleteModal}
        title={animal?.name ? animal.name : 'Deletar'}
      >
        <div>
          <p className="text textCenter">Você quer excluir este animal?</p>
          <ButtonGroup>
            <StyledButton
              $fontSize="14px"
              $backgroundColor={theme.colors.highlightColor}
              $maxWidth="fit-content"
              onClick={() => {
                mutate(animal!.id)
                setShowDeleteModal(false)
              }}
            >
              Sim
            </StyledButton>
            <StyledButton
              $fontSize="14px"
              $backgroundColor={theme.colors.highlightColor}
              $maxWidth="fit-content"
              onClick={() => setShowDeleteModal(false)}
            >
              Não
            </StyledButton>
          </ButtonGroup>
        </div>
      </Modal>
    </>
  )
}

export default MyAnimals
