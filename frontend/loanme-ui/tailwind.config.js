/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/**/*.{html,js,svelte,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'oxford-blue': '#0A174A',
        'maize': '#F5D042'
      }
    },
  },
  plugins: [],
}