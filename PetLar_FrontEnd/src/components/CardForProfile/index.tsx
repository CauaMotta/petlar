import { type JSX } from 'react'

import { API_URL } from '../../main'
import { Line } from '../../styles'

import { Container } from './styles'

type Props = {
  animal: Animal
  children: JSX.Element
}

const CardForProfile = ({ animal, children }: Props) => (
  <Container>
    <div className="image">
      {animal.imagePath != null ? (
        <img src={API_URL + animal.imagePath} alt={animal.name} />
      ) : (
        <div data-testid="noImage" className="noImage">
          <i className="fa-solid fa-image"></i>
        </div>
      )}
    </div>
    <div className="content">
      <h2>{animal.name}</h2>
      <Line />
      {children}
    </div>
  </Container>
)

export default CardForProfile
