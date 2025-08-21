<script>
  import { onMount } from 'svelte';
  import Logo from '$lib/components/Logo.svelte';
  
  export let activeTab = 'dashboard';
  
  let isMenuOpen = false;
  let user = {
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com'
  };
  
  function logout() {
    // Simulate logout
    console.log('Logging out...');
  }
</script>

<div class="flex flex-col md:flex-row min-h-screen">
  <!-- Sidebar -->
  <div class="md:w-64 bg-oxford-blue text-white">
    <div class="p-6 border-b border-gray-700">
      <Logo size="small" />
    </div>
    
    <div class="p-4">
      <div class="flex items-center mb-8">
        <div class="bg-gray-200 border-2 border-dashed rounded-xl w-12 h-12" />
        <div class="ml-3">
          <p class="font-medium">{user.firstName} {user.lastName}</p>
          <p class="text-gray-400 text-sm">{user.email}</p>
        </div>
      </div>
      
      <nav>
        <a 
          href="/dashboard" 
          class="flex items-center px-4 py-3 rounded-lg mb-2 {activeTab === 'dashboard' ? 'bg-maize bg-opacity-20 text-maize' : 'text-gray-300 hover:bg-gray-700'}"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
          </svg>
          Dashboard
        </a>
        
        <a 
          href="/dashboard/applications" 
          class="flex items-center px-4 py-3 rounded-lg mb-2 {activeTab === 'applications' ? 'bg-maize bg-opacity-20 text-maize' : 'text-gray-300 hover:bg-gray-700'}"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
          Applications
        </a>
        
        <a 
          href="/dashboard/profile" 
          class="flex items-center px-4 py-3 rounded-lg mb-2 {activeTab === 'profile' ? 'bg-maize bg-opacity-20 text-maize' : 'text-gray-300 hover:bg-gray-700'}"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          Profile
        </a>
        
        <a 
          href="/calculator" 
          class="flex items-center px-4 py-3 rounded-lg mb-2 {activeTab === 'calculator' ? 'bg-maize bg-opacity-20 text-maize' : 'text-gray-300 hover:bg-gray-700'}"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
          </svg>
          Calculator
        </a>
      </nav>
      
      <div class="mt-8 pt-8 border-t border-gray-700">
        <button 
          on:click={logout}
          class="flex items-center px-4 py-3 rounded-lg text-gray-300 hover:bg-gray-700 w-full"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
          </svg>
          Logout
        </button>
      </div>
    </div>
  </div>
  
  <!-- Main Content -->
  <div class="flex-1">
    <header class="bg-white shadow-sm">
      <div class="flex items-center justify-between p-4">
        <button 
          class="md:hidden text-oxford-blue focus:outline-none"
          on:click={() => isMenuOpen = !isMenuOpen}
          aria-label="Toggle menu"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
        
        <div></div>
        
        <div class="flex items-center">
          <button class="p-2 rounded-full hover:bg-gray-100">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9" />
            </svg>
          </button>
          
          <div class="ml-4 flex items-center">
            <div class="bg-gray-200 border-2 border-dashed rounded-xl w-8 h-8" />
          </div>
        </div>
      </div>
    </header>
    
    <main class="p-4 md:p-8">
      <slot />
    </main>
  </div>
</div>