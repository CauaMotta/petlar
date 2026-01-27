# --- FRONTEND BUILD ---
# Estágio 1: Build
FROM node:20-alpine AS frontend-build
WORKDIR /app

ARG VITE_API_URL
ARG VITE_API_PREFIX

ENV VITE_API_URL=$VITE_API_URL
ENV VITE_API_PREFIX=$VITE_API_PREFIX

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Estágio 2: Runtime (Nginx)
FROM nginx:stable-alpine
COPY --from=frontend-build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
