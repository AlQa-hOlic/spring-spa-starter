import './main.css'

import React from 'react'
import { createRoot } from 'react-dom/client'

function App() {
  return (
    <div className="min-h-screen flex flex-col justify-center items-center bg-gradient-to-r from-amber-300 to-orange-500">
      <pre className="text-6xl text-white">Hello, world!</pre>
    </div>
  )
}

const el = document.getElementById('app-root')

if (el == null) {
  throw new Error("Root element '#app-root' must be defined!")
} else {
  const root = createRoot(el)
  root.render(<App />)
}
