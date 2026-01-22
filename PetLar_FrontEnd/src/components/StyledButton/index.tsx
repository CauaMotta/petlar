import { type JSX } from 'react'

import { Button } from './styles'

type Props = {
  children: JSX.Element
  onClick: () => void
  className?: string
  disabled?: boolean
  backgroundColor?: string
  maxWidth?: string
  fontSize?: string
  paddingBlock?: string
  paddingInline?: string
}

const StyledButton = ({
  children,
  onClick,
  className,
  disabled,
  backgroundColor,
  maxWidth,
  fontSize,
  paddingBlock,
  paddingInline
}: Props) => {
  return (
    <Button
      $backgroundColor={backgroundColor}
      $maxWidth={maxWidth}
      $fontSize={fontSize}
      $paddingBlock={paddingBlock}
      $paddingInline={paddingInline}
      disabled={disabled}
      onClick={onClick}
      className={className}
      type="button"
    >
      {children}
    </Button>
  )
}

export default StyledButton
