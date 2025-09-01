import styled from 'styled-components'
import { Button, Line } from '../../styles'

export const Container = styled.section`
  padding-inline: 16px;
  margin-top: 32px;

  h2 {
    margin-top: 24px;
    text-align: center;
  }

  .box {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 8px;
    margin-top: 32px;
    line-height: 1;

    i {
      font-size: 32px;
    }
  }
`

export const AnimalForm = styled.form`
  margin-top: 16px;
  margin-bottom: 48px;

  display: flex;
  flex-direction: column;
  align-items: center;

  .subtitle {
    margin-top: 24px;
    font-size: 18px;
    font-weight: 600;
    line-height: 1;
  }

  ${Line} {
    width: 100%;
  }

  small {
    font-size: 12px;
    font-weight: 500;
    color: darkred;
  }

  .animalSelect {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-end;
    min-height: 57px;
  }

  .inputGroup {
    display: flex;
    flex-direction: column;
    max-width: 512px;
    width: 100%;
    margin-bottom: 4px;

    .input-select {
      display: flex;
      gap: 8px;

      .input {
        flex: 1;
      }

      .custom-select__control {
        min-width: 156px;
      }
    }

    .input {
      min-height: 38px;
      padding-inline: 10px;
      background-color: ${({ theme }) => theme.colors.secondaryColor};
      border: 1px solid ${({ theme }) => theme.colors.highlightColor};
      outline: none;
      font-size: 16px;
    }

    textarea.input {
      padding-block: 6px;
      resize: none;
    }
  }

  .btnGroup {
    margin-top: 16px;
    display: flex;
    gap: 8px;

    ${Button} {
      background-color: ${({ theme }) => theme.colors.highlightColor};
      border-color: ${({ theme }) => theme.colors.highlightColor};

      &:hover {
        background-color: ${({ theme }) => theme.colors.primaryColor};
        border-color: ${({ theme }) => theme.colors.primaryColor};

        &:disabled {
          background-color: ${({ theme }) => theme.colors.highlightColor};
          border-color: ${({ theme }) => theme.colors.highlightColor};
        }
      }
      &.reset {
        i {
          transition: transform 0.3s ease;
        }

        &:hover {
          i {
            transform: rotate(90deg);
          }
        }
      }
    }
  }
`

export const StyledSelectWrapper = styled.div`
  .custom-select__control {
    min-width: 256px;
    background-color: ${({ theme }) => theme.colors.secondaryColor};
    border-radius: 0;
    border: 1px solid ${({ theme }) => theme.colors.highlightColor};
    min-height: 38px;
    box-shadow: none;
    transition: border 0.3s ease;
  }

  .custom-select__control--is-focused {
    border-color: ${({ theme }) => theme.colors.highlightColor};

    &:hover {
      border-color: ${({ theme }) => theme.colors.highlightColor};
    }
  }

  .custom-select__menu {
    margin-top: 0;
    border-radius: 0;
    overflow: hidden;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    background-color: ${({ theme }) => theme.colors.backgroundColor};
  }

  .custom-select__option {
    padding: 10px 12px;
    cursor: pointer;
    transition: background 0.2s ease;
  }

  .custom-select__option--is-focused {
    background: ${({ theme }) => theme.colors.secondaryColor};
  }

  .custom-select__option--is-selected {
    background: ${({ theme }) => theme.colors.highlightColor};
    color: ${({ theme }) => theme.colors.fontColor};
  }
`
