import { defineConfig } from "vite";
import { viteSingleFile } from "vite-plugin-singlefile";

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    minify: true,
    outDir: "../../build/resources/main/mailTemplates",
    emptyOutDir: true,
    cssCodeSplit: false,
    assetsInlineLimit: 100000000,
    assetsDir: "static",
    rollupOptions: {
      // overwrite default .html entry
      input: {
        index: "./src/index.html",
      },
    },
  },
  root: "src",
  optimizeDeps: { include: [] },
  plugins: [viteSingleFile()],
});
