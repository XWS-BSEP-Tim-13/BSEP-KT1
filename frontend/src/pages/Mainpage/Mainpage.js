import Login from "../../components/Login/Login";
import Registration from "../../components/Registration/Registration";
import ForgotPassword from "../../components/Login/ForgotPassword/ForgotPassword ";
import classes from './Mainpage.module.css';

import { useState } from 'react';

function Mainpage() {
    const [isLoginPage, setIsLoginPage] = useState(true);
    const [isRegistrationPage,setIsRegistrationPage] =useState(false)
    const [isForgotPasswordPage,setIsForgotPasswordPage]= useState(false)

    function changeToLoginPage(){
        setIsLoginPage(true)
        setIsRegistrationPage(false)
        setIsForgotPasswordPage(false)
    }

    function changeToRegistrationPage(){
        setIsLoginPage(false)
        setIsRegistrationPage(true)
        setIsForgotPasswordPage(false)
    }

    function changeToForgotPasswordPage(){
        setIsLoginPage(false)
        setIsRegistrationPage(false)
        setIsForgotPasswordPage(true)
    }

    return (
        <div className={classes.page}>
            { isLoginPage ? <Login changePageToRegistration={changeToRegistrationPage} changePageToForgotPassword={changeToForgotPasswordPage} /> : null }
            { isRegistrationPage ? <Registration changePage={changeToLoginPage} /> : null}
            { isForgotPasswordPage ? <ForgotPassword changePage={changeToLoginPage} /> : null}
            <div className={classes.image}></div>
        </div>
    );
}

export default Mainpage;