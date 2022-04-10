import classes from './CertificateInfo.module.css';

function CertificateInfo(props) {

    return (
        <div className={classes.modal}>
            {props.certificateId}
        </div>
    );
}

export default CertificateInfo;