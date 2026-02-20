<script>
  import DashboardLayout from '$lib/components/DashboardLayout.svelte';
  import { onMount } from 'svelte';
  
  let applications = [
    {
      id: 1,
      type: 'Personal Loan',
      amount: 10000,
      status: 'Approved',
      date: '2023-06-12',
      statusClass: 'bg-green-100 text-green-800'
    },
    {
      id: 2,
      type: 'Auto Loan',
      amount: 25000,
      status: 'Under Review',
      date: '2023-05-28',
      statusClass: 'bg-yellow-100 text-yellow-800'
    },
    {
      id: 3,
      type: 'Mortgage',
      amount: 350000,
      status: 'Rejected',
      date: '2023-04-15',
      statusClass: 'bg-red-100 text-red-800'
    }
  ];
</script>

<svelte:head>
  <title>My Applications | LoanMe</title>
  <meta name="description" content="View and manage your loan applications." />
</svelte:head>

<DashboardLayout activeTab="applications">
  <div class="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between">
    <div>
      <h1 class="text-2xl font-bold text-oxford-blue">My Applications</h1>
      <p class="text-gray-600">Manage your loan applications and track their status.</p>
    </div>
    <a href="/apply" class="btn btn-primary mt-4 sm:mt-0">New Application</a>
  </div>
  
  <div class="card bg-white overflow-hidden">
    <div class="overflow-x-auto">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th class="py-3 px-6 text-left">Loan Type</th>
            <th class="py-3 px-6 text-left">Amount</th>
            <th class="py-3 px-6 text-left">Date</th>
            <th class="py-3 px-6 text-left">Status</th>
            <th class="py-3 px-6 text-right">Actions</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-200">
          {#each applications as application}
            <tr class="hover:bg-gray-50">
              <td class="py-4 px-6 font-medium">{application.type}</td>
              <td class="py-4 px-6">${application.amount.toLocaleString()}</td>
              <td class="py-4 px-6 text-gray-600">{application.date}</td>
              <td class="py-4 px-6">
                <span class="px-3 py-1 rounded-full text-sm font-medium {application.statusClass}">
                  {application.status}
                </span>
              </td>
              <td class="py-4 px-6 text-right">
                <button class="text-oxford-blue hover:text-maize font-medium">
                  View Details
                </button>
              </td>
            </tr>
          {/each}
        </tbody>
      </table>
    </div>
  </div>
</DashboardLayout>