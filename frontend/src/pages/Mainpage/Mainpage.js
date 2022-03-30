import Login from "../../components/Login/Login";
import Registration from "../../components/Registration/Registration";

import classes from './Mainpage.module.css';

import { useState } from 'react';

function Mainpage() {
    const [isLoginPage, setIsLoginPage] = useState(true);

    return (
        <div className={classes.page}>
            { isLoginPage ? <Login changePage={setIsLoginPage} /> : null }
            { !isLoginPage ? <Registration changePage={setIsLoginPage} /> : null}
            <div className={classes.image}></div>
        </div>
    );
}

export default Mainpage;