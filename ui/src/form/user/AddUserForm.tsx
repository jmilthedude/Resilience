import React, {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container, TextInput} from "@mantine/core";
import {User} from "../../types/user";
import UserService from '../../api/services/UserService';

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
                alert("User added successfully! id="+ response.id);
                setUserData({id: "", name: "", username: "", password: "", role: "USER"});
            })
            .catch(error => {
                let data = error.response.data;
                if (data && data.message && data.details) {
                    const errorMessage = data.message || "An error occurred.";
                    const errorDetails = data.details ? data.details.join("\n") : "";

                    alert(`${errorMessage}\n${errorDetails}`);
                } else {
                    alert("An unexpected error occurred. Please try again.");
                }
            });
    };

    return (
        <Container size="md" style={{maxWidth: 400, margin: "auto", textAlign: "center"}}>
            <form onSubmit={handleSubmit}>
                <TextInput
                    label={"Full Name"}
                    name="name"
                    radius="md"
                    mb="sm"
                    type="text"
                    placeholder="Full Name"
                    value={userData.name}
                    onChange={handleChange}
                    required
                />
                <TextInput
                    label={"Username"}
                    name="username"
                    radius="md"
                    mb="sm"
                    type="text"
                    placeholder="Username"
                    value={userData.username}
                    onChange={handleChange}
                    required
                />
                <TextInput
                    error={userData.password && (userData.password.length < 8 ? "Password must be at least 8 characters long" : "")}
                    label={"Password"}
                    name="password"
                    radius="md"
                    mb="sm"
                    type="password"
                    placeholder="Password"
                    value={userData.password}
                    onChange={handleChange}
                />
                <Button variant="filled" fullWidth color="teal" radius="md" type="submit" style={{marginTop: "40px"}}>
                    Submit
                </Button>
            </form>
        </Container>
    );
};

export default AddUserPage;
