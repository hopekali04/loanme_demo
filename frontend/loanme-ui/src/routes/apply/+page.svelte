<script>
  import { onMount } from 'svelte';
  
  let currentStep = 1;
  let totalSteps = 4;
  
  // Form data
  let loanType = 'personal';
  let loanAmount = 10000;
  let loanTerm = 36;
  let purpose = '';
  
  let firstName = '';
  let lastName = '';
  let email = '';
  let phone = '';
  let dob = '';
  let ssn = '';
  
  let address = '';
  let city = '';
  let state = '';
  let zip = '';
  
  let employer = '';
  let jobTitle = '';
  let annualIncome = '';
  let employmentYears = '';
  let monthlyHousing = '';
  
  let agreeToTerms = false;
  let loading = false;
  let error = '';
  
  function nextStep() {
    if (currentStep < totalSteps) {
      currentStep++;
    }
  }
  
  function prevStep() {
    if (currentStep > 1) {
      currentStep--;
    }
  }
  
  async function submitApplication() {
    loading = true;
    error = '';
    
    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1500));
      
      // For demo purposes, show success
      alert('Application submitted successfully!');
    } catch (err) {
      error = 'Failed to submit application. Please try again.';
    } finally {
      loading = false;
    }
  }
  
  function formatCurrency(value) {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(value);
  }
</script>

<svelte:head>
  <title>Apply for Loan | LoanMe</title>
  <meta name="description" content="Apply for a loan with LoanMe - fast, simple, and secure." />
</svelte:head>

