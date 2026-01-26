import styled from 'styled-components'

import variables from '../../styles/variables'

export const Container = styled.section`
  padding-inline: 16px;
  margin-top: 32px;
  margin-bottom: 32px;
  flex: 1;

  .welcome {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-bottom: 24px;

    .image {
      max-width: 512px;
      width: 100%;

      img {
        display: block;
        width: 100%;
        object-fit: contain;
        aspect-ratio: 16/9;
        opacity: 0.8;
      }
    }
  }

  .filterBox {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-block: 24px;
  }
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
