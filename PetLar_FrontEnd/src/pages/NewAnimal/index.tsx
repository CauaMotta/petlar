import { useState } from 'react'
import Select from 'react-select'
import { useFormik } from 'formik'
import * as Yup from 'yup'
import { IMaskInput } from 'react-imask'

import BackButton from '../../components/BackButton'
import Loader from '../../components/Loader'

import { useApiMutation } from '../../hooks/useApiMutation'

import { AnimalForm, Container, StyledSelectWrapper } from './styles'
import { Button, Line } from '../../styles'

const options = [
  { value: 'dogs', label: 'Cachorro' },
  { value: 'cats', label: 'Gato' },
  { value: 'birds', label: 'Ave' },
  { value: 'others', label: 'Outro' }
]

const sexOptions = [
  { value: 'macho', label: 'Macho' },
  { value: 'fêmea', label: 'Fêmea' }
]

const sizeOptions = [
  { value: 'pequeno', label: 'Pequeno' },
  { value: 'médio', label: 'Médio' },
  { value: 'grande', label: 'Grande' }
]

const ageUnitOptions = [
  { value: 'months', label: 'Meses' },
  { value: 'years', label: 'Anos' }
]

const NewAnimal = () => {
  const [ageUnit, setAgeUnit] = useState<'months' | 'years'>('years')
  const { mutate, data, loading, error } = useApiMutation<
    CreateAnimal,
    Animal
  >()

  const form = useFormik({
    initialValues: {
      type: '',
      name: '',
      age: '',
      breed: '',
      sex: 'macho',
      weight: null,
      size: 'pequeno',
      urlImage: '',
      description: '',
      author: '',
      phone: ''
    },
    validationSchema: Yup.object({
      type: Yup.string().required('Este campo é obrigatório'),
      name: Yup.string()
        .min(2, 'O nome deve ter no mínimo 2 caracteres')
        .required('Este campo é obrigatório'),
      age: Yup.string()
        .required('Este campo é obrigatório')
        .test('greater-than-zero', 'A idade deve ser maior que 0', (value) => {
          if (!value) return false
          return parseInt(value) > 0
        }),
      breed: Yup.string()
        .min(3, 'A raça deve ter no mínimo 3 caracteres')
        .required('Este campo é obrigatório'),
      sex: Yup.string().required('Este campo é obrigatório'),
      weight: Yup.string()
        .required('Este campo é obrigatório')
        .test('greater-than-zero', 'O peso deve ser maior que 0', (value) => {
          if (!value) return false
          return parseFloat(value) > 0
        }),
      size: Yup.string().required('Este campo é obrigatório'),
      urlImage: Yup.string(),
      description: Yup.string(),
      author: Yup.string()
        .min(3, 'O nome deve ter no mínimo 3 caracteres')
        .required('Este campo é obrigatório'),
      phone: Yup.string()
        .required('Este campo é obrigatório')
        .matches(/^\d{11}$/, 'O telefone deve ter 11 dígitos (DDD + número)')
    }),
    onSubmit: async (values) => {
      const { type, ...rest } = values

      let ageInMonths = Number(values.age)
      if (ageUnit === 'years') {
        ageInMonths = ageInMonths * 12
      }

      const payload: CreateAnimal = {
        ...rest,
        age: ageInMonths
      }

      try {
        await mutate({
          method: 'POST',
          endpoint: `/${type}`,
          body: payload
        })
      } catch (err) {
        console.error('Erro ao criar animal', err)
      }
    },
    validateOnMount: true
  })

  const isError = (fieldName: string) => {
    const isTouched = fieldName in form.touched
    const isInvalid = fieldName in form.errors

    if (isTouched && isInvalid) return true
    return false
  }

  if (loading)
    return (
      <Container>
        <BackButton path={-1} />
        <h2 className="title">Cadastre um Pet</h2>
        <Line />
        <div className="box">
          <Loader />
          <p className="text">
            Estamos cadastrando o seu bixinho, aguarde um pouquinho...
          </p>
        </div>
      </Container>
    )

  if (error)
    return (
      <Container>
        <BackButton path={-1} />
        <h2 className="title">Cadastre um Pet</h2>
        <Line />
        <div className="box">
          <i className="fa-solid fa-triangle-exclamation"></i>
          <p className="text">
            Parece que tivemos um problema ao fazer o cadastro, tente novamente
            mais tarde!
          </p>
        </div>
      </Container>
    )

  if (data)
    return (
      <Container>
        <BackButton path={-1} />
        <h2 className="title">Cadastre um Pet</h2>
        <Line />
        <div className="box">
          <i className="fa-solid fa-check"></i>
          <p className="text">
            Seu bixinho foi cadastrado com sucesso. Obrigado por tornar o mundo
            um lugar melhor!
          </p>
        </div>
      </Container>
    )

  return (
    <Container>
      <BackButton path={-1} />
      <h2 className="title">Cadastre um Pet</h2>
      <Line />
      <p className="text">
        Que bom ver você aqui! Qual bixinho quer cadastrar?
      </p>
      <AnimalForm>
        <div className="animalSelect">
          {isError('type') ? <small>* {form.errors.type}</small> : ''}
          <StyledSelectWrapper>
            <Select
              classNamePrefix="custom-select"
              value={
                options.find((option) => option.value === form.values.type) ||
                null
              }
              onChange={(option) =>
                form.setFieldValue('type', option?.value || null)
              }
              isSearchable={false}
              placeholder="Selecione um animal"
              options={options}
            />
          </StyledSelectWrapper>
        </div>
        <div className="inputGroup">
          <label className="text" htmlFor="name">
            Nome: {isError('name') ? <small>* {form.errors.name}</small> : ''}
          </label>
          <input
            id="name"
            placeholder="Bob"
            className="input"
            type="text"
            value={form.values.name}
            onChange={form.handleChange}
            onBlur={form.handleBlur}
          />
        </div>
        <div className="inputGroup">
          <label className="text" htmlFor="age">
            Idade: {isError('age') ? <small>* {form.errors.age}</small> : ''}
          </label>
          <div className="input-select">
            <IMaskInput
              id="age"
              mask={Number}
              scale={0}
              unmask={true}
              placeholder={`2 ${
                ageUnitOptions.find((opt) => opt.value === ageUnit)?.label
              }`}
              className="input"
              value={form.values.age}
              onAccept={(value) => form.setFieldValue('age', value)}
              onBlur={form.handleBlur}
            />
            <StyledSelectWrapper>
              <Select
                classNamePrefix="custom-select"
                value={ageUnitOptions.find((opt) => opt.value === ageUnit)}
                onChange={(option) =>
                  setAgeUnit(option?.value as 'months' | 'years')
                }
                isSearchable={false}
                placeholder="Selecione"
                options={ageUnitOptions}
              />
            </StyledSelectWrapper>
          </div>
        </div>
        <div className="inputGroup">
          <label className="text" htmlFor="breed">
            Raça: {isError('breed') ? <small>* {form.errors.breed}</small> : ''}
          </label>
          <input
            id="breed"
            placeholder="Vira-Lata"
            className="input"
            type="text"
            value={form.values.breed}
            onChange={form.handleChange}
            onBlur={form.handleBlur}
          />
        </div>
        <div className="inputGroup">
          <label className="text">Sexo:</label>
          <StyledSelectWrapper>
            <Select
              classNamePrefix="custom-select"
              value={sexOptions.find(
                (option) => option.value === form.values.sex
              )}
              onChange={(option) => form.setFieldValue('sex', option?.value)}
              isSearchable={false}
              placeholder="Selecione..."
              options={sexOptions}
            />
          </StyledSelectWrapper>
        </div>
        <div className="inputGroup">
          <label className="text" htmlFor="weight">
            Peso:{' '}
            {isError('weight') ? <small>* {form.errors.weight}</small> : ''}
          </label>
          <IMaskInput
            id="weight"
            mask={Number}
            scale={2}
            radix="."
            unmask={true}
            min={-1}
            placeholder="2 Kg"
            className="input"
            value={
              form.values.weight !== null
                ? String(form.values.weight / 100)
                : ''
            }
            onAccept={(value) => {
              const intWeight = value ? Math.round(Number(value) * 100) : null
              form.setFieldValue('weight', intWeight)
            }}
            onBlur={form.handleBlur}
          />
        </div>
        <div className="inputGroup">
          <label className="text" htmlFor="">
            Porte:
          </label>
          <StyledSelectWrapper>
            <Select
              classNamePrefix="custom-select"
              value={sizeOptions.find(
                (option) => option.value === form.values.size
              )}
              onChange={(option) => form.setFieldValue('size', option?.value)}
              isSearchable={false}
              placeholder="Selecione..."
              options={sizeOptions}
            />
          </StyledSelectWrapper>
        </div>
        <div className="inputGroup">
          <label className="text" htmlFor="urlImage">
            Adicionar URL para imagem:
          </label>
          <input
            id="urlImage"
            placeholder="foto.com/bob"
            className="input"
            type="text"
            value={form.values.urlImage}
            onChange={form.handleChange}
            onBlur={form.handleBlur}
          />
        </div>
        <div className="inputGroup">
          <label className="text" htmlFor="description">
            Adicionar uma descrição:
          </label>
          <textarea
            id="description"
            placeholder="Fofo e carente"
            className="input"
            rows={3}
            maxLength={250}
            value={form.values.description}
            onChange={form.handleChange}
            onBlur={form.handleBlur}
          />
        </div>
        <h3 className="subtitle">Suas Informações</h3>
        <Line />
        <div className="inputGroup">
          <label className="text" htmlFor="author">
            Nome:{' '}
            {isError('author') ? <small>* {form.errors.author}</small> : ''}
          </label>
          <input
            id="author"
            placeholder="Digite seu nome"
            className="input"
            type="text"
            value={form.values.author}
            onChange={form.handleChange}
            onBlur={form.handleBlur}
          />
        </div>
        <div className="inputGroup">
          <label className="text" htmlFor="phone">
            Whatsapp para contato:{' '}
            {isError('phone') ? <small>* {form.errors.phone}</small> : ''}
          </label>
          <IMaskInput
            id="phone"
            mask="(00) 0 0000-0000"
            unmask={true}
            className="input"
            placeholder="(99) 9 9999-9999"
            value={form.values.phone}
            onAccept={(value) => form.setFieldValue('phone', value)}
            onBlur={form.handleBlur}
          />
        </div>
        <div className="btnGroup">
          <Button
            className="reset"
            type="button"
            onClick={() => form.resetForm()}
          >
            Resetar <i className="fa-solid fa-rotate"></i>
          </Button>
          <Button
            disabled={!form.isValid || !form.dirty}
            type="button"
            onClick={() => form.handleSubmit()}
          >
            Cadastrar <i className="fa-solid fa-paw"></i>
          </Button>
        </div>
      </AnimalForm>
    </Container>
  )
}

export default NewAnimal
