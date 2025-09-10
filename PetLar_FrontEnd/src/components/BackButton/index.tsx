import { useNavigate, type To } from 'react-router-dom'
import { Button } from './styles'

type Props = {
  path: To | number
}

const BackButton = ({ path }: Props) => {
  const navigate = useNavigate()

  const handleClick = () => {
    if (typeof path === 'number') {
      navigate(path)
    } else {
      navigate(path)
    }
  }

  return (
    <Button onClick={handleClick}>
      <i className="fa-solid fa-arrow-left"></i> Voltar a página anterior
    </Button>
  )
}

export default BackButton
