import styled from 'styled-components'

export const Container = styled.div`
  margin-top: 32px;
  width: 100%;
  padding: 16px 32px;
  display: flex;
  gap: 16px;

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

    .btnDelete {
      color: darkred;
    }
  }
`
