import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCertificate } from "@fortawesome/free-solid-svg-icons";

import classes from './NewCertificate.module.css';

function NewCertificate() {

    return (
        <div className={classes.page}>
            <div className={classes.title}>
                <FontAwesomeIcon icon={faCertificate} />
                <h1 className={classes.titleText}>Add New Certificate</h1>
            </div>

            <form className={classes.form}>
                <div className={classes.formRow}>
                    <div className={classes.formColumn}>
                        <select name="Subject" defaultValue="">
                            <option value="" disabled>Subject</option>
                            <option value="first">First option</option>
                            <option value="second">Second option</option>
                            <option value="third">Third option</option>
                        </select>
                        <select name="Issuer" defaultValue="">
                            <option value="" disabled>Issuer</option>
                            <option value="first">First option</option>
                            <option value="second">Second option</option>
                            <option value="third">Third option</option>
                        </select>
                        <select name="CertificateType" defaultValue="">
                            <option value="" disabled>Certificate type</option>
                            <option value="root">Root</option>
                            <option value="intermediate">Intermediate</option>
                            <option value="endEntity">End Entity</option>
                        </select>
                        <input type='text' required placeholder='Valid From' onFocus={(e) => (e.target.type = "date")} onBlur={(e) => (e.target.type = "text")}/>
                        <input type='text' required placeholder='Valid To' onFocus={(e) => (e.target.type = "date")} onBlur={(e) => (e.target.type = "text")}/>
                    </div>
                    <div className={classes.formColumn}>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="provesIdentity" />
                            <value for="provesIdentity">Proves the identity to a remote computer</value>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="ensuresIdentity" />
                            <value for="ensuresIdentity">Ensures the idnetity of a remote computer</value>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="encryptedData" />
                            <value for="encryptedData">Allows data on disk to be encrypted</value>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="emailMessages" />
                            <value for="emailMessages">Protects e-mail messages</value>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="secureCommunication" />
                            <value for="secureCommunication">Allows secure communication on the Internet</value>
                        </div>
                    </div>
                </div>
                <button className={classes.button}>Create!</button>
            </form>
        </div>
    );
}

export default NewCertificate;

