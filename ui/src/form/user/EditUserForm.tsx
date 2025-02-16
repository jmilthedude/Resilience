import React, {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container, Input, Title} from "@mantine/core";
import {User} from "../../types/user";
import UserService from '../../api/services/UserService';

interface EditUserFormProps {
    user: User,
    onSuccess?: (updatedUser: User) => void
}

const EditUserPage = ({user, onSuccess}: EditUserFormProps) => {
    const [userData, setUserData] = useState<User>(user);

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setUserData((prev) => ({...prev, [name]: value}));
    };

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        UserService.updateUser(userData.id, userData)
            .then(() => {
                onSuccess && onSuccess(userData)
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
            })
            .finally(() => {
                setUserData({id: "", name: "", username: "", password: "", role: "USER"})
                alert("User updated successfully!");
            });
    };

    return (
        <Container size="md" style={{maxWidth: 400, textAlign: "center"}}>
            <Title order={2} mb="lg">
                Edit User
            </Title>
            <form onSubmit={handleSubmit}>
                <Input
                    name="name"
                    radius="lg"
                    mb="sm"
                    type="text"
                    placeholder="Name"
                    value={userData.name}
                    onChange={handleChange}
                    required
                />
                <Input
                    name="username"
                    radius="lg"
                    mb="sm"
                    type="text"
                    placeholder="Username"
                    value={userData.username}
                    onChange={handleChange}
                    required
                />
                <Input
                    name="password"
                    radius="lg"
                    mb="sm"
                    type="password"
                    placeholder="Password"
                    value={userData.password}
                    onChange={handleChange}
                />
                <Button variant="filled" fullWidth color="teal" radius="lg" type="submit">
                    Submit
                </Button>
            </form>
        </Container>
    );
};

export default EditUserPage;
