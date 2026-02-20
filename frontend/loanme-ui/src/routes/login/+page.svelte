<script>
  import { goto } from '$app/navigation';
  
  let email = '';
  let password = '';
  let loading = false;
  let error = '';
  
  async function handleSubmit() {
    loading = true;
    error = '';
    
    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // For demo purposes, redirect to dashboard
      goto('/dashboard');
    } catch (err) {
      error = 'Invalid email or password. Please try again.';
    } finally {
      loading = false;
    }
  }
</script>

<svelte:head>
  <title>Login | LoanMe</title>
  <meta name="description" content="Login to your LoanMe account to manage your loans and applications." />
</svelte:head>

<section class="pt-32 pb-20">
  <div class="container mx-auto px-4">
    <div class="max-w-md mx-auto">
      <div class="text-center mb-10">
        <h1 class="text-3xl font-bold text-oxford-blue mb-2">Welcome Back</h1>
        <p class="text-gray-600">Sign in to your LoanMe account</p>
      </div>
      
      <div class="card bg-white p-8">
        {#if error}
          <div class="bg-red-50 text-red-700 p-4 rounded-lg mb-6">
            {error}
          </div>
        {/if}
        
        <form on:submit|preventDefault={handleSubmit}>
          <div class="mb-6">
            <label class="block text-gray-700 mb-2 font-medium">Email Address</label>
            <input 
              type="email" 
              class="input" 
              bind:value={email} 
              placeholder="you@example.com"
              required
            />
          </div>
          
          <div class="mb-6">
            <div class="flex justify-between items-center mb-2">
              <label class="block text-gray-700 font-medium">Password</label>
              <a href="/forgot-password" class="text-oxford-blue text-sm hover:underline">Forgot password?</a>
            </div>
            <input 
              type="password" 
              class="input" 
              bind:value={password} 
              placeholder="••••••••"
              required
            />
          </div>
          
          <div class="mb-6">
            <label class="flex items-center">
              <input type="checkbox" class="rounded text-oxford-blue focus:ring-oxford-blue" />
              <span class="ml-2 text-gray-700">Remember me</span>
            </label>
          </div>
          
          <button 
            type="submit" 
            class="btn btn-primary w-full"
            disabled={loading}
          >
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>
        
        <div class="mt-8 text-center">
          <p class="text-gray-600">
            Don't have an account? <a href="/register" class="text-oxford-blue font-medium hover:underline">Sign up</a>
          </p>
        </div>
      </div>
    </div>
  </div>
</section>