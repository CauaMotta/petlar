import { Container } from './styles'

type Props = {
  totalPages?: number
  currentPage: number
  setCurrentPage: (page: number) => void
}

const PageCounter = ({ totalPages, currentPage, setCurrentPage }: Props) => {
  const handlePageClick = (pageNumber: number) => {
    window.scrollTo({ top: 0, behavior: 'smooth' })
    setCurrentPage(pageNumber)
  }

  if (!totalPages || totalPages <= 1) return null

  return (
    <Container>
      <button
        aria-label="previous-page"
        disabled={currentPage === 1}
        onClick={() => handlePageClick(currentPage - 1)}
        className="navBtn"
      >
        <i className="fa-solid fa-chevron-left"></i>
      </button>
      {Array.from({ length: totalPages! }, (_, i) => (
        <button
          key={i + 1}
          className={currentPage === i + 1 ? 'active' : ''}
          onClick={() => handlePageClick(i + 1)}
        >
          {i + 1}
        </button>
      ))}
      <button
        aria-label="next-page"
        disabled={currentPage === totalPages}
        onClick={() => handlePageClick(currentPage + 1)}
        className="navBtn"
      >
        <i className="fa-solid fa-chevron-right"></i>
      </button>
    </Container>
  )
}

export default PageCounter
