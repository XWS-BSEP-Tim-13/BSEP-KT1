import Login from "../../components/Login/Login";
import Registration from "../../components/Registration/Registration";
import ForgotPassword from "../../components/Login/ForgotPassword/ForgotPassword ";
import ConfirmationEmailSent from "../../components/ConfirmationEmailSent/ConfirmationEmailSent";

import classes from './Mainpage.module.css';

import { useState } from 'react';

function Mainpage() {
    const [isLoginPage, setIsLoginPage] = useState(true);
    const [isRegisterPage, setIsRegisterPage] =useState(false)
    const [isForgotPasswordPage,setIsForgotPasswordPage]= useState(false)
    const [isLinkSentPage, setIsLinkSentPage] = useState(false);

    function navigateToLogin() {
        setIsLoginPage(true);
        setIsRegisterPage(false);
        setIsForgotPasswordPage(false);
        setIsLinkSentPage(false);
    }

    function navigateToRegister() {
        setIsLoginPage(false);
        setIsRegisterPage(true);
        setIsForgotPasswordPage(false);
        setIsLinkSentPage(false);
    }

    function navigateToLinkSent() {
        setIsLoginPage(false);
        setIsRegisterPage(false);
        setIsForgotPasswordPage(false);
        setIsLinkSentPage(true);
    }

    function navigateToForgotPasswordPage(){
        setIsLoginPage(false);
        setIsRegisterPage(false);
        setIsForgotPasswordPage(true);
        setIsLinkSentPage(false);
    }

    return (
        <div className={classes.page}>
            { isLoginPage ? <Login navigateToRegister={navigateToRegister} navigateToForgotPasswordPage={navigateToForgotPasswordPage}/> : null }
            { isRegisterPage ? <Registration navigateToLogin={navigateToLogin} navigateToLinkSent={navigateToLinkSent} /> : null}
            { isForgotPasswordPage ? <ForgotPassword changePage={navigateToLogin} /> : null}
            { isLinkSentPage ? <ConfirmationEmailSent navigateToLogin={navigateToLogin} navigateToRegister={navigateToRegister} /> : null}
            <div className={classes.image}></div>
        </div>
    );
}

export default Mainpage;