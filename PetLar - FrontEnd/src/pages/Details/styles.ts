import styled from 'styled-components'

export const Container = styled.section`
  padding-inline: 16px;
  margin-top: 32px;
`

export const Card = styled.div`
  margin-block: 32px;
  width: 100%;

  display: flex;
  gap: 24px;

  .image {
    width: 384px;
    height: 384px;

    background-color: ${({ theme }) => theme.colors.primaryColor};
    border: 2px solid ${({ theme }) => theme.colors.primaryColor};
    border-radius: 16px;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      display: block;
      object-fit: cover;
    }

    .noImage {
      display: flex;
      justify-content: center;
      align-items: center;

      width: 100%;
      height: 100%;

      font-size: 32px;

      background-color: ${({ theme }) => theme.colors.secondaryColor};
    }
  }

  .content {
    flex: 1;

    display: flex;
    flex-direction: column;

    h2 {
      padding-inline: 16px;
      line-height: 1;
    }

    .text {
      color: ${({ theme }) => theme.colors.fontColorSecondary};
      b {
        color: ${({ theme }) => theme.colors.fontColor};
        font-weight: 600;
      }
    }

    .info {
      padding-inline: 16px;
    }

    .contact {
      padding-inline: 16px;
      margin-top: auto;
      width: 100%;

      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 8px;

      p {
        flex: 1;
      }
    }
  }
`

export const Description = styled.div`
  margin-bottom: 40px;

  h3 {
    padding-inline: 16px;
    font-weight: 600;
    line-height: 1;
  }

  p {
    color: ${({ theme }) => theme.colors.fontColorSecondary};
    padding-inline: 16px;
  }
`
