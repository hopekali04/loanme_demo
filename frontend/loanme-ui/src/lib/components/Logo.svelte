<script>
  export let size = 'large';
</script>

<div class="flex items-center">
  <div class="bg-oxford-blue rounded-xl p-2 mr-3">
    <svg xmlns="http://www.w3.org/2000/svg" class="{size === 'large' ? 'w-8 h-8' : 'w-6 h-6'} text-maize" fill="none" viewBox="0 0 24 24" stroke="currentColor">
      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
    </svg>
  </div>
  <span class="{size === 'large' ? 'text-2xl' : 'text-xl'} font-bold text-oxford-blue">LoanMe</span>
</div>