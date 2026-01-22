import styled from 'styled-components'

type Props = {
  $backgroundColor?: string
  $maxWidth?: string
  $fontSize?: string
  $paddingBlock?: string
  $paddingInline?: string
}

export const Button = styled.button<Props>`
  max-width: ${({ $maxWidth }) => $maxWidth || 'none'};
  width: 100%;
  padding-block: ${({ $paddingBlock }) => $paddingBlock || '8px'};
  padding-inline: ${({ $paddingInline }) => $paddingInline || '16px'};

  color: ${({ theme }) => theme.colors.fontColor};
  font-size: ${({ $fontSize }: Props) => $fontSize || '16px'};
  font-weight: 500;
  line-height: 1;

  background-color: ${({ $backgroundColor, theme }) =>
    $backgroundColor || theme.colors.primaryColor};
  border: 1px solid
    ${({ $backgroundColor, theme }) =>
      $backgroundColor || theme.colors.primaryColor};
  border-radius: 8px;

  cursor: pointer;

  &:hover {
    opacity: 0.8;
  }

  &:disabled {
    background-color: ${({ theme }) => theme.colors.highlightColor};
    border-color: ${({ theme }) => theme.colors.highlightColor};
    opacity: 1;
  }
`
