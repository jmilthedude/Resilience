export interface Account {
    id: string;
    name: string;
    type: "CHECKING" | "SAVINGS" | "CREDIT";
    accountNumber: string;
}
