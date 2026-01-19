import axios from 'axios'
import Cookies from 'js-cookie'

const API_URL = import.meta.env.VITE_API_URL
const API_PREFIX = import.meta.env.VITE_API_PREFIX

export const api = axios.create({
  baseURL: API_URL + API_PREFIX
})

api.interceptors.request.use((config) => {
  const token = Cookies.get('token')

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})
