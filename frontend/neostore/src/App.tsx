import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { Button } from './components/button/button'
import ListagemFornecedores from './pages/listagemFornecedores'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
        <ListagemFornecedores />
    </>
  )
}

export default App
