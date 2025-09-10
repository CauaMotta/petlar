import 'styled-components'
import { dogTheme } from './themes'

type Theme = typeof dogTheme

declare module 'styled-components' {
  // eslint-disable-next-line @typescript-eslint/no-empty-object-type
  export interface DefaultTheme extends Theme {}
}
