import styled from 'styled-components'

import variables from '../../styles/variables'

export const Container = styled.section`
  padding-inline: 16px;
  margin-top: 32px;
  margin-bottom: 32px;
`

export const CardContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;

  margin: 32px 0;
`

export const CardInfo = styled.div`
  margin-block: 48px;
  margin-inline: 16px;
  padding: 16px;
  background-color: ${({ theme }) => theme.colors.secondaryColor};
  border-radius: 16px;
  box-shadow: 0 6px 10px 0 rgba(0, 0, 0, 0.1);

  @media (max-width: ${variables.breakpoints.tablet}) {
    margin-block: 32px;
  }

  @media (max-width: ${variables.breakpoints.mobile}) {
    margin-block: 24px;
  }
`
