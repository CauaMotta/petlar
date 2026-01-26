import styled from 'styled-components'

export const Container = styled.div`
  width: 100%;
  margin-bottom: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;

  button {
    color: ${({ theme }) => theme.colors.fontColor};
    background-color: transparent;
    border: none;
    cursor: pointer;
    padding: 4px 6px;
    font-size: 16px;
    line-height: 1;

    &.active {
      font-size: 18px;
      font-weight: 600;
      text-decoration: underline;
    }

    &.navBtn {
      opacity: 0.75;

      &:hover {
        opacity: 1;
      }

      &:disabled {
        opacity: 0.2;
      }
    }
  }
`
