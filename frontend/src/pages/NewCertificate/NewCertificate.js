import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCertificate } from "@fortawesome/free-solid-svg-icons";

import { useSelector } from "react-redux";
import { useEffect, useState } from "react";
import axios from "axios";

import classes from './NewCertificate.module.css';

function NewCertificate() {
    const [possibleSubjects, setPossibleSubjects] = useState([]);
    const user = useSelector((state) => state.user.value);

    useEffect(() => {
        console.log(user)
        axios.get(`http://localhost:8081/certificate/subjects`, {
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
                'Authorization': `Bearer ${user.accessToken}`
            },
        }).then((response) => {
            setPossibleSubjects(response.data);
            
            console.log(possibleSubjects);
        })
    }, []);

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
                        {user.role === 'ROLE_ADMIN' ?
                            <select name="Issuer" defaultValue="">
                                <option value="" disabled>Issuer</option>
                                <option value="first">First option</option>
                                <option value="second">Second option</option>
                                <option value="third">Third option</option>
                            </select> :
                            <input type='text' disabled value={`Issuer: ${user.commonName}`}/>
                        }
                        <select name="Signer Certificate" defaultValue="">
                            <option value="" disabled>Signer Certificate</option>
                            <option value="first">First option</option>
                            <option value="second">Second option</option>
                            <option value="third">Third option</option>
                        </select>
                        <select name="CertificateType" defaultValue="">
                            <option value="" disabled>Certificate type</option>
                            {user.role === 'ROLE_ADMIN' ?
                                <option value="root">Root</option> : null
                            }
                            <option value="intermediate">Intermediate</option>
                            <option value="endEntity">End Entity</option>
                        </select>
                        <input type='text' required placeholder='Valid From' onFocus={(e) => (e.target.type = "date")} onBlur={(e) => (e.target.type = "text")} />
                        <input type='text' required placeholder='Valid To' onFocus={(e) => (e.target.type = "date")} onBlur={(e) => (e.target.type = "text")}/>
                    </div>
                    <div className={classes.formColumn}>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="provesIdentity" />
                            <label htmlFor="provesIdentity">Proves the identity to a remote computer</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="ensuresIdentity" />
                            <label htmlFor="ensuresIdentity">Ensures the idnetity of a remote computer</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="encryptedData" />
                            <label htmlFor="encryptedData">Allows data on disk to be encrypted</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="emailMessages" />
                            <label htmlFor="emailMessages">Protects e-mail messages</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="secureCommunication" />
                            <label htmlFor="secureCommunication">Allows secure communication on the Internet</label>
                        </div>
                        <div className={classes.checkbox}>
                            <input type='checkbox' id="signData" />
                            <label htmlFor="signData">Allows data to be signed with the current time</label>
                        </div>
                    </div>
                </div>
                <button className={classes.button}>Create!</button>
            </form>
        </div>
    );
}

export default NewCertificate;

