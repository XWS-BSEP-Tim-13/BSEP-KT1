import classes from './Login.module.css';

function Login() {

    function submitHandler(event) {
        event.preventDefault();
        console.log("Submited");
    }

    return (
        <div className={classes.page}>
            <div className={classes.login}>
                <h1>LogIn</h1>
                <form onSubmit={submitHandler} className={classes.form}>
                    <div className={classes.formItem}>
                        <label htmlFor='username' >Username: </label>
                        <input type='text' required id='username' />
                    </div>
                    <div className={classes.formItem}>
                        <label htmlFor='password' >Password:&nbsp; </label>
                        <input type='password' required id='password' />
                    </div>
                    <button className={classes.buttonLogIn}>LogIn</button>
                    <a href="localhost:3000" className={classes.registerLink}>Don't have an account? Register here.</a>
                </form>
            </div>
            <div className={classes.image}></div>
        </div>
    );
}

export default Login;