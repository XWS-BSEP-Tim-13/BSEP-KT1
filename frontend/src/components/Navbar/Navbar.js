import { Link } from 'react-router-dom';
import classes from './Navbar.module.css';

function Navbar() {
    return (
        <nav className={classes.nav}>
            <div>
                <Link to="/home" className={classes.link} >Home</Link>
            </div>
            <div>
                <Link to="/allSertificates" className={classes.link} >All Sertificates</Link>
                <Link to="/newSertificate" className={classes.link} >New Sertificate</Link>
            </div>
        </nav>
    );
}

export default Navbar;