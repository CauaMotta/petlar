import { ClipLoader } from 'react-spinners'
import styled, { keyframes } from 'styled-components'

const spin = keyframes`
  to {
    transform: rotate(360deg);
  }
`

export const StyledClipLoader = styled(ClipLoader)`
  border-width: 4px !important;
  animation: ${spin} 1s 0s linear infinite !important;
`
