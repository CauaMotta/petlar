import styled from 'styled-components'

export const Container = styled.section`
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;

  .loginContainer {
    background-color: ${({ theme }) => theme.colors.secondaryColor};
    padding: 12px 16px;
    border-radius: 16px;
    box-shadow: 0 6px 10px 0 rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
  }

  .registerLink {
    margin-top: 8px;
    display: block;
    text-align: center;
    font-size: 12px;
    line-height: 1.5;
    outline: none;
    text-decoration: none;
    color: ${({ theme }) => theme.colors.fontColor};
  }
`
