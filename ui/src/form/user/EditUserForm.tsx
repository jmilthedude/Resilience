import React, {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container} from "@mantine/core";
import {User} from "../../types/user";
import UserService from '../../api/services/UserService';
import StyledTextInput from "../../components/StyledTextInput";
import {notifications} from "@mantine/notifications";

interface EditUserFormProps {
    user: User,
    onSuccess?: (updatedUser: User) => void
}

const EditUserPage = ({user, onSuccess}: EditUserFormProps) => {
    const [userData, setUserData] = useState<User>({
        id: user.id || "",
        name: user.name || "",
        username: user.username || "",
        password: user.password || "",
        role: user.role || "USER"
    });

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

                    console.error(`${errorMessage}\n${errorDetails}`);
                } else {
                    console.error("An unexpected error occurred. Please try again.");
                }
            })
            .finally(() => {
                setUserData({id: "", name: "", username: "", password: "", role: "USER"})
                notifications.show({
                    message: `User updated successfully!`,
                    color: "resilience",
                    icon: null
                })
            });
    };

    return (
        <Container size="md" style={{maxWidth: 400, textAlign: "center"}}>
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
                    name="password"
                    inputType="password"
                    placeholder="Password"
                    value={userData.password}
                    onChange={handleChange}
                />
                <Button variant="filled" fullWidth color="resilience" radius="md" type="submit" style={{marginTop: "40px"}}>
                    Submit
                </Button>
            </form>
        </Container>
    );
};

export default EditUserPage;
