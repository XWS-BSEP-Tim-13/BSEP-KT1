import { Link } from "react-router-dom";
import classes from "./Navbar.module.css";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBars } from "@fortawesome/free-solid-svg-icons";
import { faRightFromBracket } from "@fortawesome/free-solid-svg-icons";

import { useState } from "react";
import { useDispatch } from "react-redux";
import { logout } from "../../features/user";

function Navbar() {
    var [mobileNavClicked, setMobileNavClicked] = useState(false);

    const dispatch = useDispatch();

    function handleLogOut() {
        dispatch(logout());
    }

    return (
        <div>
            {mobileNavClicked ? (<div className={classes.backdrop} onClick={() => setMobileNavClicked(false)} ></div>) : null}

            <nav className={classes.nav}>
                <div>
                    <Link to="/home" className={classes.link}>
                        Home
                    </Link>
                </div>
                <div>
                    <Link to="/allCertificates" className={classes.link}>
                        All Certificates
                    </Link>
                    <Link to="/newCertificate" className={classes.link}>
                        New Certificate
                    </Link>
                    <Link to="/" className={classes.link}>
                        LogOut <FontAwesomeIcon icon={faRightFromBracket}  onClick={handleLogOut}/>
                    </Link>
                    <FontAwesomeIcon icon={faBars} className={classes.menuIcon} onClick={() => setMobileNavClicked(true)} />
                </div>
            </nav>

            {mobileNavClicked ? (
                <nav className={classes.mobileNav}>
                    <div className={classes.mobileNavLinks}>
                        <Link to="/home" className={classes.mobileLink} onClick={() => setMobileNavClicked(false)} >
                            Home
                        </Link>
                        <Link to="/allCertificates" className={classes.mobileLink} onClick={() => setMobileNavClicked(false)} >
                            All Certificates
                        </Link>
                        <Link to="/newCertificate" className={classes.mobileLink} onClick={() => setMobileNavClicked(false)}  >
                            New Certificate
                        </Link>
                        <Link to="/" className={classes.mobileLink}>
                            LogOut <FontAwesomeIcon icon={faRightFromBracket} />
                        </Link>
                    </div>
                </nav>
            ) : null}
        </div>
    );
}

export default Navbar;
