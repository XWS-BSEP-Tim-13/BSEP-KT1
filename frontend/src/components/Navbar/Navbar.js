import { Link } from 'react-router-dom';
import classes from './Navbar.module.css';

function Navbar() {
    return (
        <nav className={classes.nav}>
            <div>
                <Link to="/home" className={classes.link} >Home</Link>
            </div>
            <div>
                <Link to="/allCertificates" className={classes.link} >All Certificates</Link>
                <Link to="/newCertificate" className={classes.link} >New Certificate</Link>
            </div>
        </nav>
    );
}

export default Navbar;