import styled from 'styled-components'

import { Button } from '../../styles'

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

    .text--small {
      padding-left: 8px;

      b {
        font-weight: 600;
      }
    }

    ${Button} {
      width: 100%;
      margin-top: 16px;

      &:disabled {
        background-color: #ccc;
        border-color: #ccc;
      }
    }
  }
`
