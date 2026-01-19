import styled from 'styled-components'

type Props = {
  fontSize?: number
  minWidth?: number
}

export const StyledSelect = styled.div<Props>`
  .custom-select__control {
    min-width: ${(props) => (props.minWidth ? `${props.minWidth}px` : 'auto')};
    font-size: ${(props) => (props.fontSize ? `${props.fontSize}px` : '16px')};
    background-color: ${({ theme }) => theme.colors.secondaryColor};
    border-color: ${({ theme }) => theme.colors.highlightColor};
    border-radius: 0;
    cursor: pointer;

    &--menu-is-open {
    }

    &--is-focused {
      box-shadow: none;

      &:hover {
        border-color: ${({ theme }) => theme.colors.highlightColor};
      }
    }
  }

  .custom-select__menu {
    background-color: ${({ theme }) => theme.colors.backgroundColor};
    font-size: ${(props) => (props.fontSize ? `${props.fontSize}px` : '16px')};
    border-radius: 0;
    margin-top: 0;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  }

  .custom-select__menu-list {
    padding: 0;
  }

  .custom-select__option {
    cursor: pointer;

    &--is-focused {
      background: ${({ theme }) => theme.colors.secondaryColor};
    }

    &--is-selected {
      background: ${({ theme }) => theme.colors.highlightColor};
      color: ${({ theme }) => theme.colors.fontColor};
      font-weight: 500;
    }
  }
`
