const defaultTheme = {
  colors: {
    backgroundColor: '#1A1A19',
    fontColor: '#EEEEEE'
  }
}

export const dogTheme = {
  name: 'dog',
  colors: {
    ...defaultTheme.colors,
    primaryColor: '#D9A066'
  }
}

export const catTheme = {
  name: 'cat',
  colors: {
    ...defaultTheme.colors,
    primaryColor: '#95A5A6'
  }
}

export const birdTheme = {
  name: 'bird',
  colors: {
    ...defaultTheme.colors,
    primaryColor: '#06923E'
  }
}

export const otherTheme = {
  name: 'other',
  colors: {
    ...defaultTheme.colors,
    primaryColor: '#0277BD'
  }
}

export const themes = [dogTheme, catTheme, birdTheme, otherTheme]

export const themesMap = Object.fromEntries(themes.map((t) => [t.name, t]))
