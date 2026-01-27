import Select, { type SingleValue } from 'react-select'

import { StyledSelect } from './styles'

type Option = {
  value: string | number
  label: string
}

type Props = {
  placeholder: string
  options: Option[]
  value: Option | null
  onChange: (option: SingleValue<Option>) => void
  fontSize?: number
  minWidth?: number
}

const StyledSelectWrapper = ({
  options,
  value,
  onChange,
  placeholder,
  fontSize,
  minWidth
}: Props) => {
  return (
    <StyledSelect $fontSize={fontSize} $minWidth={minWidth}>
      <Select
        classNamePrefix="custom-select"
        value={value}
        onChange={onChange}
        isSearchable={false}
        placeholder={placeholder}
        options={options}
      />
    </StyledSelect>
  )
}

export default StyledSelectWrapper
