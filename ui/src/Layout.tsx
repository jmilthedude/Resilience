import React, {useEffect} from "react";
import {NavLink, Outlet} from "react-router-dom"; // To render child routes
import {AppShell, Burger, SimpleGrid} from "@mantine/core";
import {useDisclosure} from "@mantine/hooks";
import {useAuth} from "./auth/AuthProvider";
import userService from "./api/services/UserService";
import {User} from "./types/user";
import {FiDollarSign, FiPieChart, FiSettings} from "react-icons/fi";

const Layout = () => {
    const [opened, {toggle}] = useDisclosure();
    const {isLoggedIn} = useAuth();
    const [user, setUser] = React.useState<User | null>(null);

    useEffect(() => {
        userService.getCurrentUser().then((user) => {
            setUser(user);
        });
    }, []);

    if (!isLoggedIn) {
        return <Outlet/>
    }

    return (
        <AppShell
            transitionTimingFunction="ease"
            navbar={{
                width: opened ? 225 : 0,
                breakpoint: 'sm',
                collapsed: {mobile: !opened}
            }}
        >
            <Burger
                style={{
                    position: "absolute",
                    left: 20,
                    top: 20,
                }}
                opened={opened}
                onClick={() => {
                    console.log("Toggling");
                    toggle();
                }
                }
                size="sm"
            />
            <AppShell.Navbar style={{
                transition: "width .3s",
                overflow: "hidden",
            }}>
                <div style={{
                    display: "flex",
                    alignItems: "flex-start",
                    justifyContent: "space-between",
                    paddingTop: 20,
                    paddingLeft: 20,
                    paddingRight: 20
                }}>
                    <h1 style={{margin: 0}}>Resilience</h1>
                    <Burger
                        style={{
                            visibility: opened ? "visible" : "hidden",
                            alignSelf: "right",
                        }}
                        opened={opened}
                        onClick={toggle}
                        size="sm"
                    />
                </div>
                <SimpleGrid cols={1} spacing="xs" style={{marginTop: 20, paddingLeft: 20}}>
                    <NavLink to="/dashboard" style={{
                        textDecoration: 'none',
                        display: "flex",
                        alignItems: "center",
                        gap: "10px"
                    }}><FiPieChart/>Dashboard</NavLink>
                    <NavLink to="/accounts" style={{
                        textDecoration: 'none',
                        display: "flex",
                        alignItems: "center",
                        gap: "10px"
                    }}><FiDollarSign/> Accounts</NavLink>
                    <NavLink to="/settings" style={{
                        textDecoration: 'none',
                        display: "flex",
                        alignItems: "center",
                        gap: "10px"
                    }}><FiSettings/>Settings</NavLink>
                    {user?.role === 'ADMIN' && (
                        <NavLink to="/users" style={{textDecoration: 'none', marginTop: 20}}>Admin</NavLink>
                    )}
                </SimpleGrid>
            </AppShell.Navbar>
            <AppShell.Main><Outlet/></AppShell.Main>
        </AppShell>
    );
};

export default Layout;
