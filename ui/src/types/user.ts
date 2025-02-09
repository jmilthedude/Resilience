export interface User {
    id: string;
    name: string;
    username: string;
    role: "ADMIN" | "USER";
    password?: string;
}
