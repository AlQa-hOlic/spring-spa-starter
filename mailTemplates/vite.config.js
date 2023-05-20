import autoprefixer from 'autoprefixer'
import { resolve } from 'path'
import tailwindcss from 'tailwindcss'
import { defineConfig } from 'vite'
import { viteSingleFile } from 'vite-plugin-singlefile'

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    minify: true,
    outDir: '../build/resources/main/mailTemplates',
    emptyOutDir: true,
    cssCodeSplit: false,
    assetsInlineLimit: 100000000,
    assetsDir: 'static',
    rollupOptions: {
      // overwrite default .html entry
      input: {
        index: resolve(__dirname, 'index.html'),
      },
    },
  },
  css: {
    postcss: {
      plugins: [
        tailwindcss({
          config: {
            content: ['./mailTemplates**/*.{html,js,jsx,ts,tsx}'],
          },
        }),
        autoprefixer(),
      ],
    },
  },
  root: './mailTemplates',
  plugins: [viteSingleFile()],
})
