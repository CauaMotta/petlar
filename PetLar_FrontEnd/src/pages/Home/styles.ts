import styled from 'styled-components'
import { Button } from '../../styles'
import variables from '../../styles/variables'
import type { dogTheme } from '../../themes'

type Props = {
  color: typeof dogTheme
}

export const Container = styled.section`
  padding-block: 32px;

  width: 100%;
  min-height: 100vh;
  padding-inline: 16px;

  .welcome {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;

    .image {
      max-width: 512px;
      max-height: 288px;

      img {
        display: block;
        width: 100%;
        height: 100%;
        object-fit: contain;
        opacity: 0.8;
      }
    }
  }

  .btnGroup {
    margin-top: 16px;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    gap: 16px;
  }

  .info {
    margin-top: 48px;
    width: 100%;
    padding: 16px;
    background-color: ${({ theme }) => theme.colors.secondaryColor};
    border-radius: 16px;
    box-shadow: 0 6px 10px 0 rgba(0, 0, 0, 0.1);
  }

  @media (max-width: ${variables.breakpoints.tablet}) {
    .welcome {
      h1 {
        font-size: 28px;
      }
    }

    .btnGroup {
      button {
        flex: 0 0 45%;
      }
    }

    .info {
      margin-top: 32px;
    }
  }

  @media (max-width: ${variables.breakpoints.mobile}) {
    .welcome {
      h1 {
        font-size: 24px;
      }
    }

    .btnGroup {
      margin-top: 8px;
      gap: 12px;
    }

    .info {
      margin-top: 24px;
    }
  }
`

export const HomeButton = styled(Button)<Props>`
  min-width: 120px;
  background-color: transparent;
  border-radius: 0;
  border: none;
  border-bottom: 2px solid ${({ color }) => color.colors.primaryColor};

  &:hover {
    background-color: transparent;
    border: none;
    border-bottom: 2px solid ${({ color }) => color.colors.primaryColor};

    i {
      color: ${({ color }) => color.colors.primaryColor};
    }
  }
`
