import styled from 'styled-components'

export const Container = styled.div`
  margin-top: 32px;
  width: 100%;
  padding: 16px 32px;
  display: flex;
  gap: 16px;

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

  .avatar {
    max-width: 144px;
    width: 100%;
    aspect-ratio: 1/1;
    background-color: ${({ theme }) => theme.colors.highlightColor};
    border-radius: 16px;
    display: flex;
    justify-content: center;
    align-items: end;
    overflow: hidden;

    i {
      font-size: 120px;
      line-height: 0.8;
      color: ${({ theme }) => theme.colors.backgroundColor};
    }
  }

  .profileInfo {
    flex: 1;
    padding-top: 8px;

    .text {
      opacity: 0.65;
    }
  }

  form {
    width: 100%;

    small {
      display: inline-block;
      color: darkred;
      max-width: 288px;
    }

    .inputGroup {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      margin-bottom: 6px;

      .text {
        font-size: 14px;
      }

      input {
        width: 100%;
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
      margin-top: 12px;
    }

    .btnDelete {
      background-color: transparent;
      border: none;
      font-size: 12px;
      font-weight: 600;
      color: darkred;
      padding: 4px 8px;
      margin-top: 4px;
      cursor: pointer;
    }
  }
`
