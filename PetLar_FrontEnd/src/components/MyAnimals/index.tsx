import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

import CardForProfile from '../CardForProfile'

import { useDeleteAnimal, useGetMyAnimals } from '../../hooks/useAnimals'

import Modal from '../Modal'

import { Container } from './styles'

const MyAnimals = () => {
  const navigate = useNavigate()
  const [showDeleteModal, setShowDeleteModal] = useState<boolean>(false)
  const [currentPage, setCurrentPage] = useState<number>(1)
  const { data, totalPages } = useGetMyAnimals({
    page: currentPage - 1,
    size: 4
  })
  const [animal, setAnimal] = useState<Animal>()
  const { mutate } = useDeleteAnimal()

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
        {data.map((animal) => (
          <CardForProfile key={animal.id} animal={animal}>
            <div className="btnGroup">
              {animal.status.toLowerCase() !== 'adotado' ? (
                <>
                  <button
                    type="button"
                    onClick={() => navigate('/editAnimal/' + animal.id)}
                  >
                    <i className="fa-solid fa-pen-to-square"></i> Editar
                  </button>
                  <button
                    type="button"
                    onClick={() => {
                      setShowDeleteModal(true)
                      setAnimal(animal)
                    }}
                    style={{
                      borderColor: '#E3C1A3',
                      backgroundColor: '#E3C1A3'
                    }}
                  >
                    <i className="fa-solid fa-ban"></i> Deletar
                  </button>
                </>
              ) : (
                <>
                  <button type="button" disabled>
                    Adotado!
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
        onClose={() => setShowDeleteModal(false)}
        isOpen={showDeleteModal}
        title={animal?.name ? animal.name : 'Deletar'}
      >
        <div className="modalContainer">
          <p>Você quer excluir este animal?</p>
          <div className="btnGroup">
            <button
              type="button"
              onClick={() => {
                mutate(animal!.id)
                setShowDeleteModal(false)
              }}
            >
              Sim
            </button>
            <button type="button" onClick={() => setShowDeleteModal(false)}>
              Não
            </button>
          </div>
        </div>
      </Modal>
    </Container>
  )
}

export default MyAnimals
