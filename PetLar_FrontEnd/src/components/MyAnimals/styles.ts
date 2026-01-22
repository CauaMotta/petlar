import styled from 'styled-components'

export const Container = styled.div`
  margin-top: 32px;

  .animals {
    display: flex;
    justify-content: center;
    gap: 16px;

    .btnGroup {
      margin-top: 8px;
      display: flex;
      flex-direction: column;
      gap: 4px;

      button {
        background-color: ${({ theme }) => theme.colors.primaryColor};
        border: 1px solid ${({ theme }) => theme.colors.primaryColor};
        border-radius: 6px;
        width: 100%;
        padding: 4px 0;
        color: ${({ theme }) => theme.colors.fontColor};
        font-size: 14px;
        font-weight: 600;
        cursor: pointer;

        &:hover {
          opacity: 0.8;
        }

        &:disabled {
          background-color: ${({ theme }) => theme.colors.highlightColor};
          border-color: ${({ theme }) => theme.colors.highlightColor};
          opacity: 1;
        }
      }
    }
  }

  .modalContainer {
    display: flex;
    flex-direction: column;
    gap: 8px;

    .btnGroup {
      display: flex;
      align-items: end;
      justify-content: center;
      gap: 6px;

      button {
        padding: 4px 8px;
        color: ${({ theme }) => theme.colors.fontColor};
        font-size: 14px;
        font-weight: 500;
        background-color: ${({ theme }) => theme.colors.highlightColor};
        border: none;
        border-radius: 4px;
        cursor: pointer;

        &:hover {
          opacity: 0.8;
        }
      }
    }
  }

  .pagesContainer {
    margin-top: 16px;
    width: 100%;
    margin-bottom: 16px;

    ul {
      width: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 6px;

      li {
        button {
          color: ${({ theme }) => theme.colors.fontColor};
          background-color: transparent;
          border: none;
          cursor: pointer;
          padding: 4px 6px;
          font-size: 16px;
          line-height: 1;

          &.active {
            font-size: 18px;
            font-weight: 600;
            text-decoration: underline;
          }

          &.navBtn {
            opacity: 0.75;

            &:hover {
              opacity: 1;
            }

            &:disabled {
              opacity: 0.2;
            }
          }
        }
      }
    }
  }
`
