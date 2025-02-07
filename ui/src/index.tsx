import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from "./App";
import {createTheme, MantineProvider, MantineThemeOverride} from "@mantine/core";
import "@mantine/core/styles.css"

const rootElement = document.getElementById("root");
if (!rootElement) {
    throw new Error("Root element not found");
}

const root = ReactDOM.createRoot(rootElement);
const theme = createTheme({

} as MantineThemeOverride);

root.render(
    <MantineProvider theme={theme} defaultColorScheme={"dark"}>
        <React.StrictMode>
            <App/>
        </React.StrictMode>
    </MantineProvider>
);
