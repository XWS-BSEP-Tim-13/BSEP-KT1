import classes from './AllCertificates.module.css';

function AllCertificates() {
    return (
        <div className={classes.page}>
            <div className={classes.options}>
                <div>
                    <input type="radio" id="end-entity" name="certificateType" value="EndEntity" />
                    <label for="end-entity">End Entity</label>
                </div>
                <div>
                    <input type="radio" id="ca" name="certificateType" value="CA" />
                    <label for="ca">CA</label>
                </div>
                <div>
                    <input type="radio" id="root" name="certificateType" value="Root" />
                    <label for="root">Root</label>
                </div>
            </div>
        </div>
    );
}

export default AllCertificates;
