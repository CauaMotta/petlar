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

export const Line = styled.hr`
  height: 2px;
  background-color: ${({ theme }) => theme.colors.highlightColor};
  border: none;
  margin: 6px 0;
`

export const Button = styled.button`
  padding: 8px 16px;

  color: ${({ theme }) => theme.colors.fontColor};
  font-weight: 500;
  font-size: 16px;
  line-height: 1;

  background-color: ${({ theme }) => theme.colors.primaryColor};
  border: 2px solid ${({ theme }) => theme.colors.primaryColor};
  border-radius: 8px;

  cursor: pointer;

  &:hover {
    background-color: ${({ theme }) => theme.colors.hoverColor};
    border: 2px solid ${({ theme }) => theme.colors.hoverColor};
  }
`
