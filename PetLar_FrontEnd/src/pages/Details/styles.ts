import styled from 'styled-components'
import variables from '../../styles/variables'

export const Container = styled.section`
  padding-inline: 16px;
  margin-top: 32px;
  flex: 1;
`

export const Card = styled.div`
  margin-block: 32px;
  width: 100%;

  display: flex;
  gap: 24px;

  .image {
    width: 100%;
    max-width: 384px;
    aspect-ratio: 1/1;

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

    .title {
      padding-inline: 16px;
      line-height: 1;
    }

    .text {
      b {
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
      align-items: center;
      gap: 8px;

      p {
        flex: 1;
      }
    }
  }

  @media (max-width: ${variables.breakpoints.desktop_sm}) {
    .content {
      .contact {
        flex-direction: column;
        align-items: flex-start;
        gap: 4px;
      }
    }
  }

  @media (max-width: ${variables.breakpoints.tablet}) {
    flex-direction: column;
    .image {
      margin: 0 auto;
    }

    .content {
      .title {
        text-align: center;
      }

      .contact {
        margin-top: 16px;
        flex-direction: row;
        align-items: center;
      }
    }
  }

  @media (max-width: ${variables.breakpoints.mobile}) {
    .content {
      .contact {
        flex-direction: column;
        align-items: flex-start;
        gap: 4px;
      }
    }
  }
`

export const Description = styled.div`
  margin-bottom: 40px;

  .title--small {
    padding-inline: 16px;
    line-height: 1;
  }

  .text {
    padding-inline: 16px;
  }
`
