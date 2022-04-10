import classes from './CertificateInfoPath.module.css';

function CertificateInfoPath(props) {
    return (
        <div className={classes.page}>
            <p className={classes.labelBold}>Certification path:</p>
            <div className={classes.status}>
                
            </div>
            <p className={classes.labelBold}>Certificate status:</p>
            <div className={classes.status}>
                {props.certificate.certificateStatus ? 'This certificate is OK.' : 'Certfifcate is not valid.'}
            </div>
        </div>
    );
}

export default CertificateInfoPath;