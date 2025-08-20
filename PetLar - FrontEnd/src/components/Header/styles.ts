import styled from 'styled-components'

type Props = {
  icon: string
}

export const Header = styled.header`
  background-color: ${({ theme }) => theme.colors.backgroundColor};
  border-bottom: 3px solid ${({ theme }) => theme.colors.primaryColor};

  box-shadow: 0 6px 10px 0 rgba(0, 0, 0, 0.1);
`

export const Container = styled.div`
  max-width: 1200px;
  width: 100%;
  height: 60px;

  padding-inline: 16px;
  margin: 0 auto;

  display: flex;
  justify-content: space-between;
  align-items: center;

  .logo {
    display: flex;
    align-items: center;
    gap: 8px;

    cursor: pointer;

    h1 {
      font-size: 24px;
    }
  }

  .nav {
    display: flex;
    align-items: center;
    gap: 16px;

    button {
      color: ${({ theme }) => theme.colors.fontColor};
      font-size: 16px;
      font-weight: 600;

      background-color: transparent;
      border: none;

      display: flex;
      align-items: center;
      gap: 4px;

      cursor: pointer;
    }
  }
`

export const Icon = styled.span<Props>`
  display: block;
  width: 24px;
  height: 24px;
  background-image: url('/assets/icons/dog.png');
  background-position: center;
  background-repeat: no-repeat;
  background-size: contain;
`
