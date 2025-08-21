<script>
  import { onMount } from 'svelte';
  import Logo from '$lib/components/Logo.svelte';
  
  let isMenuOpen = false;
  let isScrolled = false;
  
  onMount(() => {
    const handleScroll = () => {
      isScrolled = window.scrollY > 10;
    };
    
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  });
</script>

<header class="fixed w-full z-50 transition-all duration-300 ease-in-out {isScrolled ? 'glass py-2' : 'bg-transparent py-4'}">
  <div class="container mx-auto px-4">
    <div class="flex items-center justify-between">
      <Logo />
      
      <nav class="hidden md:flex space-x-8">
        <a href="/" class="text-oxford-blue font-medium hover:text-maize transition-colors">Home</a>
        <a href="/calculator" class="text-oxford-blue font-medium hover:text-maize transition-colors">Calculator</a>
        <a href="/apply" class="text-oxford-blue font-medium hover:text-maize transition-colors">Apply</a>
        <a href="/dashboard" class="text-oxford-blue font-medium hover:text-maize transition-colors">Dashboard</a>
      </nav>
      
      <div class="hidden md:flex items-center space-x-4">
        <a href="/login" class="btn btn-secondary">Login</a>
        <a href="/register" class="btn btn-primary">Register</a>
      </div>
      
      <button 
        class="md:hidden text-oxford-blue focus:outline-none"
        on:click={() => isMenuOpen = !isMenuOpen}
        aria-label="Toggle menu"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
        </svg>
      </button>
    </div>
  </div>
  
  {#if isMenuOpen}
    <div class="md:hidden bg-white mt-2 rounded-2xl shadow-lg mx-4 p-4">
      <div class="flex flex-col space-y-4">
        <a href="/" class="text-oxford-blue font-medium hover:text-maize transition-colors py-2">Home</a>
        <a href="/calculator" class="text-oxford-blue font-medium hover:text-maize transition-colors py-2">Calculator</a>
        <a href="/apply" class="text-oxford-blue font-medium hover:text-maize transition-colors py-2">Apply</a>
        <a href="/dashboard" class="text-oxford-blue font-medium hover:text-maize transition-colors py-2">Dashboard</a>
        <div class="flex space-x-4 pt-4">
          <a href="/login" class="btn btn-secondary flex-1">Login</a>
          <a href="/register" class="btn btn-primary flex-1">Register</a>
        </div>
      </div>
    </div>
  {/if}
</header>