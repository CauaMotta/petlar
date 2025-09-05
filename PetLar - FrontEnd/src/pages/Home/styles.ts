import styled from 'styled-components'
import { Button } from '../../styles'
import type { dogTheme } from '../../themes'

type Props = {
  color: typeof dogTheme
}

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  width: 100%;
  height: 100vh;
  padding-inline: 16px;

  .image {
    width: 512px;
    height: 288px;

    img {
      display: block;
      width: 100%;
      height: 100%;
      object-fit: contain;
      opacity: 0.8;
    }
  }

  .btnGroup {
    display: flex;
    gap: 16px;
    margin-top: 16px;
  }

  .info {
    margin-top: 48px;
    width: 100%;
    padding: 16px;
    background-color: ${({ theme }) => theme.colors.secondaryColor};
    border-radius: 16px;
    box-shadow: 0 6px 10px 0 rgba(0, 0, 0, 0.1);

    h2 {
      font-size: 20px;
      font-weight: 600;
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
