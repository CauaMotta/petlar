/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from 'react'
import { useFormik } from 'formik'
import * as Yup from 'yup'
import { IMaskInput } from 'react-imask'
import { useNavigate, useParams } from 'react-router-dom'
import { useSelector } from 'react-redux'

import { formatDateBr, formatDateIso } from '../../utils'
import { useGetAnimalById, useUpdateAnimal } from '../../hooks/useAnimals'
import type { RootReducer } from '../../store'

import Modal from '../../components/Modal'
import BackButton from '../../components/BackButton'
import StyledSelectWrapper from '../../components/StyledSelectWrapper'
import Loader from '../../components/Loader'

import { AnimalForm, Container } from './styles'
import {
  ErrorMessage,
  StyledButton,
  Line,
  ButtonGroup,
  InputGroup
} from '../../styles'

const options = [
  { value: 'CACHORRO', label: 'Cachorro' },
  { value: 'GATO', label: 'Gato' },
  { value: 'PASSARO', label: 'Pássaro' },
  { value: 'OUTRO', label: 'Outro' }
]

const sexOptions = [
  { value: 'MACHO', label: 'Macho' },
  { value: 'FEMEA', label: 'Fêmea' }
]

const sizeOptions = [
  { value: 'PEQUENO', label: 'Pequeno' },
  { value: 'MEDIO', label: 'Médio' },
  { value: 'GRANDE', label: 'Grande' }
]

const parseDateString = (originalValue: string) => {
  if (!originalValue || typeof originalValue !== 'string') return new Date('')

  const [day, month, year] = originalValue.split('/').map(Number)

  const date = new Date(year, month - 1, day)

  if (
    date.getFullYear() !== year ||
    date.getMonth() !== month - 1 ||
    date.getDate() !== day
  ) {
    return new Date('')
  }

  return date
}

type Params = {
  id: string
}

