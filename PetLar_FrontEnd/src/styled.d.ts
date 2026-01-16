import 'styled-components'
import { principalTheme } from './themes'

type Theme = typeof principalTheme

declare module 'styled-components' {
  // eslint-disable-next-line @typescript-eslint/no-empty-object-type
  export interface DefaultTheme extends Theme {}
}
