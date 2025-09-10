import styled from 'styled-components'
import variables from '../../styles/variables'

export const Footer = styled.footer`
  background-color: ${({ theme }) => theme.colors.backgroundColor};
  border-top: 3px solid ${({ theme }) => theme.colors.primaryColor};

  box-shadow: 0 -6px 10px 0 rgba(0, 0, 0, 0.1);
`

export const Container = styled.div`
  max-width: 1200px;
  width: 100%;

  text-align: center;
  padding: 24px 16px;
  margin: 0 auto;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 24px;

  .logo {
    display: flex;
    gap: 16px;

    hr {
      width: 2px;
      background-color: ${({ theme }) => theme.colors.highlightColor};
      border: none;
      transform: rotate(5deg);
    }
  }

  .petlar {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
  }

  .ocauamotta {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
  }

  @media (max-width: ${variables.breakpoints.tablet}) {
    gap: 16px;
  }
`
