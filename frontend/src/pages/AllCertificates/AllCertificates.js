import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import CertificatesList from '../../components/CertificatesList/CertificatesList';
import classes from './AllCertificates.module.css';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import CertificationService from '../../services/CertificationService';


function AllCertificates() {
    const [selectedType, setSelectedType] = useState('ee');
    const [certificates,setCertificates] = useState([]);
    const user = useSelector((state) => state.user.value);

    
    useEffect(()=>{
        let type = 0
        if(selectedType === 'ee') type = 2
        else if (selectedType ==="ca") type=1
        CertificationService.findCertificatesByType(type)
        .then(resp=>{
            setCertificates(resp.data)
        })
    },[selectedType],[])

    return (
        <div className={classes.page}>
            <div className={classes.options}>
                <button className={selectedType === 'ee' ? classes.optionSelected : classes.option}
                    onClick={() => setSelectedType('ee')}>
                    End-Entity
                </button>
                <button className={selectedType === 'ca' ? classes.optionSelected : classes.option}
                    onClick={() => setSelectedType('ca')}>
                    CA
                </button>
                <button className={selectedType === 'root' ? classes.optionSelected : classes.option}
                    onClick={() => setSelectedType('root')}>
                    Root
                </button>
            </div>
            <div className={classes.searchOptions}>
                <div className={classes.search}>
                    { user.role === 'ROLE_ADMIN' ? 
                        <input type="text" placeholder="Search Subject..." className={classes.searchInput}></input> :
                        <input type="text" placeholder="Search Serial Number..." className={classes.searchInput}></input>
                    }
                    <FontAwesomeIcon icon={faMagnifyingGlass} className={classes.searchIcon} />
                </div> 
                <div className={classes.search}>
                    <input type="text" placeholder="Search Issuer..." className={classes.searchInput}></input>
                    <FontAwesomeIcon icon={faMagnifyingGlass} className={classes.searchIcon} />
                </div>
                <div className={classes.search}>
                    <input type="text" placeholder="Search Organization..." className={classes.searchInput}></input>
                    <FontAwesomeIcon icon={faMagnifyingGlass} className={classes.searchIcon} />
                </div>
            </div>
            <div>
                <CertificatesList certificates={certificates} selectedType={selectedType} />
            </div>
        </div>
    );
}

export default AllCertificates;
