import { useEffect } from 'react';
import { useSelector } from 'react-redux';

function Homepage(props) {
    const user = useSelector((state) => state.user.value);

    useEffect(() => {
        console.log(user)
        console.log(`${new Date().getFullYear()}-${new Date().getMonth()}-${new Date().getDay()}`);
    }, [user]);

    return (
        <div>
            Homepage
        </div>
    );
}

export default Homepage;