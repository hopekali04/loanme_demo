<script>
  import Logo from '$lib/components/Logo.svelte';
</script>

<footer class="bg-oxford-blue text-white py-12">
  <div class="container mx-auto px-4">
    <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
      <div>
        <Logo size="small" />
        <p class="mt-4 text-gray-300">
          Making loans simple, transparent, and accessible for everyone.
        </p>
      </div>
      
      <div>
        <h3 class="text-lg font-semibold mb-4">Products</h3>
        <ul class="space-y-2">
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">Personal Loans</a></li>
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">Business Loans</a></li>
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">Auto Loans</a></li>
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">Mortgage Loans</a></li>
        </ul>
      </div>
      
      <div>
        <h3 class="text-lg font-semibold mb-4">Company</h3>
        <ul class="space-y-2">
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">About Us</a></li>
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">Careers</a></li>
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">Contact</a></li>
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">Blog</a></li>
        </ul>
      </div>
      
      <div>
        <h3 class="text-lg font-semibold mb-4">Support</h3>
        <ul class="space-y-2">
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">Help Center</a></li>
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">Privacy Policy</a></li>
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">Terms of Service</a></li>
          <li><a href="#" class="text-gray-300 hover:text-maize transition-colors">FAQ</a></li>
        </ul>
      </div>
    </div>
    
    <div class="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
      <p>&copy; {new Date().getFullYear()} LoanMe. All rights reserved.</p>
    </div>
  </div>
</footer>