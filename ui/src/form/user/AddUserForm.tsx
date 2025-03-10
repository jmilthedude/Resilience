import React, {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container} from "@mantine/core";
import {User} from "../../types/user";
import UserService from '../../api/services/UserService';
import StyledTextInput from "../../components/StyledTextInput";
import {notifications} from "@mantine/notifications";

interface AddUserFormProps {
    onSuccess?: (newUser: User) => void
}

const AddUserPage = ({onSuccess}: AddUserFormProps) => {
    const [userData, setUserData] = useState<User>({id: "", name: "", username: "", password: "", role: "USER"});

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setUserData((prev) => ({...prev, [name]: value}));
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        UserService.addUser(userData)
            .then(response => {
                onSuccess && onSuccess(response);
                setUserData({id: "", name: "", username: "", password: "", role: "USER"});
                notifications.show({
                    message: `User added successfully! id=${response.id}`,
                    color: "resilience",
                    icon: null
                })
            });
    };

    return (
        <Container size="md" style={{maxWidth: 400, margin: "auto", textAlign: "center"}}>
            <form onSubmit={handleSubmit}>
                <StyledTextInput
                    label={"Full Name"}
                    name="name"
                    placeholder="Full Name"
                    value={userData.name}
                    onChange={handleChange}
                    required
                />
                <StyledTextInput
                    label={"Username"}
                    name="username"
                    placeholder="Username"
                    value={userData.username}
                    onChange={handleChange}
                    required
                />
                <StyledTextInput
                    error={userData.password && (userData.password.length < 8 ? "Password must be at least 8 characters long" : "")}
                    label={"Password"}
                    name={"password"}
                    inputType="password"
                    placeholder="Password"
                    value={userData.password}
                    onChange={handleChange}
                    required
                />
                <Button variant="filled" fullWidth color="resilience" radius="md" type="submit" style={{marginTop: "40px"}}>
                    Submit
                </Button>
            </form>
        </Container>
    );
};

export default AddUserPage;
