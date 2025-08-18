import styled, { createGlobalStyle } from 'styled-components'

import variables from './variables'

export const GlobalStyle = createGlobalStyle`
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        list-style: none;

        font-family: ${variables.fontFamily};
    }
`

export const Container = styled.div`
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
`
