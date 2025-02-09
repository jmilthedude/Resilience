import React from "react";
import {Outlet} from "react-router-dom"; // To render child routes
import {AppShell, Burger, Text} from "@mantine/core";
import {useDisclosure} from "@mantine/hooks";
import {useAuth} from "./auth/AuthProvider";

const Layout = () => {
    const [opened, {toggle}] = useDisclosure();
    const {isLoggedIn} = useAuth(); // Check login status

    if (!isLoggedIn) {
        return <Outlet/>
    }

    return (
        <AppShell
            padding="md"
            navbar={{
                width: 300,
                breakpoint: 'sm',
                collapsed: {mobile: !opened},
            }}
        >
            <AppShell.Navbar p={"md"}>
                <Text>Dashboard</Text>
                <Text>Users</Text>
                <Text>Settings</Text>
                <Burger
                    opened={opened}
                    onClick={toggle}
                    hiddenFrom="sm"
                    size="sm"
                />
            </AppShell.Navbar>

            <Outlet/>

        </AppShell>
    );
};

export default Layout;
