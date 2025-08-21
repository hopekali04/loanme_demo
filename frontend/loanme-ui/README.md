# LoanMe UI

A modern, Apple-inspired SvelteKit frontend for the LoanMe application.

## Design Principles

- **Apple-inspired Design**: Clean, minimalist interface with generous whitespace and smooth animations
- **Color Scheme**: Oxford Blue (#0A174A) and Maize (#F5D042)
- **Responsive**: Works beautifully on all device sizes
- **Accessible**: Follows WCAG guidelines for accessibility

## Features

- Home page with hero section and features
- Interactive loan calculator
- Multi-step loan application form
- User dashboard with application tracking
- Login and registration pages

## Development

### Prerequisites

- Node.js 16+
- npm or yarn

### Setup

1. Install dependencies:
   ```bash
   npm install
   ```

2. Run the development server:
   ```bash
   npm run dev
   ```

3. Build for production:
   ```bash
   npm run build
   ```

## Project Structure

```
src/
  lib/
    components/     # Reusable components
  routes/           # Page routes
  app.css          # Global styles
  app.html         # Base HTML template
```

## Technologies

- [SvelteKit](https://kit.svelte.dev/)
- [Tailwind CSS](https://tailwindcss.com/)
- [Vite](https://vitejs.dev/)

