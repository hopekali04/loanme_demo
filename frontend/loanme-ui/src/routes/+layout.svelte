<script>
  import '../app.css';
  import Header from '$lib/components/Header.svelte';
  import Footer from '$lib/components/Footer.svelte';
</script>

<Header />

<main class="min-h-screen">
  <slot />
</main>

<Footer />