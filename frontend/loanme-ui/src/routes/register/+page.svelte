<script>
  import { goto } from '$app/navigation';
  
  let firstName = '';
  let lastName = '';
  let email = '';
  let password = '';
  let confirmPassword = '';
  let loading = false;
  let error = '';
  
  async function handleSubmit() {
    if (password !== confirmPassword) {
      error = 'Passwords do not match';
      return;
    }
    
    loading = true;
    error = '';
    
    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // For demo purposes, redirect to dashboard
      goto('/dashboard');
    } catch (err) {
      error = 'Registration failed. Please try again.';
    } finally {
      loading = false;
    }
  }
</script>

<svelte:head>
  <title>Register | LoanMe</title>
  <meta name="description" content="Create a new LoanMe account to apply for loans and manage your finances." />
</svelte:head>

<section class="pt-32 pb-20">
  <div class="container mx-auto px-4">
    <div class="max-w-md mx-auto">
      <div class="text-center mb-10">
        <h1 class="text-3xl font-bold text-oxford-blue mb-2">Create Account</h1>
        <p class="text-gray-600">Join LoanMe today and simplify your borrowing experience</p>
      </div>
      
      <div class="card bg-white p-8">
        {#if error}
          <div class="bg-red-50 text-red-700 p-4 rounded-lg mb-6">
            {error}
          </div>
        {/if}
        
        <form on:submit|preventDefault={handleSubmit}>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-6 mb-6">
            <div>
              <label class="block text-gray-700 mb-2 font-medium">First Name</label>
              <input 
                type="text" 
                class="input" 
                bind:value={firstName} 
                placeholder="John"
                required
              />
            </div>
            
            <div>
              <label class="block text-gray-700 mb-2 font-medium">Last Name</label>
              <input 
                type="text" 
                class="input" 
                bind:value={lastName} 
                placeholder="Doe"
                required
              />
            </div>
          </div>
          
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
            <label class="block text-gray-700 mb-2 font-medium">Password</label>
            <input 
              type="password" 
              class="input" 
              bind:value={password} 
              placeholder="••••••••"
              required
            />
          </div>
          
          <div class="mb-6">
            <label class="block text-gray-700 mb-2 font-medium">Confirm Password</label>
            <input 
              type="password" 
              class="input" 
              bind:value={confirmPassword} 
              placeholder="••••••••"
              required
            />
          </div>
          
          <div class="mb-6">
            <label class="flex items-center">
              <input type="checkbox" class="rounded text-oxford-blue focus:ring-oxford-blue" required />
              <span class="ml-2 text-gray-700">I agree to the <a href="/terms" class="text-oxford-blue hover:underline">Terms of Service</a> and <a href="/privacy" class="text-oxford-blue hover:underline">Privacy Policy</a></span>
            </label>
          </div>
          
          <button 
            type="submit" 
            class="btn btn-primary w-full"
            disabled={loading}
          >
            {loading ? 'Creating Account...' : 'Create Account'}
          </button>
        </form>
        
        <div class="mt-8 text-center">
          <p class="text-gray-600">
            Already have an account? <a href="/login" class="text-oxford-blue font-medium hover:underline">Sign in</a>
          </p>
        </div>
      </div>
    </div>
  </div>
</section>