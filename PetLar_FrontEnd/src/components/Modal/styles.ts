import styled from 'styled-components'

export const ModalContainer = styled.div`
  width: 100vw;
  height: 100vh;

  z-index: 1;

  display: flex;
  justify-content: center;
  align-items: center;

  position: fixed;
  top: 0;
  left: 0;
`

export const ModalCard = styled.div`
  max-width: 992px;

  min-width: 320px;
  min-height: 120px;

  position: relative;
  z-index: 1;

  background-color: ${({ theme }) => theme.colors.backgroundColor};

  padding: 16px;
  border-radius: 16px;

  text-align: center;
  line-height: 1.3;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .close {
      background: transparent;
      border: none;

      color: ${({ theme }) => theme.colors.fontColor};
      font-size: 24px;

      cursor: pointer;

      &:hover {
        color: ${({ theme }) => theme.colors.fontColor};
      }
    }
  }
`

export const Overlay = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
`
