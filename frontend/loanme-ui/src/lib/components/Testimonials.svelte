<section class="py-20 bg-gray-50">
  <div class="container mx-auto px-4">
    <div class="text-center mb-16">
      <h2 class="text-3xl md:text-4xl font-bold text-oxford-blue mb-4">What Our Customers Say</h2>
      <p class="text-xl text-gray-600 max-w-2xl mx-auto">
        Don't just take our word for it. Hear from people who have experienced our service.
      </p>
    </div>
    
    <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
      <div class="card bg-white p-8">
        <div class="flex items-center mb-6">
          <div class="bg-gray-200 border-2 border-dashed rounded-xl w-16 h-16" />
          <div class="ml-4">
            <h4 class="font-bold text-lg">Sarah Johnson</h4>
            <p class="text-gray-600">Personal Loan</p>
          </div>
        </div>
        <p class="text-gray-600 italic">
          "The process was incredibly smooth. I got my funds within 24 hours of approval. The rates are fantastic compared to traditional banks."
        </p>
        <div class="flex mt-4">
          {#each [1, 2, 3, 4, 5] as i}
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-yellow-400" viewBox="0 0 20 20" fill="currentColor">
              <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
            </svg>
          {/each}
        </div>
      </div>
      
      <div class="card bg-white p-8">
        <div class="flex items-center mb-6">
          <div class="bg-gray-200 border-2 border-dashed rounded-xl w-16 h-16" />
          <div class="ml-4">
            <h4 class="font-bold text-lg">Michael Chen</h4>
            <p class="text-gray-600">Business Loan</p>
          </div>
        </div>
        <p class="text-gray-600 italic">
          "As a small business owner, I needed quick funding. LoanMe delivered exactly what they promised with no surprises. Highly recommended!"
        </p>
        <div class="flex mt-4">
          {#each [1, 2, 3, 4, 5] as i}
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-yellow-400" viewBox="0 0 20 20" fill="currentColor">
              <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
            </svg>
          {/each}
        </div>
      </div>
      
      <div class="card bg-white p-8">
        <div class="flex items-center mb-6">
          <div class="bg-gray-200 border-2 border-dashed rounded-xl w-16 h-16" />
          <div class="ml-4">
            <h4 class="font-bold text-lg">David Rodriguez</h4>
            <p class="text-gray-600">Auto Loan</p>
          </div>
        </div>
        <p class="text-gray-600 italic">
          "The calculator helped me understand my options before I applied. The approval process was seamless and the rates were better than my bank."
        </p>
        <div class="flex mt-4">
          {#each [1, 2, 3, 4, 5] as i}
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-yellow-400" viewBox="0 0 20 20" fill="currentColor">
              <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
            </svg>
          {/each}
        </div>
      </div>
    </div>
  </div>
</section>