<section class="pt-32 pb-20">
  <div class="container mx-auto px-4">
    <div class="max-w-4xl mx-auto">
      <div class="text-center mb-10">
        <h1 class="text-3xl font-bold text-oxford-blue mb-2">Apply for a Loan</h1>
        <p class="text-gray-600">Complete the application form in a few simple steps</p>
      </div>
      
      <!-- Progress Bar -->
      <div class="card bg-white p-8 mb-8">
        <div class="flex justify-between mb-4">
          {#each Array(totalSteps) as _, i}
            <div class="flex flex-col items-center w-1/{totalSteps}">
              <div class="flex items-center w-full">
                <div class="flex-1 h-1 {i < currentStep - 1 ? 'bg-oxford-blue' : 'bg-gray-200'}"></div>
                {#if i < totalSteps - 1}
                  <div class="flex-1 h-1 {i < currentStep - 1 ? 'bg-oxford-blue' : 'bg-gray-200'}"></div>
                {/if}
              </div>
              <div class="mt-2 w-8 h-8 rounded-full flex items-center justify-center {i < currentStep ? 'bg-oxford-blue text-white' : 'bg-gray-200 text-gray-600'}">
                {i + 1}
              </div>
            </div>
          {/each}
        </div>
        
        <div class="text-center">
          <p class="font-medium text-oxford-blue">
            {#if currentStep === 1}Loan Details
            {:else if currentStep === 2}Personal Information
            {:else if currentStep === 3}Employment & Housing
            {:else if currentStep === 4}Review & Submit
            {/if}
          </p>
        </div>
      </div>
      
      <!-- Step 1: Loan Details -->
      {#if currentStep === 1}
        <div class="card bg-white p-8">
          <h2 class="text-2xl font-bold text-oxford-blue mb-6">Loan Details</h2>
          
          <div class="space-y-6">
            <div>
              <label class="block text-gray-700 mb-2 font-medium">Loan Type</label>
              <select class="input" bind:value={loanType}>
                <option value="personal">Personal Loan</option>
                <option value="auto">Auto Loan</option>
                <option value="mortgage">Mortgage</option>
                <option value="business">Business Loan</option>
              </select>
            </div>
            
            <div>
              <label class="block text-gray-700 mb-2 font-medium">Loan Amount</label>
              <div class="relative">
                <span class="absolute left-3 top-3 text-gray-400">$</span>
                <input 
                  type="range" 
                  class="w-full mb-2" 
                  min="1000" 
                  max="100000" 
                  step="1000" 
                  bind:value={loanAmount} 
                />
                <input 
                  type="number" 
                  class="input pl-8" 
                  bind:value={loanAmount} 
                  min="1000" 
                  max="100000"
                />
              </div>
              <p class="text-center mt-2 text-lg font-semibold">{formatCurrency(loanAmount)}</p>
            </div>
            
            <div>
              <label class="block text-gray-700 mb-2 font-medium">Loan Term</label>
              <div class="relative">
                <input 
                  type="range" 
                  class="w-full mb-2" 
                  min="6" 
                  max="360" 
                  step="6" 
                  bind:value={loanTerm} 
                />
                <select class="input" bind:value={loanTerm}>
                  <option value="6">6 months</option>
                  <option value="12">12 months</option>
                  <option value="18">18 months</option>
                  <option value="24">24 months</option>
                  <option value="36">36 months</option>
                  <option value="48">48 months</option>
                  <option value="60">60 months</option>
                  <option value="84">84 months</option>
                  <option value="120">120 months</option>
                  <option value="180">180 months</option>
                  <option value="240">240 months</option>
                  <option value="360">360 months</option>
                </select>
              </div>
            </div>
            
            <div>
              <label class="block text-gray-700 mb-2 font-medium">Purpose of Loan</label>
              <select class="input" bind:value={purpose}>
                <option value="">Select a purpose</option>
                <option value="debt_consolidation">Debt Consolidation</option>
                <option value="home_improvement">Home Improvement</option>
                <option value="major_purchase">Major Purchase</option>
                <option value="medical_expenses">Medical Expenses</option>
                <option value="vacation">Vacation</option>
                <option value="wedding">Wedding</option>
                <option value="business_expansion">Business Expansion</option>
                <option value="education">Education</option>
                <option value="other">Other</option>
              </select>
            </div>
          </div>
          
          <div class="mt-8 flex justify-end">
            <button class="btn btn-primary" on:click={nextStep}>Continue</button>
          </div>
        </div>
      {/if}
      
      <!-- Step 2: Personal Information -->
      {#if currentStep === 2}
        <div class="card bg-white p-8">
          <h2 class="text-2xl font-bold text-oxford-blue mb-6">Personal Information</h2>
          
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
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
            
            <div>
              <label class="block text-gray-700 mb-2 font-medium">Email Address</label>
              <input 
                type="email" 
                class="input" 
                bind:value={email} 
                placeholder="you@example.com"
                required
              />
            </div>
            
            <div>
              <label class="block text-gray-700 mb-2 font-medium">Phone Number</label>
              <input 
                type="tel" 
                class="input" 
                bind:value={phone} 
                placeholder="(123) 456-7890"
                required
              />
            </div>
            
            <div>
              <label class="block text-gray-700 mb-2 font-medium">Date of Birth</label>
              <input 
                type="date" 
                class="input" 
                bind:value={dob} 
                required
              />
            </div>
            
            <div>
              <label class="block text-gray-700 mb-2 font-medium">SSN (Last 4 digits)</label>
              <input 
                type="text" 
                class="input" 
                bind:value={ssn} 
                placeholder="1234"
                maxlength="4"
                required
              />
            </div>
          </div>
          
          <div class="mt-8 flex justify-between">
            <button class="btn btn-secondary" on:click={prevStep}>Back</button>
            <button class="btn btn-primary" on:click={nextStep}>Continue</button>
          </div>
        </div>
      {/if}
      
      <!-- Step 3: Employment & Housing -->
      {#if currentStep === 3}
        <div class="card bg-white p-8">
          <h2 class="text-2xl font-bold text-oxford-blue mb-6">Employment & Housing</h2>
          
          <div class="space-y-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-gray-700 mb-2 font-medium">Current Employer</label>
                <input 
                  type="text" 
                  class="input" 
                  bind:value={employer} 
                  placeholder="Company Name"
                  required
                />
              </div>
              
              <div>
                <label class="block text-gray-700 mb-2 font-medium">Job Title</label>
                <input 
                  type="text" 
                  class="input" 
                  bind:value={jobTitle} 
                  placeholder="Your Position"
                  required
                />
              </div>
              
              <div>
                <label class="block text-gray-700 mb-2 font-medium">Annual Income</label>
                <div class="relative">
                  <span class="absolute left-3 top-3 text-gray-400">$</span>
                  <input 
                    type="number" 
                    class="input pl-8" 
                    bind:value={annualIncome} 
                    placeholder="50000"
                    required
                  />
                </div>
              </div>
              
              <div>
                <label class="block text-gray-700 mb-2 font-medium">Years at Current Job</label>
                <input 
                  type="number" 
                  class="input" 
                  bind:value={employmentYears} 
                  placeholder="2"
                  required
                />
              </div>
            </div>
            
            <div>
              <label class="block text-gray-700 mb-2 font-medium">Current Address</label>
              <input 
                type="text" 
                class="input mb-4" 
                bind:value={address} 
                placeholder="Street Address"
                required
              />
              
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <input 
                  type="text" 
                  class="input" 
                  bind:value={city} 
                  placeholder="City"
                  required
                />
                
                <input 
                  type="text" 
                  class="input" 
                  bind:value={state} 
                  placeholder="State"
                  required
                />
                
                <input 
                  type="text" 
                  class="input" 
                  bind:value={zip} 
                  placeholder="ZIP Code"
                  required
                />
              </div>
            </div>
            
            <div>
              <label class="block text-gray-700 mb-2 font-medium">Monthly Housing Payment</label>
              <div class="relative">
                <span class="absolute left-3 top-3 text-gray-400">$</span>
                <input 
                  type="number" 
                  class="input pl-8" 
                  bind:value={monthlyHousing} 
                  placeholder="1200"
                  required
                />
              </div>
            </div>
          </div>
          
          <div class="mt-8 flex justify-between">
            <button class="btn btn-secondary" on:click={prevStep}>Back</button>
            <button class="btn btn-primary" on:click={nextStep}>Continue</button>
          </div>
        </div>
      {/if}
      
      <!-- Step 4: Review & Submit -->
      {#if currentStep === 4}
        <div class="card bg-white p-8">
          <h2 class="text-2xl font-bold text-oxford-blue mb-6">Review & Submit</h2>
          
          <div class="space-y-8">
            <div>
              <h3 class="text-lg font-semibold text-oxford-blue mb-4">Loan Details</h3>
              <div class="bg-gray-50 rounded-lg p-4">
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div>
                    <p class="text-gray-600">Loan Type</p>
                    <p class="font-medium capitalize">{loanType.replace('_', ' ')}</p>
                  </div>
                  <div>
                    <p class="text-gray-600">Loan Amount</p>
                    <p class="font-medium">{formatCurrency(loanAmount)}</p>
                  </div>
                  <div>
                    <p class="text-gray-600">Loan Term</p>
                    <p class="font-medium">{loanTerm} months</p>
                  </div>
                </div>
              </div>
            </div>
            
            <div>
              <h3 class="text-lg font-semibold text-oxford-blue mb-4">Personal Information</h3>
              <div class="bg-gray-50 rounded-lg p-4">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <p class="text-gray-600">Name</p>
                    <p class="font-medium">{firstName} {lastName}</p>
                  </div>
                  <div>
                    <p class="text-gray-600">Email</p>
                    <p class="font-medium">{email}</p>
                  </div>
                  <div>
                    <p class="text-gray-600">Phone</p>
                    <p class="font-medium">{phone}</p>
                  </div>
                  <div>
                    <p class="text-gray-600">Date of Birth</p>
                    <p class="font-medium">{dob}</p>
                  </div>
                </div>
              </div>
            </div>
            
            <div>
              <h3 class="text-lg font-semibold text-oxford-blue mb-4">Employment & Housing</h3>
              <div class="bg-gray-50 rounded-lg p-4">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <p class="text-gray-600">Employer</p>
                    <p class="font-medium">{employer}</p>
                  </div>
                  <div>
                    <p class="text-gray-600">Job Title</p>
                    <p class="font-medium">{jobTitle}</p>
                  </div>
                  <div>
                    <p class="text-gray-600">Annual Income</p>
                    <p class="font-medium">${annualIncome}</p>
                  </div>
                  <div>
                    <p class="text-gray-600">Monthly Housing</p>
                    <p class="font-medium">${monthlyHousing}</p>
                  </div>
                </div>
                
                <div class="mt-4">
                  <p class="text-gray-600">Address</p>
                  <p class="font-medium">{address}, {city}, {state} {zip}</p>
                </div>
              </div>
            </div>
            
            <div>
              <label class="flex items-center">
                <input 
                  type="checkbox" 
                  class="rounded text-oxford-blue focus:ring-oxford-blue" 
                  bind:checked={agreeToTerms}
                  required
                />
                <span class="ml-2 text-gray-700">I agree to the <a href="/terms" class="text-oxford-blue hover:underline">Terms of Service</a> and <a href="/privacy" class="text-oxford-blue hover:underline">Privacy Policy</a></span>
              </label>
            </div>
            
            {#if error}
              <div class="bg-red-50 text-red-700 p-4 rounded-lg">
                {error}
              </div>
            {/if}
          </div>
          
          <div class="mt-8 flex justify-between">
            <button class="btn btn-secondary" on:click={prevStep}>Back</button>
            <button 
              class="btn btn-primary" 
              on:click={submitApplication}
              disabled={loading || !agreeToTerms}
            >
              {loading ? 'Submitting...' : 'Submit Application'}
            </button>
          </div>
        </div>
      {/if}
    </div>
  </div>
</section>