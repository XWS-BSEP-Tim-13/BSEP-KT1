import './App.css';

import { useEffect } from 'react';
import axios from 'axios';

function App() {

  useEffect(() => {
    axios.get('http://localhost:8081/')
    .then((res) => {
        console.log(res.data)
    })
  }, []);

  return (
    <div className="App">
        Nasa aplikacija!
    </div>
  );
}

export default App;
