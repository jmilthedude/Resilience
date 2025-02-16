import React, {useEffect} from "react";
import {Outlet, Link} from "react-router-dom"; // To render child routes
import {AppShell, Burger, NavLink, SimpleGrid} from "@mantine/core";
import {useDisclosure, useMediaQuery} from "@mantine/hooks";
import {useAuth} from "./auth/AuthProvider";
import userService from "./api/services/UserService";
import {User} from "./types/user";
import {FiBriefcase, FiDollarSign, FiPieChart, FiSettings} from "react-icons/fi";

const Layout = () => {
    const [opened, {toggle, close}] = useDisclosure(true);
    const {isLoggedIn} = useAuth();
    const [user, setUser] = React.useState<User | null>(null);
    const isMobile = useMediaQuery('(max-width: 768px)');

    useEffect(() => {
        if (isLoggedIn) {
            userService.getCurrentUser().then((user) => {
                setUser(user);
            });
        }
    }, [isLoggedIn]);

    const handleLinkClick = () => {
        if (isMobile) {
            close();
        }
    };

    if (!isLoggedIn) {
        return <Outlet/>
    }

    return (
        <AppShell
            transitionTimingFunction="ease"
            transitionDuration={500}
            navbar={{
                width: opened ? 225 : 0,
                breakpoint: 'sm',
                collapsed: {mobile: !opened},
            }}
        >
            <Burger
                style={{
                    position: "absolute",
                    left: 20,
                    top: 20,
                    zIndex: 100,
                }}
                opened={opened}
                onClick={toggle}
                size="sm"
            />
            <AppShell.Navbar style={{
                transition: "width 0.5s ease",
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
                    <NavLink label={"Dashboard"}
                             leftSection={<FiPieChart/>}
                             style={{
                                 textDecoration: 'none',
                                 display: "flex",
                                 alignItems: "center"
                             }}/>
                    <NavLink label={"Accounts"}
                             component={Link} to="/accounts"
                             leftSection={<FiDollarSign/>}
                             style={{
                                 textDecoration: 'none',
                                 display: "flex",
                                 alignItems: "center"
                             }}
                             onClick={handleLinkClick}/>
                    {user?.role === 'ADMIN' &&
                        (
                            <NavLink label={"Users"}
                                     component={Link} to="/users"
                                     leftSection={<FiBriefcase/>}
                                     style={{
                                         textDecoration: 'none'
                                     }}
                                     onClick={handleLinkClick}/>
                        )}
                    <NavLink label={"Settings"}
                             leftSection={<FiSettings/>}
                             childrenOffset={40}
                             style={{
                                 textDecoration: 'none',
                                 display: "flex",
                                 alignItems: "center"
                             }}>
                    </NavLink>
                </SimpleGrid>
            </AppShell.Navbar>
            <AppShell.Main><Outlet/></AppShell.Main>
        </AppShell>
    );
};

export default Layout;