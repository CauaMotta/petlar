import styled from 'styled-components'

export const Container = styled.div`
  display: block;
  padding: 16px;
  max-width: 280px;
  width: 100%;
  background-color: ${({ theme }) => theme.colors.secondaryColor};
  border-radius: 16px;

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
    margin-top: 6px;
  }
`
