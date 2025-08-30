module.exports = {
  root: true,
  parser: '@typescript-eslint/parser',
  plugins: ['@typescript-eslint'],
  extends: ['universe/native', 'universe/web'],
  ignorePatterns: ['build'],
  rules: {
    '@typescript-eslint/no-explicit-any': 'off',
  },
};
