# Contributing to Vync Video Player

Thank you for your interest in contributing to Vync Video Player! This document provides guidelines and information for contributors.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Contributing Guidelines](#contributing-guidelines)
- [Code Style](#code-style)
- [Testing](#testing)
- [Pull Request Process](#pull-request-process)
- [Release Process](#release-process)
- [Contact](#contact)

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code.

### Our Standards

- **Be respectful** and inclusive of all contributors
- **Be collaborative** and open to different viewpoints
- **Be constructive** in feedback and discussions
- **Be professional** in all interactions

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Node.js** 16.0 or higher
- **Yarn** 1.22 or higher (recommended) or npm 8.0+
- **React Native CLI** (for native development)
- **Xcode** 13.0+ (for iOS development)
- **Android Studio** (for Android development)
- **Expo CLI** (for Expo development)

### Fork and Clone

1. **Fork** the repository on GitHub
2. **Clone** your fork locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/vync-video-player.git
   cd vync-video-player
   ```
3. **Add upstream** remote:
   ```bash
   git remote add upstream https://github.com/ZynclaveTech/vync-video-player.git
   ```

## Development Setup

### Install Dependencies

```bash
# Install project dependencies
yarn install

# Install example app dependencies
cd example
yarn install
cd ..
```

### Build the Project

```bash
# Build the main package
yarn build

# Clean build artifacts
yarn clean

# Prepare for development
yarn prepare
```

### Run the Example App

```bash
# Start the example app
cd example
yarn start

# Run on iOS
yarn ios

# Run on Android
yarn android

# Run on web
yarn web
```

## Contributing Guidelines

### What We're Looking For

We welcome contributions in the following areas:

#### 🚀 **Features**
- New video player capabilities
- Performance improvements
- Platform-specific enhancements
- Accessibility improvements

#### 🐛 **Bug Fixes**
- Bug reports with reproducible steps
- Fixes for existing issues
- Performance optimizations
- Memory leak fixes

#### 📚 **Documentation**
- README improvements
- API documentation updates
- Code examples
- Tutorial guides

#### 🧪 **Testing**
- Unit test coverage
- Integration tests
- Platform-specific tests
- Performance benchmarks

#### 🔧 **Infrastructure**
- Build system improvements
- CI/CD enhancements
- Development tooling
- Code quality tools

### Issue Guidelines

#### Before Submitting an Issue

1. **Search existing issues** to avoid duplicates
2. **Check the documentation** for solutions
3. **Reproduce the issue** with clear steps
4. **Provide environment details** (OS, React Native version, etc.)

#### Issue Template

```markdown
## Issue Description
Brief description of the issue

## Steps to Reproduce
1. Step one
2. Step two
3. Step three

## Expected Behavior
What you expected to happen

## Actual Behavior
What actually happened

## Environment
- OS: [e.g., macOS 12.0, Windows 11]
- React Native: [e.g., 0.70.0]
- Expo: [e.g., SDK 47]
- Device: [e.g., iPhone 13, Pixel 6]

## Additional Information
Any other context, logs, or screenshots
```

## Code Style

### General Guidelines

- **Follow existing patterns** in the codebase
- **Write self-documenting code** with clear variable names
- **Add comments** for complex logic
- **Keep functions small** and focused
- **Use meaningful commit messages**

### TypeScript Guidelines

- **Use strict TypeScript** configurations
- **Define proper types** for all functions and variables
- **Avoid `any` type** - use proper typing
- **Export types** that users might need
- **Use interfaces** for object shapes

### React Native Guidelines

- **Follow React Native best practices**
- **Use functional components** with hooks
- **Implement proper error boundaries**
- **Handle platform differences** gracefully
- **Optimize for performance**

### Native Code Guidelines

#### iOS (Swift)
- **Follow Swift style guidelines**
- **Use proper memory management**
- **Handle errors gracefully**
- **Add documentation comments**

#### Android (Kotlin)
- **Follow Kotlin conventions**
- **Use proper lifecycle management**
- **Handle exceptions properly**
- **Add KDoc comments**

### File Naming

- **Use PascalCase** for component files: `VideoPlayerView.tsx`
- **Use camelCase** for utility files: `videoUtils.ts`
- **Use kebab-case** for native files: `video-player-module.swift`

## Testing

### Running Tests

```bash
# Run all tests
yarn test

# Run tests in watch mode
yarn test:watch

# Run tests with coverage
yarn test:coverage

# Run specific test files
yarn test -- VideoPlayerView.test.tsx
```

### Writing Tests

- **Test all public APIs**
- **Test edge cases** and error conditions
- **Test platform-specific behavior**
- **Use descriptive test names**
- **Mock external dependencies**

### Test Structure

```typescript
describe('VideoPlayerView', () => {
  describe('props', () => {
    it('should render with url prop', () => {
      // Test implementation
    });
    
    it('should handle invalid url gracefully', () => {
      // Test implementation
    });
  });
  
  describe('events', () => {
    it('should fire onStatusChange when playing', () => {
      // Test implementation
    });
  });
});
```

## Pull Request Process

### Before Submitting a PR

1. **Ensure tests pass** locally
2. **Update documentation** if needed
3. **Follow the commit message convention**
4. **Rebase on main** to avoid conflicts

### Commit Message Convention

We use [Conventional Commits](https://www.conventionalcommits.org/):

```
type(scope): description

[optional body]

[optional footer]
```

#### Types
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

#### Examples
```
feat(video): add picture-in-picture support
fix(android): resolve memory leak in fullscreen mode
docs(readme): add installation examples
test(player): add unit tests for error handling
```

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Tests pass locally
- [ ] Added new tests
- [ ] Updated existing tests

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Documentation updated
- [ ] No console errors
- [ ] No TypeScript errors

## Screenshots (if applicable)
Add screenshots for UI changes

## Additional Notes
Any additional information
```

### PR Review Process

1. **Automated checks** must pass
2. **Code review** by maintainers
3. **Address feedback** and make changes
4. **Maintainer approval** required
5. **Merge** after approval

## Release Process

### Version Bumping

We use [semantic versioning](https://semver.org/):

- **Patch** (0.0.x): Bug fixes and minor improvements
- **Minor** (0.x.0): New features, backward compatible
- **Major** (x.0.0): Breaking changes

### Release Steps

1. **Create release branch** from main
2. **Update version** in package.json
3. **Update changelog** with new features/fixes
4. **Run tests** and build
5. **Create PR** for release
6. **Merge** after review
7. **Tag release** on GitHub
8. **Publish to npm**

## Development Workflow

### Branch Strategy

- **main**: Production-ready code
- **develop**: Development branch
- **feature/***: New features
- **bugfix/***: Bug fixes
- **release/***: Release preparation

### Workflow Steps

1. **Create feature branch** from develop
2. **Make changes** and commit
3. **Push branch** and create PR
4. **Code review** and feedback
5. **Merge** to develop
6. **Create release** from develop
7. **Merge** to main and tag

## Contact

### Getting Help

- **GitHub Issues**: For bugs and feature requests
- **GitHub Discussions**: For questions and discussions
- **Email**: support@zynclave.tech

### Maintainers

- **Lead Maintainer**: [Maintainer Name]
- **Core Contributors**: [List of core contributors]

### Community

- **Join our discussions** on GitHub
- **Follow updates** on our releases
- **Share feedback** and suggestions

## Recognition

### Contributors

We recognize all contributors in our:
- **README.md** contributors section
- **GitHub contributors** page
- **Release notes** and changelog

### Hall of Fame

Special recognition for:
- **Major contributions** to the project
- **Long-term involvement** in the community
- **Outstanding documentation** or examples

---

Thank you for contributing to Vync Video Player! Your contributions help make this library better for the entire React Native community.

**Happy coding! 🎉**
