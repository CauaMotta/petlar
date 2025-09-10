import { useTheme } from 'styled-components'

import { StyledClipLoader } from './styles'

const Loader = () => {
  const theme = useTheme()

  return (
    <StyledClipLoader data-testid="loader" color={theme.colors.primaryColor} />
  )
}

export default Loader
