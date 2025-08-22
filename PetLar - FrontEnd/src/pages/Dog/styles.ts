import styled from 'styled-components'

export const Container = styled.div`
  padding-inline: 16px;
  margin-top: 32px;

  .text {
    margin-bottom: 32px;
  }

  .error {
    text-align: center;
    i {
      font-size: 24px;
    }
  }
`

export const CardContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
`
