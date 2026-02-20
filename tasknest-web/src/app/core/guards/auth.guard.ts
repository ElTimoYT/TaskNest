import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  
  // Comprobamos si hay un token guardado en el navegador
  // (Si en tu app lo llamaste de otra forma, ej: 'jwt', cámbialo aquí)
  const token = localStorage.getItem('token'); 

  if (token) {
    return true; // ¡Adelante, puedes pasar!
  } else {
    // No hay token, te devolvemos de una patada a la pantalla de login
    router.navigate(['/login']);
    return false;
  }
};