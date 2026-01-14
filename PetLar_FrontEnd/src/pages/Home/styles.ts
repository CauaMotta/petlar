import styled from 'styled-components'

import variables from '../../styles/variables'

export const Container = styled.section`
  padding-inline: 16px;
  margin-top: 32px;
  margin-bottom: 32px;

  .filterBox {
    display: flex;
    align-items: center;
    gap: 16px;
    margin-bottom: 24px;
  }
`

export const CardContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;

  margin: 32px 0;
`

export const CardInfo = styled.div`
  margin-block: 48px;
  margin-inline: 16px;
  padding: 16px;
  background-color: ${({ theme }) => theme.colors.secondaryColor};
  border-radius: 16px;
  box-shadow: 0 6px 10px 0 rgba(0, 0, 0, 0.1);

  @media (max-width: ${variables.breakpoints.tablet}) {
    margin-block: 32px;
  }

  @media (max-width: ${variables.breakpoints.mobile}) {
    margin-block: 24px;
  }
`

export const StyledSelectWrapper = styled.div`
  .custom-select__control {
    width: auto;
    min-width: 120px;
    min-height: 38px;
    background-color: ${({ theme }) => theme.colors.secondaryColor};
    border-radius: 0;
    border: 1px solid ${({ theme }) => theme.colors.highlightColor};
    box-shadow: none;
    transition: border 0.3s ease;
    font-size: 12px;
  }

  .custom-select__control--is-focused {
    border-color: ${({ theme }) => theme.colors.highlightColor};

    &:hover {
      border-color: ${({ theme }) => theme.colors.highlightColor};
    }
  }

  .custom-select__value-container {
    padding: 2px 8px;
  }

  .custom-select__single-value {
    margin: 0;
  }

  .custom-select__indicator {
    padding: 4px;
  }

  .custom-select__menu {
    margin-top: 0;
    border-radius: 0;
    overflow: hidden;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    background-color: ${({ theme }) => theme.colors.backgroundColor};
    font-size: 12px;
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
