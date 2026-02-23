import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import * as AOS from 'aos';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RouterModule, MatIconModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {

  ngOnInit(): void {
    AOS.init({
      duration: 1000, // Duración de la animación (1 segundo)
      once: true,     // Que la animación solo ocurra una vez al bajar
      offset: 100,    // Distancia de scroll antes de animar
      easing: 'ease-out-cubic' // Curva de aceleración súper suave
    });
  }

}
