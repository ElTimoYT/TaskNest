export interface User {
  id: number; // Ojo, en el backend usamos Long (number)
  name: string;
  email: string;
  username: string;
  roles: string[];
}

