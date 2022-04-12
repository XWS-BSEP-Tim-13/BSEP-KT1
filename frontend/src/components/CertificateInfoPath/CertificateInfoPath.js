import classes from './CertificateInfoPath.module.css';

import { useSelector } from 'react-redux';
import CertificationService from '../../services/CertificationService';
import { useEffect, useState } from 'react';

function CertificateInfoPath(props) {
    const user = useSelector((state) => state.user.value);
    const [hierarchy, setHierarchy] = useState([]);

    useEffect(() => {
        CertificationService.findCertificateHierarchy(props.certificate.serialNumber)
            .then((res) => {
                setHierarchy(res.data);
            })
    }, [])

    function revokeCertificateHandler() {
        props.onRevoke();
    }

    return (
        <div className={classes.page}>
            <p className={classes.labelBold}>Certification path:</p>
            <div className={classes.status}>
                {
                    hierarchy.map((certificate) => {
                        return <div  key={certificate.certificateId}>
                                    <div> {certificate.subjectCommonName} </div> 
                                    {(certificate.certificateId !== props.certificate.serialNumber) && <div> | </div>}
                                </div>
                    })
                }
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