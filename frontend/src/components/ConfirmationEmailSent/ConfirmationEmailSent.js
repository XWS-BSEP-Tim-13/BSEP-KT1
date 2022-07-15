import classes from './ConfirmationEmailSent.module.css';

function ConfirmationEmailSent(props) {
    return (
        <div className={classes.component}>
            <div className={classes.message}>
                <p>The activation link has been sent to your e-mail address.</p>
                <p>Please check your mailbox to activate your account!</p>
            </div>


            <a href="/#" className={classes.registerLink} onClick={() => props.navigateToLogin()}>
                Already have an account? Log in here!
            </a>
            <a href="/#" className={classes.registerLink} onClick={() => props.navigateToRegister()} >
                Don't have an account? Register here.
            </a>
        </div>
    );
}

export default ConfirmationEmailSent;