const EditAnimal = () => {
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()
  const { id } = useParams() as Params
  const { data: animal, isLoading } = useGetAnimalById(id)
  const { user } = useSelector((state: RootReducer) => state.auth)
  const { mutate, isPending, error, isSuccess, reset } = useUpdateAnimal()

  const form = useFormik({
    initialValues: {
      name: animal?.name || '',
      birthDate: formatDateBr(animal?.birthDate) || '',
      weight: animal?.weight || null,
      type: animal?.type || '',
      sex: animal?.sex || '',
      size: animal?.size || '',
      image: null as File | null,
      description: animal?.description || ''
    },
    enableReinitialize: true,
    validationSchema: Yup.object({
      name: Yup.string()
        .min(3, 'Deve conter no mínimo 3 caracteres.')
        .max(110, 'Deve conter no máximo 110 caracteres.')
        .required('Este campo é obrigatório.'),
      birthDate: Yup.string()
        .required('Este campo é obrigatório.')
        .test('is-valid-date', 'Data inválida.', (value) => {
          if (!value) return false
          if (!/^\d{2}\/\d{2}\/\d{4}$/.test(value)) return false
          const date = parseDateString(value)
          return !isNaN(date.getTime())
        })
        .test(
          'not-future',
          'A data não pode estar no futuro.',
          function (value) {
            if (!value) return true

            const date = parseDateString(value)

            const today = new Date()
            today.setHours(0, 0, 0, 0)

            return date <= today
          }
        ),
      weight: Yup.string()
        .required('Este campo é obrigatório.')
        .test('greater-than-zero', 'O peso deve ser maior que 0.', (value) => {
          if (!value) return false
          return parseFloat(value) > 0
        }),
      type: Yup.string().required('Este campo é obrigatório.'),
      sex: Yup.string().required('Este campo é obrigatório.'),
      size: Yup.string().required('Este campo é obrigatório.'),
      image: Yup.mixed()
        .test(
          'fileSize',
          'O arquivo é muito grande (max 2MB).',
          (value: any) => {
            if (!value) return true
            return value && value.size <= 2 * 1024 * 1024
          }
        )
        .test('fileType', 'Formato não suportado.', (value: any) => {
          if (!value) return true
          return (
            value &&
            ['image/jpeg', 'image/png', 'image/jpg'].includes(value.type)
          )
        })
        .nullable(),
      description: Yup.string()
        .min(3, 'Deve conter no mínimo 3 caracteres.')
        .max(255, 'Deve conter no máximo 255 caracteres.')
    }),
    onSubmit: async (values) => {
      const formData = new FormData()

      const { image, description, ...rest } = values

      const payload = {
        ...rest,
        birthDate: formatDateIso(values.birthDate),
        ...(description && { description })
      }

      const jsonBlob = new Blob([JSON.stringify(payload)], {
        type: 'application/json'
      })

      formData.append('data', jsonBlob)
      if (image) {
        formData.append('image', image)
      }

      mutate({ id, formData })
    },
    validateOnMount: true
  })

  const isError = (fieldName: string) => {
    const isTouched = fieldName in form.touched
    const isInvalid = fieldName in form.errors

    if (isTouched && isInvalid) return true
    return false
  }

  useEffect(() => {
    const timer = setTimeout(() => {
      setLoading(false)
    }, 100)

    if (animal && user && animal.author.id !== user.id) navigate(-1)

    if (isSuccess) {
      form.resetForm()
    }

    return () => clearTimeout(timer)
  }, [animal, user, navigate, form, isSuccess])

  if (loading || isLoading)
    return (
      <Container>
        <div className="box">
          <Loader />
        </div>
      </Container>
    )

  return (
    <Container>
      <BackButton path={-1} />
      <h2 className="title--big textCenter">Editando um Pet</h2>
      <Line />
      <p className="text">O que você deseja mudar?</p>
      <AnimalForm>
        <div className="animalSelect">
          {isError('type') ? (
            <ErrorMessage>* {form.errors.type}</ErrorMessage>
          ) : (
            ''
          )}
          <StyledSelectWrapper
            placeholder="Selecione um animal"
            value={
              options.find((option) => option.value === form.values.type) ||
              null
            }
            onChange={(option) =>
              form.setFieldValue('type', option?.value || null)
            }
            options={options}
            minWidth={256}
          />
        </div>
        <InputGroup>
          <label className="text" htmlFor="name">
            Nome:{' '}
            {isError('name') ? (
              <ErrorMessage>* {form.errors.name}</ErrorMessage>
            ) : (
              ''
            )}
          </label>
          <input
            id="name"
            placeholder="Bob"
            className="input"
            type="text"
            value={form.values.name}
            onChange={form.handleChange}
            onBlur={(e) => {
              const trimmedValue = e.target.value.replace(/\s+/g, ' ').trim()
              form.setFieldValue('name', trimmedValue)
              form.handleBlur(e)
            }}
          />
        </InputGroup>
        <InputGroup>
          <label className="text" htmlFor="birthDate">
            Data de nascimento:{' '}
            {isError('birthDate') ? (
              <ErrorMessage>* {form.errors.birthDate}</ErrorMessage>
            ) : (
              ''
            )}
          </label>
          <IMaskInput
            id="birthDate"
            mask="00/00/0000"
            placeholder={'01/01/2026'}
            className="input"
            value={form.values.birthDate}
            onAccept={(value) => form.setFieldValue('birthDate', value)}
            onBlur={form.handleBlur}
          />
        </InputGroup>
        <InputGroup>
          <label className="text">Sexo:</label>
          <StyledSelectWrapper
            placeholder="Selecione..."
            value={
              sexOptions.find((option) => option.value === form.values.sex) ||
              null
            }
            onChange={(option) => form.setFieldValue('sex', option?.value)}
            options={sexOptions}
          />
        </InputGroup>
        <InputGroup>
          <label className="text" htmlFor="weight">
            Peso:{' '}
            {isError('weight') ? (
              <ErrorMessage>* {form.errors.weight}</ErrorMessage>
            ) : (
              ''
            )}
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
                ? String(form.values.weight / 1000)
                : ''
            }
            onAccept={(value) => {
              const intWeight = value ? Math.round(Number(value) * 1000) : null
              form.setFieldValue('weight', intWeight)
            }}
            onBlur={form.handleBlur}
          />
        </InputGroup>
        <InputGroup>
          <label className="text" htmlFor="">
            Porte:
          </label>
          <StyledSelectWrapper
            placeholder="Selecione..."
            value={
              sizeOptions.find((option) => option.value === form.values.size) ||
              null
            }
            onChange={(option) => form.setFieldValue('size', option?.value)}
            options={sizeOptions}
          />
        </InputGroup>
        <InputGroup>
          <label className="text" htmlFor="image">
            {animal?.imagePath
              ? 'Substituir a imagem atual: '
              : 'Adicionar uma imagem: '}
            {isError('image') ? (
              <ErrorMessage>* {form.errors.image}</ErrorMessage>
            ) : (
              ''
            )}
          </label>
          <label htmlFor="image" className="imageBtn">
            {form.values.image ? (
              <>
                <p>
                  <i className="fa-solid fa-file"></i> {form.values.image.name}
                </p>
                <span onClick={() => form.setFieldValue('image', null)}>
                  <i className="fa-solid fa-trash"></i>
                </span>
              </>
            ) : (
              <p>
                <i className="fa-regular fa-file"></i> Escolher uma imagem
              </p>
            )}
          </label>
          <input
            id="image"
            type="file"
            accept="image/*"
            onChange={(e) => {
              const file = e.currentTarget.files?.[0] || null
              form.setFieldValue('image', file)
              form.setFieldTouched('image', true, false)
              form.validateField('image')
            }}
          />
        </InputGroup>
        <InputGroup>
          <label className="text" htmlFor="description">
            {animal?.description
              ? 'Editar a descrição: '
              : 'Adicionar uma descrição: '}
            {isError('description') ? (
              <ErrorMessage>* {form.errors.description}</ErrorMessage>
            ) : (
              ''
            )}
          </label>
          <textarea
            id="description"
            placeholder="Muito dócil."
            className="input"
            rows={3}
            maxLength={250}
            value={form.values.description}
            onChange={form.handleChange}
            onBlur={(e) => {
              const trimmedValue = e.target.value.replace(/\s+/g, ' ').trim()
              form.setFieldValue('description', trimmedValue)
              form.handleBlur(e)
            }}
          />
        </InputGroup>
        <ButtonGroup>
          <StyledButton
            $maxWidth="fit-content"
            className="btnReset"
            type="button"
            onClick={() => form.resetForm()}
          >
            Resetar <i className="fa-solid fa-rotate"></i>
          </StyledButton>
          <StyledButton
            $maxWidth="fit-content"
            type="button"
            disabled={!form.isValid || !form.dirty}
            onClick={() => form.handleSubmit()}
          >
            Atualizar <i className="fa-solid fa-paw"></i>
          </StyledButton>
        </ButtonGroup>
      </AnimalForm>

      <Modal title="Atualizando..." isOpen={isPending} onClose={() => reset()}>
        <div className="box">
          <Loader />
          <p className="text">
            Estamos atualizando os dados do seu bixinho, aguarde um pouquinho...
          </p>
        </div>
      </Modal>

      <Modal title="Ops..." isOpen={!!error} onClose={() => reset()}>
        <div className="box">
          <i className="fa-solid fa-triangle-exclamation"></i>
          <p className="text">
            Parece que tivemos um problema ao fazer a atualização, <br /> tente
            novamente mais tarde!
          </p>
        </div>
      </Modal>

      <Modal
        title="Sucesso!"
        isOpen={isSuccess}
        onClose={() => {
          reset()
          navigate(-1)
        }}
      >
        <div className="box">
          <i className="fa-solid fa-check"></i>
          <p className="text">Seu bixinho foi atualizado com sucesso!</p>
        </div>
      </Modal>
    </Container>
  )
}

export default EditAnimal
