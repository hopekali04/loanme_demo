<script>
  import { onMount } from 'svelte';
  
  let loanAmount = 10000;
  let interestRate = 5.5;
  let loanTerm = 36;
  let startDate = new Date().toISOString().split('T')[0];
  
  let monthlyPayment = 0;
  let totalPayment = 0;
  let totalInterest = 0;
  let amortizationSchedule = [];
  
  function formatCurrency(value) {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 2
    }).format(value);
  }
  
  function calculateLoan() {
    const principal = loanAmount;
    const monthlyRate = interestRate / 100 / 12;
    const numberOfPayments = loanTerm;
    
    // Calculate monthly payment
    const x = Math.pow(1 + monthlyRate, numberOfPayments);
    monthlyPayment = (principal * x * monthlyRate) / (x - 1);
    
    // Calculate total payment and interest
    totalPayment = monthlyPayment * numberOfPayments;
    totalInterest = totalPayment - principal;
    
    // Generate amortization schedule
    amortizationSchedule = [];
    let balance = principal;
    let cumulativeInterest = 0;
    
    for (let i = 1; i <= numberOfPayments; i++) {
      const interestPayment = balance * monthlyRate;
      const principalPayment = monthlyPayment - interestPayment;
      balance -= principalPayment;
      cumulativeInterest += interestPayment;
      
      // Calculate payment date
      const paymentDate = new Date(startDate);
      paymentDate.setMonth(paymentDate.getMonth() + i - 1);
      
      amortizationSchedule.push({
        paymentNumber: i,
        paymentDate: paymentDate.toISOString().split('T')[0],
        paymentAmount: monthlyPayment,
        principalPayment: principalPayment,
        interestPayment: interestPayment,
        cumulativeInterest: cumulativeInterest,
        balance: balance > 0 ? balance : 0
      });
    }
  }
  
  onMount(() => {
    calculateLoan();
  });
  
  $: calculateLoan();
</script>

<div class="card bg-white p-8">
  <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
    <div>
      <h2 class="text-2xl font-bold text-oxford-blue mb-6">Loan Details</h2>
      
      <div class="space-y-6">
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
        </div>
        
        <div>
          <label class="block text-gray-700 mb-2 font-medium">Interest Rate</label>
          <div class="relative">
            <input 
              type="range" 
              class="w-full mb-2" 
              min="0.1" 
              max="30" 
              step="0.1" 
              bind:value={interestRate} 
            />
            <input 
              type="number" 
              class="input" 
              bind:value={interestRate} 
              min="0.1" 
              max="30" 
              step="0.1"
            />
            <span class="absolute right-3 top-3 text-gray-400">%</span>
          </div>
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
          <label class="block text-gray-700 mb-2 font-medium">Start Date</label>
          <input 
            type="date" 
            class="input" 
            bind:value={startDate}
          />
        </div>
      </div>
    </div>
    
    <div>
      <h2 class="text-2xl font-bold text-oxford-blue mb-6">Payment Summary</h2>
      
      <div class="space-y-4 mb-8">
        <div class="flex justify-between items-center py-3 border-b border-gray-200">
          <span class="text-gray-600">Monthly Payment</span>
          <span class="text-xl font-bold text-oxford-blue">{formatCurrency(monthlyPayment)}</span>
        </div>
        
        <div class="flex justify-between items-center py-3 border-b border-gray-200">
          <span class="text-gray-600">Total Principal</span>
          <span class="text-lg font-semibold">{formatCurrency(loanAmount)}</span>
        </div>
        
        <div class="flex justify-between items-center py-3 border-b border-gray-200">
          <span class="text-gray-600">Total Interest</span>
          <span class="text-lg font-semibold text-red-500">{formatCurrency(totalInterest)}</span>
        </div>
        
        <div class="flex justify-between items-center py-3 border-b border-gray-200">
          <span class="text-gray-600">Total Payment</span>
          <span class="text-xl font-bold">{formatCurrency(totalPayment)}</span>
        </div>
        
        <div class="flex justify-between items-center py-3">
          <span class="text-gray-600">Interest to Principal Ratio</span>
          <span class="text-lg font-semibold">
            {((totalInterest / loanAmount) * 100).toFixed(1)}%
          </span>
        </div>
      </div>
      
      <a href="/apply" class="btn btn-secondary w-full">Apply for This Loan</a>
    </div>
  </div>
  
  <div class="mt-12">
    <h2 class="text-2xl font-bold text-oxford-blue mb-6">Amortization Schedule</h2>
    
    <div class="overflow-x-auto">
      <table class="w-full">
        <thead>
          <tr class="bg-gray-100">
            <th class="py-3 px-4 text-left">Payment</th>
            <th class="py-3 px-4 text-left">Date</th>
            <th class="py-3 px-4 text-right">Payment</th>
            <th class="py-3 px-4 text-right">Principal</th>
            <th class="py-3 px-4 text-right">Interest</th>
            <th class="py-3 px-4 text-right">Cumulative Interest</th>
            <th class="py-3 px-4 text-right">Balance</th>
          </tr>
        </thead>
        <tbody>
          {#each amortizationSchedule.slice(0, 12) as payment}
            <tr class="border-b border-gray-200 hover:bg-gray-50">
              <td class="py-3 px-4">{payment.paymentNumber}</td>
              <td class="py-3 px-4">{payment.paymentDate}</td>
              <td class="py-3 px-4 text-right">{formatCurrency(payment.paymentAmount)}</td>
              <td class="py-3 px-4 text-right">{formatCurrency(payment.principalPayment)}</td>
              <td class="py-3 px-4 text-right">{formatCurrency(payment.interestPayment)}</td>
              <td class="py-3 px-4 text-right">{formatCurrency(payment.cumulativeInterest)}</td>
              <td class="py-3 px-4 text-right">{formatCurrency(payment.balance)}</td>
            </tr>
          {/each}
        </tbody>
      </table>
    </div>
    
    {#if amortizationSchedule.length > 12}
      <div class="mt-4 text-center">
        <p class="text-gray-600">Showing first 12 of {amortizationSchedule.length} payments</p>
      </div>
    {/if}
  </div>
</div>