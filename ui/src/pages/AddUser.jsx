import React, { useState } from "react";
import { Button, Container, Input, Title } from "@mantine/core";

const API_URL = "http://localhost:8081/api/v1/users";

const AddUserPage = () => {
    const [formData, setFormData] = useState({ username: "", password: "" });

    const handleChange = ({ target: { name, value } }) => {
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleErrorResponse = async (response) => {
        const textResponse = await response.text();
        try {
            const errorResponse = JSON.parse(textResponse);
            return errorResponse.errors?.[0]
                ? `Invalid ${errorResponse.errors[0].field}: ${errorResponse.errors[0].defaultMessage}`
                : errorResponse.message || "An unknown error occurred.";
        } catch {
            return textResponse || "An unknown error occurred.";
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(API_URL, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ ...formData, role: "USER" }),
            });

            if (response.ok) {
                alert("User added successfully!");
                setFormData({ username: "", password: "" });
            } else {
                alert(await handleErrorResponse(response));
            }
        } catch (error) {
            alert("Failed to connect to the server.");
        }
    };

    return (
        <Container size="md" style={{ maxWidth: 400, margin: "50px auto", textAlign: "center" }}>
            <Title order={2} mb="lg">
                Add User
            </Title>
            <form onSubmit={handleSubmit}>
                <Input
                    name="username"
                    radius="lg"
                    mb="sm"
                    type="text"
                    placeholder="Username"
                    value={formData.username}
                    onChange={handleChange}
                    required
                />
                <Input
                    name="password"
                    radius="lg"
                    mb="sm"
                    type="password"
                    placeholder="Password"
                    value={formData.password}
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
