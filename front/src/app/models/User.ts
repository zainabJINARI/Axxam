export interface User {
    id?: number;
    username: string;
    password?: string; 
    email: string;
    fullname: string;
    appRoles: string[]; 
  }
  