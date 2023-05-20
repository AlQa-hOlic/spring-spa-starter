import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    outDir: '../build/ts',
    emptyOutDir: true,
    assetsDir: 'static',
    // generate manifest.json in outDir
    manifest: true,
    rollupOptions: {
      // overwrite default .html entry
      input: './src/main.tsx',
    },
  },
  base: process.env.mode === 'production' ? '/static/' : '/',
  plugins: [react()],
})
