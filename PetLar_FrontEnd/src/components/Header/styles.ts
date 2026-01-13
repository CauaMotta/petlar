import styled from 'styled-components'
import variables from '../../styles/variables'

export const Header = styled.header`
  background-color: ${({ theme }) => theme.colors.backgroundColor};
  border-bottom: 3px solid ${({ theme }) => theme.colors.primaryColor};

  box-shadow: 0 6px 10px 0 rgba(0, 0, 0, 0.1);
  z-index: 1;
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

  .menu {
    height: 21px;
    display: none;
    border: none;
    background: none;
    cursor: pointer;

    .line {
      display: block;
      width: 30px;
      height: 3px;
      background-color: ${({ theme }) => theme.colors.fontColor};
      border-radius: 8px;
      position: relative;
      transition: 0.3s;
    }
  }

  .navBackground {
    display: none;
  }

  @media (max-width: ${variables.breakpoints.tablet}) {
    .menu {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      z-index: 1;
    }

    .navBackground {
      display: block;
      position: fixed;
      top: 0;
      left: 0;
      width: 100vw;
      height: 100vh;
      background-color: ${({ theme }) => theme.colors.primaryColor};
      clip-path: circle(100px at 110% -15%);
      transition: 0.8s ease;
      pointer-events: none;
    }

    .nav {
      position: fixed;
      top: 0;
      left: 0;
      width: 100vw;
      height: 100vh;
      background-color: ${({ theme }) => theme.colors.backgroundColor};
      clip-path: circle(100px at 115% -20%);
      transition: 0.8s ease;

      flex-direction: column;
      justify-content: space-around;
      gap: 0;
      pointer-events: none;

      button {
        font-size: 18px;
        opacity: 0;
      }

      button:nth-child(1) {
        transition: 0.5s 0.2s;
      }

      button:nth-child(2) {
        transition: 0.5s 0.4s;
      }

      button:nth-child(3) {
        transition: 0.5s 0.6s;
      }

      button:nth-child(4) {
        transition: 0.5s 0.8s;
      }
    }

    &.active {
      .menu {
        position: fixed;
        top: 22px;
        right: 16px;

        display: block;

        .line:nth-child(1) {
          display: none;
        }
        .line:nth-child(2) {
          transform: rotate(135deg);
        }
        .line:nth-child(3) {
          top: -3px;
          transform: rotate(-135deg);
        }
      }

      .navBackground {
        clip-path: circle(1500px at 110% -15%);
      }

      .nav {
        clip-path: circle(1500px at 115% -20%);
        pointer-events: all;

        button {
          opacity: 1;
        }
      }
    }
  }
`
