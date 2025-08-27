import styled from 'styled-components'

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

    h2 {
      line-height: 1;
      margin-bottom: 16px;

      b {
        font-weight: 700;
        font-size: 20px;
      }
    }

    p {
      line-height: 1;
    }

    .btnContainer {
      margin-top: auto;
    }
  }
`
