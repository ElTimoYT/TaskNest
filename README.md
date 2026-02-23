# 🪹 TaskNest

![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![NgRx](https://img.shields.io/badge/NgRx-BA2929?style=for-the-badge&logo=ngrx&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)

> **Organiza tu vida, sin perder la calma.**
> TaskNest es una aplicación Full-Stack de gestión de tareas diseñada con una obsesión absoluta por la Experiencia de Usuario (UX), el rendimiento y los detalles visuales propios de un producto SaaS Premium.

---

## ✨ Características Principales

### 🎨 UI/UX Premium (Diseño Asistido por IA)

- **Modo Oscuro Nativo:** Tema visual adaptable (Claro/Oscuro) con guardado de preferencias en `localStorage`.
- **Glassmorphism:** Tarjetas y menús translúcidos con efecto de cristal esmerilado y desenfoques dinámicos.
- **Landing Page Interactiva:** Animaciones de entrada al hacer scroll utilizando `AOS` y un diseño moderno tipo *Bento Grid*.
- **Skeleton Loaders:** Transiciones de carga fluidas que evitan saltos bruscos en la interfaz simulando las tarjetas de tareas.
- **Micro-interacciones (Dopamina Visual):**
  - Sistema de notificaciones propio (*Toasts*) flotantes con barras de progreso animadas.
  - Explosión de confeti programada (`canvas-confetti`) al marcar una tarea como completada.

### ⚡ Rendimiento y Arquitectura

- **Gestión de Estado Reactiva:** Implementación de **NgRx** (Redux) en el frontend para cálculos en tiempo real (Dashboard de progreso) sin llamadas redundantes al backend.
- **Seguridad:** Autenticación robusta basada en tokens gestionada por Spring Security.
- **Buscador en Tiempo Real:** Filtrado instantáneo de tareas en milisegundos.

---

## 🛠️ Stack Tecnológico

**Frontend:**
- [Angular](https://angular.io/) — Framework principal
- [NgRx](https://ngrx.io/) — Gestión de estado global
- [Tailwind CSS](https://tailwindcss.com/) & CSS Custom — Estilizado y layout
- [Angular Material](https://material.angular.io/) — Componentes base modificados
- [AOS](https://michalsnik.github.io/aos/) — Animaciones de scroll

**Backend:**
- [Spring Boot](https://spring.io/projects/spring-boot) (Java)
- Spring Security — Autenticación
- Base de datos relacional (MySQL / PostgreSQL)

**Automatización (Próximamente):**
- [n8n](https://n8n.io/) — Preparado para integraciones con Telegram, flujos de email y recordatorios automáticos.

---

## 📸 Capturas de Pantalla / Demo

> *Próximamente — Añade aquí GIFs o imágenes de tu aplicación.*
>
> Sugerencias:
> 1. Un GIF de la Landing Page haciendo scroll.
> 2. Una captura del Dashboard en Modo Oscuro.
> 3. Un GIF completando una tarea (para que se vea el confeti y la notificación).

```md
![Demo Landing](./docs/landing.gif)
![Dashboard Modo Oscuro](./docs/dashboard-dark.png)
![Completar Tarea](./docs/complete-task.gif)
```

---

## 🚀 Instalación y Despliegue

### Prerrequisitos

- Node.js (v18+)
- Angular CLI
- Java 17+ y Maven
- Base de datos configurada (MySQL / PostgreSQL)

### 1. Configurar el Backend (Spring Boot)

1. Clona el repositorio.
2. Navega a la carpeta del backend.
3. Configura tus credenciales de base de datos en `application.properties` o `application.yml`.
4. Ejecuta el proyecto:

```bash
mvn clean install
mvn spring-boot:run
```

### 2. Configurar el Frontend (Angular)

1. Navega a la carpeta del frontend.
2. Instala las dependencias:

```bash
npm install
```

3. Inicia el servidor de desarrollo:

```bash
ng serve
```

4. Abre tu navegador en `http://localhost:4200/`

---

## 🤝 Reflexión del Proyecto

Este proyecto nació como el clásico "Gestor de Tareas", pero el objetivo real fue salir de la zona de confort de las plantillas predefinidas. Combinando una arquitectura robusta (Spring Boot + NgRx) y apoyándome en herramientas de Inteligencia Artificial para potenciar y pulir el diseño frontend (HTML/CSS avanzado), el resultado es una aplicación que prioriza la interacción humana y el diseño profesional.

Desarrollado con ☕ y pasión por el código limpio.
