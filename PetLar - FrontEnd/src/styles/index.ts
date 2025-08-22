import styled, { createGlobalStyle, keyframes } from 'styled-components'

import variables from './variables'
import { ClipLoader } from 'react-spinners'

export const GlobalStyle = createGlobalStyle`
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    list-style: none;

    font-family: ${variables.fontFamily};
  }

  body {
    color: ${({ theme }) => theme.colors.fontColor};
    background-color: ${({ theme }) => theme.colors.backgroundColor};
  }

  .text {
    font-size: 16px;
    font-weight: 500;

    &--small {
      font-size: 12px;
      line-height: 16px;
    }
  }
`

export const Container = styled.div`
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
`

const spin = keyframes`
  to {
    transform: rotate(360deg);
  }
`

export const StyledClipLoader = styled(ClipLoader)`
  border-width: 4px !important;
  animation: ${spin} 1s 0s linear infinite !important;
`
