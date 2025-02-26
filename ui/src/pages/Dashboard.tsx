import UserService from "../api/services/UserService";
import {User} from "../types/user";
import {useEffect, useState} from "react";


const Dashboard = () => {

    const [loggedInUser, setLoggedInUser] = useState<User>();

    useEffect(() => {
        UserService.getCurrentUser()
            .then((response) => setLoggedInUser(response))
    }, []);

    return (
        <div>
            {loggedInUser?.username}
        </div>
    );
};

export default Dashboard;