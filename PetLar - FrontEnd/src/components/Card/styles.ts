import styled from 'styled-components'

export const Card = styled.div`
  display: block;
  padding: 16px;
  max-width: 280px;
  width: 100%;
  background-color: ${({ theme }) => theme.colors.secondaryColor};
  border-radius: 16px;

  &:hover {
    box-shadow: 0 6px 10px 0 rgba(0, 0, 0, 0.1);
  }

  .image {
    width: 248px;
    height: 248px;
    border-radius: 8px;
    overflow: hidden;

    background-color: ${({ theme }) => theme.colors.primaryColor};
    border: 2px solid ${({ theme }) => theme.colors.primaryColor};

    .noImage {
      display: flex;
      justify-content: center;
      align-items: center;

      width: 100%;
      height: 100%;

      font-size: 32px;

      background-color: ${({ theme }) => theme.colors.secondaryColor};
    }

    img {
      display: block;
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .content {
    margin-top: 8px;

    h2 {
      font-size: 18px;
      font-weight: 600;
      text-align: center;
      line-height: 1;
    }

    hr {
      height: 2px;
      background-color: ${({ theme }) => theme.colors.primaryColor};
      border: none;
      margin: 6px 0;
    }

    .text--small {
      padding-left: 8px;
    }

    button {
      width: 100%;

      color: ${({ theme }) => theme.colors.fontColor};
      font-size: 16px;
      font-weight: 600;

      background-color: ${({ theme }) => theme.colors.primaryColor};
      border: 2px solid ${({ theme }) => theme.colors.primaryColor};
      border-radius: 8px;

      margin-top: 16px;
      padding-block: 4px;

      cursor: pointer;

      &:hover {
        background-color: ${({ theme }) => theme.colors.hoverColor};
        border-color: ${({ theme }) => theme.colors.hoverColor};
      }
    }
  }
`
