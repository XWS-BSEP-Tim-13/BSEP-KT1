import { useState, useEffect } from 'react';

import CertificateInfoGeneral from '../CertificateInfoGeneral/CertificateInfoGeneral';
import CertificateInfoDetails from '../CertificateInfoDetails/CertificateInfoDetails';
import CertificateInfoPath from '../CertificateInfoPath/CertificateInfoPath';
import { faXmark } from '@fortawesome/free-solid-svg-icons';
import classes from './CertificateInfo.module.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

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

    useEffect(() => {
        // ajax poziv za dobavljanje sertifikata po id-ju
        setCertificate({
            id: '1',
            serialNumber: '651516551',
            purposes: ['Proves your identity to a remote computer', 'Ensures the identity of a remote computer'],
            subject: 'Subject name',
            issuer: 'Issuer name',
            validFrom: '2/3/2022',
            validTo: '2/4/2023',
            signatureAlgorithm: 'sha256RSA',
            signatureHashAlgorithm: 'sha2560',
            publicKey: 'jhhzfbkzdhskjvjv',
            certificateStatus: true
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
            {selectedTab === 'path' ? <CertificateInfoPath certificate={certificate} /> : null}
        </div>
    );
}

export default CertificateInfo;