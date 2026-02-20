<script>
  import DashboardLayout from '$lib/components/DashboardLayout.svelte';
</script>

<svelte:head>
  <title>Dashboard | LoanMe</title>
  <meta name="description" content="Your LoanMe dashboard - manage your loans and applications." />
</svelte:head>

<DashboardLayout activeTab="dashboard">
  <div class="mb-8">
    <h1 class="text-2xl font-bold text-oxford-blue">Dashboard</h1>
    <p class="text-gray-600">Welcome back! Here's what's happening with your loans.</p>
  </div>
  
  <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
    <div class="card bg-white p-6">
      <div class="flex items-center">
        <div class="bg-oxford-blue bg-opacity-10 rounded-lg p-3">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-oxford-blue" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <div class="ml-4">
          <p class="text-gray-600">Total Loans</p>
          <p class="text-2xl font-bold">2</p>
        </div>
      </div>
    </div>
    
    <div class="card bg-white p-6">
      <div class="flex items-center">
        <div class="bg-oxford-blue bg-opacity-10 rounded-lg p-3">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-oxford-blue" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <div class="ml-4">
          <p class="text-gray-600">Active Loans</p>
          <p class="text-2xl font-bold">1</p>
        </div>
      </div>
    </div>
    
    <div class="card bg-white p-6">
      <div class="flex items-center">
        <div class="bg-oxford-blue bg-opacity-10 rounded-lg p-3">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-oxford-blue" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <div class="ml-4">
          <p class="text-gray-600">Next Payment</p>
          <p class="text-2xl font-bold">$275.00</p>
        </div>
      </div>
    </div>
  </div>
  
  <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
    <div class="card bg-white p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-xl font-bold text-oxford-blue">Recent Applications</h2>
        <a href="/dashboard/applications" class="text-oxford-blue hover:underline">View all</a>
      </div>
      
      <div class="space-y-4">
        <div class="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
          <div>
            <p class="font-medium">Personal Loan</p>
            <p class="text-sm text-gray-600">Submitted on Jun 12, 2023</p>
          </div>
          <span class="px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-800">Approved</span>
        </div>
        
        <div class="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
          <div>
            <p class="font-medium">Auto Loan</p>
            <p class="text-sm text-gray-600">Submitted on May 28, 2023</p>
          </div>
          <span class="px-3 py-1 rounded-full text-sm font-medium bg-yellow-100 text-yellow-800">Under Review</span>
        </div>
      </div>
    </div>
    
    <div class="card bg-white p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-xl font-bold text-oxford-blue">Upcoming Payments</h2>
        <a href="/dashboard/applications" class="text-oxford-blue hover:underline">View all</a>
      </div>
      
      <div class="space-y-4">
        <div class="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
          <div>
            <p class="font-medium">Personal Loan</p>
            <p class="text-sm text-gray-600">Due on Jul 15, 2023</p>
          </div>
          <span class="font-medium">$275.00</span>
        </div>
        
        <div class="flex items-center justify-between p-4 border border-gray-200 rounded-lg">
          <div>
            <p class="font-medium">Credit Card</p>
            <p class="text-sm text-gray-600">Due on Jul 22, 2023</p>
          </div>
          <span class="font-medium">$150.00</span>
        </div>
      </div>
    </div>
  </div>
</DashboardLayout>