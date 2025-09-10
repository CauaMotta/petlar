const defaultTheme = {
  colors: {
    secondaryColor: '#DDDDDD',
    backgroundColor: '#E2E2E2',
    highlightColor: '#CCCCCC',
    fontColor: '#1F1F1F'
  }
}

export const dogTheme = {
  name: 'dogs',
  colors: {
    ...defaultTheme.colors,
    primaryColor: '#D9A066',
    hoverColor: '#c9925bff'
  }
}

export const catTheme = {
  name: 'cats',
  colors: {
    ...defaultTheme.colors,
    primaryColor: '#95A5A6',
    hoverColor: '#859394ff'
  }
}

export const birdTheme = {
  name: 'birds',
  colors: {
    ...defaultTheme.colors,
    primaryColor: '#06923E',
    hoverColor: '#068338ff'
  }
}

export const otherTheme = {
  name: 'others',
  colors: {
    ...defaultTheme.colors,
    primaryColor: '#0288D1',
    hoverColor: '#0277BD'
  }
}

export const themes = [dogTheme, catTheme, birdTheme, otherTheme]

export const themesMap = Object.fromEntries(themes.map((t) => [t.name, t]))
