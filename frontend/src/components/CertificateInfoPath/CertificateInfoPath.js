import classes from './CertificateInfoPath.module.css';

import { useSelector } from 'react-redux';

function CertificateInfoPath(props) {
    const user = useSelector((state) => state.user.value);

    function revokeCertificateHandler() {
        props.onRevoke();
    }

    return (
        <div className={classes.page}>
            <p className={classes.labelBold}>Certification path:</p>
            <div className={classes.status}>
                
            </div>
            <p className={classes.labelBold}>Certificate status:</p>
            <div className={classes.status}>
                {props.certificate.certificateStatus === 'GOOD' ? 'This certificate is OK.' : null}
                {props.certificate.certificateStatus === 'INVALID' ? 'This certificate is invalid.' : null}
                {props.certificate.certificateStatus === 'REVOKED' ? 'This certificate has been revoked.' : null}
            </div>
            { ((props.certificate.certificateStatus === 'INVALID' || props.certificate.certificateStatus === 'GOOD')
                && user.role === "ROLE_ADMIN") ?
                <button className={classes.button} onClick={revokeCertificateHandler}>Revoke certificate</button> : null
            }
        </div>
    );
}

export default CertificateInfoPath;