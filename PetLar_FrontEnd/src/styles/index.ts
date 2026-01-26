import styled, { createGlobalStyle } from 'styled-components'

import variables from './variables'

export const GlobalStyle = createGlobalStyle`
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    list-style: none;

    font-family: ${variables.fontFamily};
    -webkit-tap-highlight-color: transparent;
  }

  body {
    color: ${({ theme }) => theme.colors.fontColor};
    background-color: ${({ theme }) => theme.colors.backgroundColor};
  }

  .textCenter {
    text-align: center;
  }

  .pi-8 {
    padding-inline: 8px;
  }

  .title {
    font-size: 20px;
    font-weight: 600;
    line-height: 1;

    &--big {
      font-size: 24px;
      font-weight: 700;
      line-height: 1;
    }

    &--small {
      font-size: 18px;
      font-weight: 600;
      line-height: 1;
    }
  }

  .text {
    font-size: 16px;
    font-weight: 500;

    &--small {
      font-size: 12px;
      line-height: 16px;
    }
  }

  .box {
    margin-block: 16px;
    text-align: center;
    i {
      font-size: 24px;
    }
  }

  @media (max-width: ${variables.breakpoints.tablet}) {
    .title {
      font-size: 20px;

      &--small {
        font-size: 16px;
      }
    }

    .text {
      font-size: 14px;

      &--small {
        font-size: 10px;
      }
    }
  }
`

// Card Container

export const CardContainer = styled.div`
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 16px;

  margin-block: 32px;
`

// Line

export const Line = styled.hr`
  height: 2px;
  background-color: ${({ theme }) => theme.colors.highlightColor};
  border: none;
  margin: 6px 0;
`

// Error

export const ErrorMessage = styled.small`
  max-width: 288px;
  display: inline-block;
  font-size: 12px;
  color: darkred;
`

// Input

type InpGroup = {
  $fontSize?: string
  $light?: boolean
  $borderRadius?: string
  $minHeight?: string
}

export const InputGroup = styled.div<InpGroup>`
  max-width: 512px;
  width: 100%;
  margin-bottom: 6px;
  display: flex;
  flex-direction: column;

  input,
  label,
  textarea {
    font-size: ${({ $fontSize }) => $fontSize || '16px'};
  }

  input,
  textarea {
    min-height: ${({ $minHeight }) => $minHeight || '38px'};
    padding-inline: 10px;
    background-color: ${({ $light, theme }) =>
      $light ? theme.colors.backgroundColor : theme.colors.secondaryColor};
    border: 1px solid ${({ theme }) => theme.colors.highlightColor};
    outline: none;
    border-radius: ${({ $borderRadius }) => $borderRadius || 'none'};
  }

  textarea {
    padding-block: 6px;
    resize: none;
  }
`

// Button Group

type BtnGroup = {
  $marginTop?: string
  $flexDirection?: 'row' | 'column'
  $alignItems?: 'center' | 'flex-end' | 'flex-start'
  $justifyContent?: 'center' | 'flex-end' | 'flex-start'
  $gap?: string
}

export const ButtonGroup = styled.div<BtnGroup>`
  margin-top: ${({ $marginTop }) => $marginTop || '16px'};
  display: flex;
  flex-direction: ${({ $flexDirection }) => $flexDirection || 'row'};
  align-items: ${({ $alignItems }) => $alignItems || 'center'};
  justify-content: ${({ $justifyContent }) => $justifyContent || 'center'};
  gap: ${({ $gap }) => $gap || '8px'};
`

// Button

type Button = {
  $backgroundColor?: string
  $maxWidth?: string
  $fontSize?: string
  $paddingBlock?: string
  $paddingInline?: string
}

export const StyledButton = styled.button<Button>`
  max-width: ${({ $maxWidth }) => $maxWidth || 'none'};
  width: 100%;
  padding-block: ${({ $paddingBlock }) => $paddingBlock || '8px'};
  padding-inline: ${({ $paddingInline }) => $paddingInline || '16px'};

  color: ${({ theme }) => theme.colors.fontColor};
  font-size: ${({ $fontSize }) => $fontSize || '16px'};
  font-weight: 500;
  line-height: 1;

  background-color: ${({ $backgroundColor, theme }) =>
    $backgroundColor || theme.colors.primaryColor};
  border: 1px solid
    ${({ $backgroundColor, theme }) =>
      $backgroundColor || theme.colors.primaryColor};
  border-radius: 8px;

  cursor: pointer;

  &:hover {
    opacity: 0.8;
  }

  &:disabled {
    background-color: ${({ theme }) => theme.colors.highlightColor};
    border-color: ${({ theme }) => theme.colors.highlightColor};
    opacity: 1;
  }
`

// Form

export const AnimalForm = styled.form`
  margin-top: 16px;

  display: flex;
  flex-direction: column;
  align-items: center;

  .selectAnimal {
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

  .btnImage {
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
`
