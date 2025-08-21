# UI Documentation

This document provides detailed information about the SvelteKit frontend implementation for the LoanMe application, including the component structure, design system, and development guidelines.

## Design Principles

The LoanMe UI follows modern design principles with a focus on usability, accessibility, and performance:

- **Apple-inspired Design**: Clean, minimalist interface with generous whitespace and subtle animations
- **Responsive First**: Mobile-first approach that works beautifully on all device sizes
- **Accessibility**: WCAG AA compliance with proper semantic HTML and ARIA attributes
- **Performance**: Optimized bundle size and lazy loading for fast page loads
- **Consistency**: Unified design system with reusable components

## Technology Stack

- **Framework**: [SvelteKit](https://kit.svelte.dev/) - Web development, streamlined
- **Styling**: [Tailwind CSS](https://tailwindcss.com/) - Utility-first CSS framework
- **Build Tool**: [Vite](https://vitejs.dev/) - Next generation frontend tooling
- **Language**: JavaScript/TypeScript
- **Package Manager**: npm

## Project Structure

```
frontend/loanme-ui/
├── src/
│   ├── lib/
│   │   ├── components/     # Reusable components
│   │   │   ├── ui/         # Generic UI components
│   │   │   ├── auth/       # Authentication components
│   │   │   ├── loan/       # Loan-specific components
│   │   │   └── admin/      # Admin dashboard components
│   │   └── utils/          # Utility functions
│   ├── routes/             # Page routes
│   │   ├── apply/          # Loan application pages
│   │   ├── calculator/     # Loan calculator pages
│   │   ├── dashboard/      # User dashboard pages
│   │   ├── login/          # Authentication pages
│   │   └── register/       # Registration pages
│   ├── app.css             # Global styles
│   ├── app.html            # Base HTML template
│   └── hooks/              # SvelteKit hooks
├── static/                 # Static assets
├── tests/                  # Test files
├── package.json            # Dependencies and scripts
├── svelte.config.js        # Svelte configuration
├── tailwind.config.js      # Tailwind configuration
└── vite.config.js          # Vite configuration
```

## Component Library

### UI Components

Located in `src/lib/components/ui/`, these are generic, reusable components:

#### Buttons

```svelte
<!-- Primary button -->
<Button variant="primary">Click me</Button>

<!-- Secondary button -->
<Button variant="secondary">Cancel</Button>

<!-- Icon button -->
<Button variant="icon">
  <Icon name="heart" />
</Button>
```

#### Forms

```svelte
<!-- Text input with validation -->
<Input 
  label="Email Address"
  type="email"
  bind:value={email}
  error={errors.email}
  required
/>

<!-- Select dropdown -->
<Select
  label="Loan Type"
  options={loanTypes}
  bind:value={selectedType}
/>
```

#### Cards

```svelte
<Card>
  <Card.Header>
    <h3>Loan Summary</h3>
  </Card.Header>
  <Card.Body>
    <p>Details here</p>
  </Card.Body>
</Card>
```

### Loan Components

Located in `src/lib/components/loan/`:

#### Loan Calculator

Interactive component for calculating loan payments:

- Real-time calculation as users input values
- Visual amortization schedule
- Responsive chart visualization

#### Application Form

Multi-step form for loan applications:

- Progressive disclosure of form fields
- Inline validation and error messaging
- Auto-save functionality

### Authentication Components

Located in `src/lib/components/auth/`:

#### Login Form

Secure authentication form with:

- Email/password validation
- Remember me option
- Password visibility toggle
- Loading states and error handling

#### Registration Form

User registration with:

- Password strength meter
- Terms and conditions acceptance
- Email verification flow

## Pages

### Home Page

- Hero section with value proposition
- Featured loan calculator
- Key features and benefits
- Call-to-action buttons

### Loan Calculator Page

- Full-screen loan calculator
- Detailed amortization schedule
- Payment breakdown visualization
- Save and share functionality

### Loan Application Pages

Multi-step application process:

1. **Personal Information**: Basic user details
2. **Employment Details**: Income and employment verification
3. **Financial Information**: Expenses and existing debt
4. **Loan Details**: Amount, term, and purpose
5. **Review & Submit**: Summary and confirmation

### User Dashboard

- Application status tracking
- Submitted applications list
- Profile management
- Document upload area

### Admin Dashboard

- Loan application management
- User account administration
- System analytics and reporting
- Audit log review

## Design System

### Color Palette

| Color Name | HEX | Usage |
|------------|-----|-------|
| Oxford Blue | #0A174A | Primary brand color, headers |
| Maize | #F5D042 | Accent color, buttons, highlights |
| White | #FFFFFF | Backgrounds, text |
| Black | #000000 | Text, borders |
| Gray 100 | #F5F5F5 | Light backgrounds |
| Gray 500 | #9CA3AF | Secondary text, borders |
| Gray 800 | #1F2937 | Primary text |

### Typography

- **Font Family**: System UI font stack (-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif)
- **Headings**: Bold, larger sizes for hierarchy
- **Body Text**: Normal weight, readable size
- **Line Height**: 1.5 for paragraphs, 1.2 for headings

### Spacing Scale

Based on an 8px grid system:

- 2xs: 4px
- xs: 8px
- sm: 12px
- md: 16px
- lg: 24px
- xl: 32px
- 2xl: 48px

### Breakpoints

| Size | Minimum Width | Container Width |
|------|---------------|-----------------|
| sm | 640px | 100% |
| md | 768px | 100% |
| lg | 1024px | 1024px |
| xl | 1280px | 1280px |
| 2xl | 1536px | 1536px |

## State Management

### Client-Side State

- Uses Svelte's built-in reactivity (`$:` statements, reactive assignments)
- Stores authentication tokens in localStorage
- Caches API responses in component variables

### Form State

- Uses Svelte's two-way data binding
- Implements custom validation functions
- Handles loading and error states

### Global State

- Authentication state managed in a session store
- Theme preferences stored in localStorage
- Notification messages handled through a toast system

## Routing

SvelteKit's file-based routing system maps files in `src/routes/` to URLs:

```
src/routes/
├── +page.svelte              → /
├── calculator/
│   └── +page.svelte          → /calculator
├── apply/
│   ├── +page.svelte          → /apply
│   └── +layout.svelte        → Layout for all apply routes
├── dashboard/
│   ├── +layout.svelte        → Protected layout
│   ├── +page.svelte          → /dashboard
│   └── applications/
│       └── +page.svelte      → /dashboard/applications
├── login/
│   └── +page.svelte          → /login
└── register/
    └── +page.svelte          → /register
```

### Route Protection

- Uses SvelteKit hooks for authentication checks
- Redirects unauthenticated users to login
- Provides role-based access control for admin routes

## Styling with Tailwind CSS

### Utility Classes

- Consistent spacing using the spacing scale
- Responsive design with breakpoint prefixes
- Color system using defined palette names
- Typography utilities for consistent text styles

### Custom Components

- Extends Tailwind with component classes in `src/app.css`
- Uses `@apply` directive for complex utility combinations
- Implements dark mode variants where appropriate

## Accessibility Features

### Semantic HTML

- Proper heading hierarchy (h1-h6)
- Landmark elements (header, main, nav, footer)
- ARIA attributes for interactive components

### Keyboard Navigation

- Focus management for modal dialogs
- Skip links for main content
- Logical tab order

### Screen Reader Support

- Descriptive alt text for images
- ARIA labels for icon-only buttons
- Live regions for dynamic content updates

## Performance Optimizations

### Bundle Optimization

- Code splitting by route
- Dynamic imports for heavy components
- Tree-shaking unused dependencies

### Image Optimization

- Responsive images with `picture` element
- Modern formats (WebP) where supported
- Lazy loading for off-screen images

### Caching Strategies

- Service worker for offline functionality
- HTTP caching headers for static assets
- Client-side caching of API responses

## Testing

### Component Testing

- Unit tests for individual components
- Snapshot testing for UI consistency
- Interaction testing with user events

### End-to-End Testing

- User flow testing with Playwright
- Cross-browser compatibility verification
- Accessibility testing

### Visual Regression

- Screenshot testing for UI changes
- Responsive design verification
- Theme switching validation

## Development Workflow

### Component Development

1. Create new components in `src/lib/components/`
2. Follow established patterns and conventions
3. Add stories for component documentation
4. Write tests for new functionality

### Page Development

1. Create new route files in `src/routes/`
2. Implement page layout and structure
3. Connect to backend APIs
4. Add navigation and routing

### Styling Guidelines

1. Use Tailwind utility classes primarily
2. Create component classes for repeated patterns
3. Follow the defined color and spacing systems
4. Maintain responsive design principles

## Deployment

### Build Process

```bash
npm run build
```

The build process:

- Compiles Svelte components
- Optimizes CSS and JavaScript
- Generates static assets
- Creates service worker for PWA

### Environment Variables

- `VITE_API_BASE_URL`: Backend API endpoint
- `VITE_APP_TITLE`: Application title
- `VITE_ENVIRONMENT`: Environment identifier (dev, staging, prod)

### Hosting

- Can be deployed as a static site
- Works with CDN distribution
- Compatible with server-side rendering

## Contributing to UI

### Code Standards

- Follow Svelte best practices
- Maintain consistent component structure
- Use TypeScript for type safety
- Write meaningful commit messages

### Component Design

- Keep components focused and single-purpose
- Use props for customization
- Provide clear documentation
- Include accessibility features

### Pull Request Guidelines

1. Ensure all tests pass
2. Update documentation if needed
3. Follow the established code style
4. Include screenshots for visual changes

This UI documentation provides a comprehensive guide to understanding, developing, and maintaining the SvelteKit frontend for the LoanMe application. It ensures consistency and quality across all user interface elements while providing clear guidelines for future development.
