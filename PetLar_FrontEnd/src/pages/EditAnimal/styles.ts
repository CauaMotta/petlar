import styled from 'styled-components'

import variables from '../../styles/variables'

export const Container = styled.section`
  padding-inline: 16px;
  margin-top: 32px;
  flex: 1;

  .title--big {
    margin-top: 24px;
  }
`

export const AnimalForm = styled.form`
  margin-top: 16px;
  margin-bottom: 48px;

  display: flex;
  flex-direction: column;
  align-items: center;

  .animalSelect {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-end;
    min-height: 57px;
  }

  .btnReset {
    i {
      transition: transform 0.3s ease;
    }

    &:hover {
      i {
        transform: rotate(90deg);
      }
    }
  }

  #image {
    display: none;
  }

  .imageBtn {
    background-color: ${({ theme }) => theme.colors.secondaryColor};
    border: 1px solid ${({ theme }) => theme.colors.highlightColor};
    min-height: 38px;
    padding-inline: 10px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 4px;
    cursor: pointer;
  }

  @media (max-width: ${variables.breakpoints.tablet}) {
    .animalSelect {
      margin-bottom: 4px;
    }
  }
`
