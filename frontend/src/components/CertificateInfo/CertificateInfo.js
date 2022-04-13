import { useState, useEffect } from 'react';

import CertificateInfoGeneral from '../CertificateInfoGeneral/CertificateInfoGeneral';
import CertificateInfoDetails from '../CertificateInfoDetails/CertificateInfoDetails';
import CertificateInfoPath from '../CertificateInfoPath/CertificateInfoPath';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import classes from './CertificateInfo.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import CertificationService from '../../services/CertificationService';
import axios from 'axios';

function CertificateInfo(props) {
    const [selectedTab, setSelectedTab] = useState('general');
    const [certificate, setCertificate] = useState({
        id: '',
        serialNumber: '',
        purposes: [],
        subject: '',
        issuer: '',
        validFrom: '',
        validTo: '',
        signatureAlgorithm: '',
        signatureHashAlgorithm: '',
        publicKey: '',
        certificateStatus: true
    });

    function revokeCertificateHandler() {
        //ajax za povlacenje sertifikata
        axios.put(`http://localhost:8081/certificate/${props.certificateId}`)
        .then(() => {
            CertificationService.findCertificateById(props.certificateId)
            .then(resp=>{
                setCertificate(resp.data)
            })
        })
    }

    useEffect(() => {
        // ajax poziv za dobavljanje sertifikata po id-ju
        CertificationService.findCertificateById(props.certificateId)
        .then(resp=>{
            setCertificate(resp.data)
        })
    }, []);

    return (
        <div className={classes.modal}>
            <nav className={classes.nav}>
                <div className={classes.navItems}>
                    <button className={selectedTab === 'general' ? classes.navButtonSelected : classes.navButton}
                        onClick={() => setSelectedTab('general')} >
                        General
                    </button>
                    <button className={selectedTab === 'details' ? classes.navButtonSelected : classes.navButton}
                        onClick={() => setSelectedTab('details')} >
                        Details
                    </button>
                    <button className={selectedTab === 'path' ? classes.navButtonSelected : classes.navButton}
                        onClick={() => setSelectedTab('path')} >
                        Certification Path
                    </button>
                </div>
                <FontAwesomeIcon icon={faXmark} className={classes.xIcon} onClick={props.click} />
            </nav>

            {selectedTab === 'general' ? <CertificateInfoGeneral certificate={certificate} /> : null}
            {selectedTab === 'details' ? <CertificateInfoDetails certificate={certificate} /> : null}
            {selectedTab === 'path' ? <CertificateInfoPath certificate={certificate} onRevoke={revokeCertificateHandler}/> : null}
        </div>
    );
}

export default CertificateInfo;