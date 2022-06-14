import { useLocation } from 'react-router-dom';
import { Route, Routes } from 'react-router-dom';

import Homepage from './pages/Homepage/Homepage';
import Mainpage from './pages/Mainpage/Mainpage';
import Navbar from './components/Navbar/Navbar';
import AllCertificates from './pages/AllCertificates/AllCertificates';
import NewCertificate from './pages/NewCertificate/NewCertificate';
import ChangePassword from './components/Login/ChangePassword/ChangePassword';
import './App.css';

function App() {
    const location = useLocation();

    return (
        <div className="App">
            { location.pathname !== '/'  && location.pathname !== '/forgot-password' && !location.pathname.includes("/change-password/") ? <Navbar /> : null }
            <Routes>
                <Route path='/' element={ <Mainpage /> } />
                <Route path='/home' element={ <Homepage /> } />
                <Route path='/newCertificate' element={ <NewCertificate /> } />
                <Route path='/allCertificates' element={ <AllCertificates /> } />
                <Route path='/change-password/:token'  exact  element= {<ChangePassword /> } />
            </Routes>
        </div>
    );
}

export default App;
