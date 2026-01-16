import styled from 'styled-components'

export const Container = styled.section`
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;

  small {
    color: darkred;
  }

  .errorMessage {
    text-align: center;
  }

  .loginContainer {
    background-color: ${({ theme }) => theme.colors.secondaryColor};
    padding: 12px 16px;
    border-radius: 16px;
    box-shadow: 0 6px 10px 0 rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;

    .inputGroup {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      margin-bottom: 6px;

      .text {
        font-size: 14px;
      }

      input {
        min-height: 28px;
        padding-inline: 8px;
        background-color: ${({ theme }) => theme.colors.backgroundColor};
        border: 1px solid ${({ theme }) => theme.colors.highlightColor};
        outline: none;
        font-size: 14px;
        border-radius: 6px;
      }
    }

    .btnGroup {
      margin-top: auto;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-top: 16px;

      button {
        width: 100%;
      }
    }
  }

  .registerLink {
    margin-top: 8px;
    display: block;
    text-align: center;
    font-size: 12px;
    line-height: 1.5;
    outline: none;
    text-decoration: none;
    color: ${({ theme }) => theme.colors.fontColor};
  }
`
