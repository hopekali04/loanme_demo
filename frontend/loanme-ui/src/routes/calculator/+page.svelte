<script>
  import LoanCalculator from '$lib/components/LoanCalculator.svelte';
</script>

<svelte:head>
  <title>Loan Calculator | LoanMe</title>
  <meta name="description" content="Calculate your loan payments with our free loan calculator. See monthly payments, total interest, and amortization schedule." />
</svelte:head>

<section class="pt-32 pb-20">
  <div class="container mx-auto px-4">
    <div class="text-center mb-16">
      <h1 class="text-3xl md:text-4xl font-bold text-oxford-blue mb-4">Loan Calculator</h1>
      <p class="text-xl text-gray-600 max-w-2xl mx-auto">
        Calculate your monthly payments, total interest, and see the full amortization schedule.
      </p>
    </div>
    
    <div class="max-w-4xl mx-auto">
      <LoanCalculator />
    </div>
  </div>
</section>