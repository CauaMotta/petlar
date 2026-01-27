import styled from 'styled-components'

export const Container = styled.section`
  flex: 1;
  margin-bottom: 48px;

  .navHeader {
    margin-top: 16px;
    display: flex;
    gap: 16px;
    padding-inline: 16px;

    button {
      background-color: transparent;
      border-radius: 8px;
      border: 2px solid transparent;

      font-size: 16px;
      font-weight: 500;
      color: ${({ theme }) => theme.colors.fontColor};

      padding: 4px 16px;

      cursor: pointer;

      &:hover {
        border-color: ${({ theme }) => theme.colors.secondaryColor};
      }

      &.active {
        border-color: ${({ theme }) => theme.colors.highlightColor};
      }
    }

    .btnRegister {
      margin-left: auto;
    }
  }
`
