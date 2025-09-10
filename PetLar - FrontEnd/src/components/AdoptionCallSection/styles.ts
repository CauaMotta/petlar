import styled from 'styled-components'
import variables from '../../styles/variables'

export const CallSection = styled.section`
  margin-bottom: 48px;
  padding-inline: 16px;

  display: flex;
  gap: 16px;

  .image {
    width: 256px;
    height: 164px;

    display: flex;
    justify-content: center;
    align-items: center;

    img {
      display: block;
      width: 100%;
      height: 100%;
      object-fit: contain;
      opacity: 0.8;
    }
  }

  .call {
    flex: 1;

    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .title {
      line-height: 1;
      font-weight: 700;
    }

    .text {
      line-height: 1;
    }
  }

  @media (max-width: ${variables.breakpoints.tablet}) {
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 0;

    .call {
      .text {
        margin-block: 8px;
      }
    }
  }
`
