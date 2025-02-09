import React, {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container, Input, Title} from "@mantine/core";
import {User} from "../types/user";
import UserService from '../api/services/UserService';

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
            .catch(error => {alert(error.message)})
            .then(() => {onSuccess && onSuccess(userData)})
            .finally(() => {
                setUserData({id: "", name: "", username: "", password: "", role: "USER"})
                alert("User added successfully!");
            });
    };

    return (
        <Container size="md" style={{maxWidth: 400, margin: "auto", textAlign: "center"}}>
            <Title order={2} mb="lg">
                Add User
            </Title>
            <form onSubmit={handleSubmit}>
                <Input autoComplete="new-name"
                    name="name"
                    radius="lg"
                    mb="sm"
                    type="text"
                    placeholder="Name"
                    value={userData.name}
                    onChange={handleChange}
                    required
                />
                <Input autoComplete="new-username"
                    name="username"
                    radius="lg"
                    mb="sm"
                    type="text"
                    placeholder="Username"
                    value={userData.username}
                    onChange={handleChange}
                    required
                />
                <Input autoComplete="new-password"
                    name="password"
                    radius="lg"
                    mb="sm"
                    type="password"
                    placeholder="Password"
                    value={userData.password}
                    onChange={handleChange}
                    required
                />
                <Button variant="filled" fullWidth color="teal" radius="lg" type="submit">
                    Submit
                </Button>
            </form>
        </Container>
    );
};

export default AddUserPage;
