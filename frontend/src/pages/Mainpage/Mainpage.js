import Login from "../../components/Login/Login";
import Registration from "../../components/Registration/Registration";
import ConfirmationEmailSent from "../../components/ConfirmationEmailSent/ConfirmationEmailSent";

import classes from './Mainpage.module.css';

import { useState } from 'react';

function Mainpage() {
    const [isLoginPage, setIsLoginPage] = useState(true);
    const [isRegisterPage, setIsRegisterPage] = useState(false);
    const [isLinkSentPage, setIsLinkSentPage] = useState(false);

    function navigateToLogin() {
        setIsLoginPage(true);
        setIsRegisterPage(false);
        setIsLinkSentPage(false);
    }

    function navigateToRegister() {
        setIsLoginPage(false);
        setIsRegisterPage(true);
        setIsLinkSentPage(false);
    }

    function navigateToLinkSent() {
        setIsLoginPage(false);
        setIsRegisterPage(false);
        setIsLinkSentPage(true);
    }

    return (
        <div className={classes.page}>
            { isLoginPage ? <Login navigateToRegister={navigateToRegister} /> : null }
            { isRegisterPage ? <Registration navigateToLogin={navigateToLogin} navigateToLinkSent={navigateToLinkSent} /> : null}
            { isLinkSentPage ? <ConfirmationEmailSent navigateToLogin={navigateToLogin} navigateToRegister={navigateToRegister} /> : null}
            <div className={classes.image}></div>
        </div>
    );
}

export default Mainpage;