import { useNavigate } from 'react-router-dom'
import { useState } from 'react'

import ProfileInfo from '../../components/ProfileInfo'
import MyAnimals from '../../components/MyAnimals'
import AdoptionRequests from '../../components/AdoptionRequests'
import MyRequests from '../../components/MyRequests'

import { Line } from '../../styles'
import { Container } from './styles'

const Profile = () => {
  const [activeTab, setActiveTab] = useState('my-animals')
  const navigate = useNavigate()

  return (
    <Container>
      <ProfileInfo />
      <Line />
      <div className="navHeader">
        <button
          type="button"
          className={activeTab === 'my-animals' ? 'active' : ''}
          onClick={() => setActiveTab('my-animals')}
        >
          Meus Animais
        </button>
        <button
          type="button"
          className={activeTab === 'adoption-requests' ? 'active' : ''}
          onClick={() => setActiveTab('adoption-requests')}
        >
          Solicitações de Adoção
        </button>
        <button
          type="button"
          className={activeTab === 'my-requests' ? 'active' : ''}
          onClick={() => setActiveTab('my-requests')}
        >
          Minhas Solicitações
        </button>
        <button
          type="button"
          className="btnRegister"
          onClick={() => navigate('/registerAnimal')}
        >
          <i className="fa-solid fa-plus"></i> Cadastrar novo animal
        </button>
      </div>
      {activeTab === 'my-animals' && <MyAnimals />}
      {activeTab === 'adoption-requests' && <AdoptionRequests />}
      {activeTab === 'my-requests' && <MyRequests />}
    </Container>
  )
}

export default Profile
